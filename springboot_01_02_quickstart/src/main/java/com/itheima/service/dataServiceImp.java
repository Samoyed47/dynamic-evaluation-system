package com.itheima.service;

import com.itheima.model.node.Post;
import com.itheima.model.node.User;
import com.itheima.repository.HotSourceRepository;
import com.itheima.repository.InfluenceSourceRepository;
import com.itheima.repository.PostRepository;
import com.itheima.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.min;

@Service
public class dataServiceImp implements dataService{
   @Autowired
   PostRepository postRepository;
   @Autowired
   UserRepository userRepository;
   @Autowired
    HotSourceRepository hotSourceRepository;
   @Autowired
    InfluenceSourceRepository influenceSourceRepository;
    private static final Logger logger = LoggerFactory.getLogger(dataServiceImp.class);

    @Override
    // 根据用户id获取用户的热度得分
    public User getUserById(Integer id) throws Exception{
        User user = userRepository.findByUserId(id);
        user.setFansCount(userRepository.getUserFans(id));
        if(userRepository.getMAXUserPostScore(id)==0){
            user.setMostValuePostScore(-1);
        }else{
            user.setMostValuePostScore(userRepository.getMAXUserPostScore(id));
        }
        return user;
    }

    @Override
    // 根据帖子id获得帖子的热度得分
    public Post getPostById(Integer id) throws Exception{
        return postRepository.findByPostId(id);
    }

    @Override
    // 获得某个领域排序靠前的n个帖子
    public List<Post> getPostsBySort(Integer n, Integer type, Integer timestamp) throws Exception{
        Integer max_time = hotSourceRepository.getMaxTimestamp();
        if(timestamp==-1){
            timestamp = max_time;
        }else{
            timestamp = min(max_time, timestamp);
        }
        List<Post> posts = postRepository.findPostBySort(timestamp, type);
        if(posts.size()<n){
            return posts;
        }else{
            return posts.subList(0,n);
        }
    }

    @Override
    // 获得热度排序靠前的n个用户
    public List<User> getUsersBySort(Integer n, Integer type, Integer timestamp) throws Exception{
        Integer max_time = influenceSourceRepository.getMaxTimestamp();
        if(timestamp==-1){
            timestamp = max_time;
        }else{
            timestamp = min(max_time, timestamp);
        }
        List<Integer> users = userRepository.findUserBySort(timestamp, type);
        if(users.size()>n){
            users = users.subList(0,n);
        }
        List<User> userList = new ArrayList<>();
        for(Integer userId: users){
            userList.add(getUserById(userId));
        }
        return userList;
    }
    // 根据论坛新发送的数据修改数据库内容
    public void update(List<Map<String,Object>> users, List<Map<String,Object>> posts) throws Exception{
        // 添加新增的用户节点
        for(Map<String,Object> user:users){
            Integer userId = (Integer) user.get("userId");
            Integer type = (Integer) user.get("type");
            //List<Map<String,Object>> fans = (List<Map<String,Object>>)user.get("fansSet");
            if(!userRepository.existsUserByUserId(userId)){
                userRepository.save(new User(userId,1500,type));
            }
        }
        // 添加粉丝关系
        for(Map<String,Object> user:users){
            List<Map<String,Object>> fans = (List<Map<String,Object>>)user.get("fansSet");
            Integer userId = (Integer) user.get("userId");
            for(Map<String,Object> fan: fans){
                Integer fanId = (Integer)fan.get("id");
                Integer timestamp = (Integer)fan.get("timestamp");
                userRepository.createFanRelation(userId,fanId,timestamp);
            }
        }
        logger.info("创建或修改用户节点"+users.size()+"个");
        for(Map<String,Object> post:posts){
            Integer postId = (Integer) post.get("postId");
            Integer type = (Integer) post.get("type");
            Integer writeId = (Integer) post.get("writerId");
            if(!postRepository.existsPostByPostId(postId)){
                Integer timestamp = (Integer) post.get("timestamp");
                // 添加帖子节点
                postRepository.save(new Post(1500,type,postId));
                // 创建撰写关系
                postRepository.createWriteRelation(writeId,postId,timestamp);
            }
            // 创建点赞关系
            List<Map<String,Object>> likes = (List<Map<String,Object>>)post.get("likeSet");
            for(Map<String,Object>like : likes){
                Integer likeId = (Integer)like.get("id");
                Integer timestamp = (Integer)like.get("timestamp");
                postRepository.createLikeRelation(likeId,postId,timestamp);
            }
            // 创建点踩关系
            List<Map<String,Object>> hates = (List<Map<String,Object>>)post.get("hateSet");
            for(Map<String,Object>hate : hates){
                Integer hateId = (Integer)hate.get("id");
                Integer timestamp = (Integer)hate.get("timestamp");
                postRepository.createHateRelation(hateId,postId,timestamp);
            }
            // 创建评论关系
            List<Map<String,Object>> comments = (List<Map<String,Object>>)post.get("commentSet");
            for(Map<String,Object>comment : comments){
                Integer commentId = (Integer)comment.get("id");
                Integer timestamp = (Integer)comment.get("timestamp");
                postRepository.createCommentRelation(commentId,postId,timestamp);
            }
        }
        logger.info("创建或修改帖子节点"+posts.size()+"个");
        Integer hot_time=hotSourceRepository.getMaxTimestamp();
        Integer influence_time = influenceSourceRepository.getMaxTimestamp();
        if(hot_time==null){
            hot_time=0;
        }
        if(influence_time==null){
            influence_time=0;
        }
        // 创建本次结果对应的源点
        hotSourceRepository.createHotSource(hot_time+1);
        influenceSourceRepository.createInfluenceSource(influence_time+1);

        // 运行脚本文件
        String srcPath = getSrcPath();
        try {
            // 获取脚本文件所在的目录
            String exePath = srcPath+"\\bin\\getScore.py";
            File directory = new File(exePath).getParentFile();

            // 构造 ProcessBuilder 对象
            ProcessBuilder processBuilder = new ProcessBuilder("python", exePath);

            // 设置运行目录
            processBuilder.directory(directory);
            // 纳秒级
            //long timestamp = System.currentTimeMillis() * 1000000L + System.nanoTime() % 1000000L;
            // 毫秒级
            long timestamp = System.currentTimeMillis();
            // 启动外部可执行文件
            Process process = processBuilder.start();
            // 等待子进程执行结束
            int exitCode = process.waitFor();
            timestamp = System.currentTimeMillis()-timestamp;
            logger.info("算法运行花费"+timestamp+"ms");
            if (exitCode == 0) {
                System.out.println("外部程序执行成功！");
            } else {
                System.out.println("外部程序执行失败！");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getSrcPath() throws Exception{
        ClassLoader classLoader = dataService.class.getClassLoader();
        URL resourceURL = classLoader.getResource("");
        String resourcePath = null;
        try {
            resourcePath = URLDecoder.decode(resourceURL.getFile(), StandardCharsets.UTF_8.name());
            resourcePath = resourcePath.substring(1,resourcePath.length());
            Path windowsPath = Paths.get(resourcePath);
            String windowsPathWithChinese = windowsPath.toString();
            return windowsPathWithChinese;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
