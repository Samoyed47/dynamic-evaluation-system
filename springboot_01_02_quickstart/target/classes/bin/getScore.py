import json
import random

if __name__ == '__main__':
    # 读取数据
    data = json.load(open("data.json", "r"))
    #  print(ans)
    ###
    #  算法
    ###
    # 最后生成的结果
    userList = list()
    postList = list()
    # 测试数据
    for i in range(20):
        user = dict()
        user['id'] = i
        user['score'] = random.randint(1, 1000)
        userList.append(user)
        post = dict()
        post['id'] = i
        post['type'] = random.randint(1, 10)
        post['score'] = random.randint(1, 1000)
        postList.append(post)
    json.dump(userList, open('userScore.json', 'w'))
    json.dump(postList, open('postScore.json', 'w'))