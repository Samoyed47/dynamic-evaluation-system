package com.itheima.service;

import com.itheima.model.Post;
import com.itheima.model.User;

import java.util.List;

public interface dataService {
    // 根据用户id获取用户的热度得分
    User getUserById(Integer id) throws Exception;
    // 根据帖子id获得帖子的热度得分
    Post getPostById(Integer id) throws Exception;
    // 获得某个领域排序靠前的n个帖子
    List<Post> getPostsBySort(Integer n, Integer type) throws Exception;
    // 获得热度排序靠前的n个用户
    List<User> getUsersBySort(Integer n) throws Exception;
    // 根据论坛新发送的数据修改数据库内容
   void update(String jsonString) throws Exception;
}
