package com.itheima.service;

import com.itheima.model.node.Post;
import com.itheima.model.node.User;

import java.util.List;
import java.util.Map;

public interface dataService {
    // 根据用户id获取用户的热度得分
    User getUserById(Integer id) throws Exception;
    // 根据帖子id获得帖子的热度得分
    Post getPostById(Integer id) throws Exception;
    // 获得某个领域排序靠前的n个帖子
    List<Post> getPostsBySort(Integer n, Integer type, Integer timestamp) throws Exception;
    // 获得热度排序靠前的n个用户
    List<User> getUsersBySort(Integer n, Integer type, Integer timestamp) throws Exception;
    // 根据论坛新发送的数据修改数据库内容
   void update(List<Map<String,Object>> users, List<Map<String,Object>> posts) throws Exception;
}
