package com.itheima.controller;


import com.itheima.exception.BadRequestException;
import com.itheima.model.node.Post;
import com.itheima.model.node.User;
import com.itheima.service.dataService;
import com.itheima.service.dataServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class dataController {
    @Autowired
    private dataService dataService;
    @GetMapping("/user/{user_id}")
    public ResponseData<Map<String,Object>> getUserById(@PathVariable Integer user_id) throws Exception{
        
        User user  = dataService.getUserById(user_id);
        if(user==null){
            //return new ResponseData<>(null, "不存在",400);
            throw new BadRequestException("用户不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId",user.getUserId());
        map.put("type",user.getType());
        map.put("score",user.getScore());
        map.put("fanCount",user.getFansCount());
        map.put("mostValuePostScore",user.getMostValuePostScore());
        return new ResponseData<Map<String,Object>>(map, "成功",200);
    }
    @GetMapping("/user/sort")
    public ResponseData<List<Map<String,Object>>> getUserBySort(HttpServletRequest request) throws Exception{
        Integer type = Integer.valueOf(request.getParameter("type"));
        Integer timestamp=-1;
        if(request.getParameter("timestamp")!=null) {
            timestamp = Integer.valueOf(request.getParameter("timestamp"));
        }
        Integer n = Integer.valueOf(request.getParameter("n"));
        List<User> users = dataService.getUsersBySort(n,type,timestamp);
        if(users.size()==0){
            //return new ResponseData<>(null, "不存在",400);
            throw new BadRequestException("无匹配项");
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for(User user:users){
            Map<String, Object> map = new HashMap<>();
            map.put("userId",user.getUserId());
            map.put("type",user.getType());
            map.put("score",user.getScore());
            map.put("fanCount",user.getFansCount());
            map.put("mostValuePostScore",user.getMostValuePostScore());
            maps.add(map);
        }
        return new ResponseData<List<Map<String, Object>>>(maps, "成功",200);
    }
    @GetMapping("/post/{post_id}")
    public ResponseData<Map<String,Object>> getPostById(@PathVariable Integer post_id) throws Exception{
        Post post  = dataService.getPostById(post_id);
        if(post==null){
            //return new ResponseData<>(null, "不存在",400);
            throw new BadRequestException("帖子不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("postId",post.getPostId());
        map.put("type",post.getType());
        map.put("score",post.getScore());
        return new ResponseData<Map<String,Object>>(map, "成功",200);
    }
    @GetMapping("/post/sort")
    public ResponseData<List<Map<String,Object>>> getPostBySort(HttpServletRequest request) throws Exception{
        Integer n = Integer.valueOf(request.getParameter("n"));
        Integer type = Integer.valueOf(request.getParameter("type"));
        Integer timestamp=-1;
        if(request.getParameter("timestamp")!=null) {
            timestamp = Integer.valueOf(request.getParameter("timestamp"));
        }
        List<Post> posts = dataService.getPostsBySort(n, type,timestamp);
        if(posts.size()==0){
            //return new ResponseData<>(null, "不存在",400);
            throw new BadRequestException("无匹配项");
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for(Post post:posts){
            Map<String, Object> map = new HashMap<>();
            map.put("postId",post.getPostId());
            map.put("type",post.getType());
            map.put("score",post.getScore());
            maps.add(map);
        }
        return new ResponseData<List<Map<String,Object>>>(maps, "成功",200);
    }
    @PostMapping("/info")
    public ResponseData<Object> update(@RequestBody Map<String,Object> maps)  throws Exception{
        List<Map<String,Object>> users = (List<Map<String,Object>>)maps.get("users");
        List<Map<String,Object>> posts = (List<Map<String,Object>>)maps.get("posts");
        dataService.update(users,posts);
        return new ResponseData<Object>(null, "成功",200);
    }
}
