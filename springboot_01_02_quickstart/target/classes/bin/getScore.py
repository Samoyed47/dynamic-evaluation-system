from tqdm import tqdm
from py2neo import Graph, Node, Relationship
from py2neo.matching import *
import math

# 用户影响力
user_value = [] #影响力
user_rank = [] #影响力排名
user_activation = [] #活跃度
quality_index = [] #质量指数
direct_index = [] #直接质量指数
indirect_index = [] #间接质量指数
retweet_rate = [] #用户发帖被转发率
comment_rate = [] #用户发帖被评论率
sum_user = 0 #用户总数
user_retweet = [] #用户发帖被转发次数
user_post = [] #用户发帖数
user_comment = [] #用户发帖被评论数
user_originality = [] #原创度
user_fan = [] #粉丝集合
exp_rank = [] #期望排名
change_value = [] #影响力变化量
user_type = [] #用户知识领域

time = 1 #时间周期
d = 0.15 #阻尼系数
now_time = 0 #最新周期数

#帖子热度
post_hot = [] #热度
post_user = [] #发帖人
like_count = [] #点赞集合
hate_count= [] #点踩集合
post_comment = [] #帖子评论集合
post_type = [] #帖子知识领域
sum_post = 0 #帖子总数
w_user = 20 #作者权重
w_like = 20 #点赞权重
w_hate = -10 #点踩权重
w_comment = 40 #评论权重
k = -0.5 #衰减因子
post_time = [] #发帖时间

#计算结果
userList = list()
postList = list()

#缓存数据
pre_user = [] #历史用户数据
pre_post = [] #历史帖子数据
pre_value = [] #用户历史影响力
new_user = list() #新用户数据
new_post = list() #新帖子数据

# 连接到数据库
uri = "bolt://localhost:7687"  # 数据库的URI
username = "neo4j"     # 数据库的用户名
password = "12345678"     # 数据库的密码

# 创建一个Graph对象
graph = Graph(uri, auth=(username, password))

#统计用户总数与帖子总数
for node in graph.nodes:
    if graph.nodes[node]['userId'] != None:
        sum_user = max(sum_user, graph.nodes[node]['userId'] + 1)
    else:
        if graph.nodes[node]['postId'] != None:
            sum_post = max(sum_post, graph.nodes[node]['postId'] + 1)
        else:
            now_time = max(now_time, graph.nodes[node]['timestamp'])

#预处理
for i in range(0, sum_post):
    post_user.append(0)
    post_type.append(0)
    post_time.append(0)
    post_comment.append([])
    like_count.append([])
    hate_count.append([])

#预处理
for i in range(0, sum_user):
    user_comment.append(0)
    user_retweet.append(0)
    user_post.append(0)
    user_fan.append([])
    user_type.append(0)
    pre_value.append(1500)
    exp_rank.append(1.0)
    change_value.append(0)

#获取相关数据
for node in graph.nodes:
    if graph.nodes[node]['userId'] != None:
        now_id = graph.nodes[node]['userId']  #用户id
        user_type[now_id] = graph.nodes[node]['type']
        pre_value[now_id] = graph.nodes[node]['score'] #用户历史影响力
    else:
        if graph.nodes[node]['postId'] != None:
            now_id = graph.nodes[node]['postId']  #帖子id
            post_type[now_id] = graph.nodes[node]['type']

# 边
query = """
MATCH (source)-[relationship]->(target)
RETURN source, type(relationship), target, properties(relationship) AS relationship_properties
"""

result = graph.run(query)

for record in result:
    source = record["source"]
    relationship = record["type(relationship)"]
    target = record["target"]
    relationship_properties = record["relationship_properties"]
    if relationship == 'write':
        post_id = target['postId']
        post_user[post_id] = source['userId']
        post_time[post_id] = relationship_properties['timestamp']
        user_post[source['userId']] = user_post[source['userId']] + 1 #更新用户发帖数
    if relationship == 'like':
        post_id = source['postId']
        user_id = target['userId']
        like_count[post_id].append([user_id, relationship_properties['timestamp']]) #更新帖文点赞集合
    if relationship == 'hate':
        post_id = source['postId']
        user_id = target['userId']
        hate_count[post_id].append([user_id, relationship_properties['timestamp']]) #更新帖文点踩集合
    if relationship == 'comment':
        post_id = source['postId']
        user_id = target['userId']
        post_comment[post_id].append([user_id, relationship_properties['timestamp']]) #更新帖文评论集合
    if relationship == 'fans':
        user_A = source['userId']
        user_B = target['userId']
        user_fan[user_A].append(user_B) #更新用户粉丝集合

for i in range(0, sum_post):
    user_comment[post_user[i]] = user_comment[post_user[i]] + len(post_comment[i]) #更新用户被评论数
    user_retweet[post_user[i]] = user_retweet[post_user[i]] + len(like_count[i]) + len(hate_count[i]) #更新用户被转发数

for i in range(0, sum_user):
    retweet = 0
    comment = 0
    origin = 0
    if user_post[i] != 0:
        retweet = user_retweet[i] / user_post[i] / sum_user #计算用户发帖被转发率
        comment = user_comment[i] / user_post[i] / sum_user #计算用户发帖被评论率
        origin = (user_post[i] + user_post[i]) / user_post[i] / sum_user #计算原创度
        
    retweet_rate.append(retweet)
    comment_rate.append(comment)
    user_originality.append(origin)
    
    act = user_post[i] / time #计算活跃度
    user_activation.append(act)

    direct = retweet_rate[i] + comment_rate[i] + user_originality[i] #计算直接质量指数
    direct_index.append(direct)

    indirect_index.append(0)

#迭代计算间接质量指数
Count = 10000

for i in tqdm(range(0, Count)):
    tmp_index = []

    for j in range(0, sum_user):
        indirect = d

        for l in user_fan[j]:
            if len(user_fan[l]) != 0:
                indirect = indirect + (1 - d) * (direct_index[l] + indirect_index[l]) / len(user_fan[l])

        tmp_index.append(indirect)
    
    flag = 0
    for j in range(0, sum_user):
        if indirect_index[j] == tmp_index[j]:
            flag = flag + 1
        indirect_index[j] = tmp_index[j]
    
    if flag == sum_user:
        break

for i in  range(0, sum_user):
    quality = direct_index[i] + indirect_index[i] #计算质量指数
    quality_index.append(quality)

    value = quality_index[i] * user_activation[i]  #计算影响力
    user_value.append(value)

#计算实际排名
for i in range(0, sum_user):
    user_rank.append([i, user_value[i]])
user_rank.sort(key = lambda x : x[1])

rank_user = 1
for i in user_rank:
    user_value[i[0]] = sum_user - rank_user + 1
    rank_user = rank_user + 1

#计算期望排名
for i in range(0, sum_user):
    for j in range(0, sum_user):
        if i == j:
            continue
        else:
            exp_rank[i] = exp_rank[i] + 1.0 / (1.0 + 10 ** ((pre_value[i] - pre_value[j]) / 400))

#计算用户影响力变化值
for i in range(0, sum_user):
    tmp_rank = (exp_rank[i] * (1.0 * user_value[i])) ** 0.5
    left = 1
    right = 8000

    new_value = 0
    while left <= right:
        mid = (left + right) // 2
        new_rank = 1
        for j in range(0, sum_user):
            new_rank = new_rank + 1.0 / (1.0 + 10 ** ((mid - pre_value[j]) / 400))
        if new_rank < tmp_rank:
            right = mid - 1
        else:
            new_value = mid
            left = mid + 1
    change_value[i] = (new_value - pre_value[i]) // 2

#微调
s = min(sum_user, round(4 * (sum_user ** 0.5)))
inc = 0
for i in range(0, sum_user):
    inc = inc + change_value[i]
inc = inc + 1
inc = inc // sum_user
inc = -inc
new_inc = 1
for i in range(0, s):
    new_inc = new_inc + change_value[i]
    now_inc = new_inc // (i + 1)
    now_inc = -now_inc
    inc = min(inc, max(now_inc, -10))
inc = min(inc, 0)

#计算最终用户影响力
for i in range(0, sum_user):
    change_value[i] = change_value[i] + inc
    user_value[i] = pre_value[i] + change_value[i]

#计算帖子热度
for i in range(0, sum_post):
    value = user_value[post_user[i]] * w_user * (2 if user_type[post_user[i]] == post_type[i] else 0.5)
    like_value = 0
    hate_value = 0
    comment_value = 0

    for like in like_count[i]:
        like_value = like_value + user_value[like[0]] * math.exp(like[1])
    for hate in hate_count[i]:
        hate_value = hate_value + user_value[hate[0]] * math.exp(hate[1])
    for comment in post_comment[i]:
        comment_value = comment_value + user_value[comment[0]] * math.exp(comment[1])
    
    like_value = like_value * w_like
    hate_value = hate_value * w_hate
    comment_value = comment_value * w_comment
    value = value + like_value - hate_value + comment_value
    
    value = value * math.exp(k * (now_time - post_time[i]))
    value = round(value)
    post_hot.append(value)

#修改用户节点影响力值、帖文节点热度值
matcher = NodeMatcher(graph)
user_node = matcher.match('user').all()
for now_node in user_node:
    now_node['score'] = user_value[now_node['userId']]
    graph.push(now_node)
post_node = matcher.match('post').all()
for now_node in post_node:
    now_node['score'] = post_hot[now_node['postId']]
    graph.push(now_node)

#添加影响力边、热度边
influence_node = matcher.match('influenceSource').where(timestamp = now_time).first()
hot_node = matcher.match('hotSource').where(timestamp = now_time).first()
for i in range(0, sum_user):
    now_node = matcher.match('user').where(userId = i).first()
    relation = Relationship(now_node, "influent", influence_node, influence = user_value[i])
    graph.create(relation)
for i in range(0, sum_post):
    now_node = matcher.match('post').where(postId = i).first()
    relation = Relationship(now_node, "hot", hot_node, hot = post_hot[i])
    graph.create(relation)