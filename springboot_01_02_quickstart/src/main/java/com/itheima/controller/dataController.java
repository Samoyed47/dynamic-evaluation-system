package com.itheima.controller;


import com.itheima.model.Post;
import com.itheima.model.User;
import com.itheima.service.PostData;
import com.itheima.service.dataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class dataController {
    @Autowired
    private dataService dataService;
    @GetMapping("/user/{user_id}")
    public ResponseData<User> getUserById(@PathVariable Integer user_id) throws Exception{
        User user  = dataService.getUserById(user_id);
        if(user==null){
            return new ResponseData<User>(null, "用户不存在",400);
        }
        return new ResponseData<User>(user, "成功",200);
    }
    @GetMapping("/user/sort")
    public ResponseData<List<User>> getUserBySort(HttpServletRequest request) throws Exception{
        Integer n = Integer.valueOf(request.getParameter("n"));
        List<User> users = dataService.getUsersBySort(n);
        return new ResponseData<List<User>>(users, "成功",200);
    }
    @GetMapping("/post/{post_id}")
    public ResponseData<Post> getPostById(@PathVariable Integer post_id) throws Exception{
        Post post  = dataService.getPostById(post_id);
        if(post==null){
            return new ResponseData<Post>(null, "帖子不存在",400);
        }
        return new ResponseData<Post>(post, "成功",200);
    }
    @GetMapping("/post/sort")
    public ResponseData<List<Post>> getPostBySort(HttpServletRequest request) throws Exception{
        Integer n = Integer.valueOf(request.getParameter("n"));
        Integer type = Integer.valueOf(request.getParameter("type"));
        List<Post> posts = dataService.getPostsBySort(n, type);
        return new ResponseData<List<Post>>(posts, "成功",200);
    }
    @PostMapping("/info")
    public ResponseData<Object> update(@RequestBody List<PostData> postData)  throws Exception{
        dataService.update(postData);
        return new ResponseData<Object>(null, "成功",200);
    }
}
