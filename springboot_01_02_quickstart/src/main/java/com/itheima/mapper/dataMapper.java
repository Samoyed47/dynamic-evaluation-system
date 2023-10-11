package com.itheima.mapper;

import com.itheima.model.Post;
import com.itheima.model.User;

import java.util.List;

public interface dataMapper {
    // 根据用户id获取用户的热度得分
    User getUserById(Integer id) throws Exception;
    // 根据帖子id获得帖子的热度得分
    Post getPostById(Integer id) throws Exception;
    // 获得某个领域排序靠前的n个帖子
    List<Post> getPostsBySort(Integer n, Integer type) throws Exception;
    // 获得热度排序靠前的n个用户
    List<User> getUsersBySort(Integer n) throws Exception;
    // 修改用户热度
    Integer updateUser(Integer id, Integer score) throws Exception;
    // 修改帖子热度
    Integer updatePost(Integer id, Integer score, Integer type) throws Exception;
}
