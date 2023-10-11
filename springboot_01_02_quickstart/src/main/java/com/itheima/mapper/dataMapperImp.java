package com.itheima.mapper;

import com.itheima.model.Post;
import com.itheima.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class dataMapperImp implements dataMapper{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // 根据用户id获取用户的热度得分
    public User getUserById(Integer id) throws Exception{
        String selectUserData = "select * from User where id=?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        try {
            // 执行查询
            User user = jdbcTemplate.queryForObject(selectUserData,rowMapper,id);
            // 处理查询结果
            if (user != null) {
                return user;
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    // 根据帖子id获得帖子的热度得分
    public Post getPostById(Integer id) throws Exception{
        String selectPostData = "select * from Post where id=?";
        RowMapper<Post> rowMapper = new BeanPropertyRowMapper<Post>(Post.class);
        try {
            // 执行查询
            Post post = jdbcTemplate.queryForObject(selectPostData,rowMapper,id);
            // 处理查询结果
            if (post != null) {
                return post;
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    // 获得某个领域排序靠前的n个帖子
    public List<Post> getPostsBySort(Integer n, Integer type) throws Exception{
        String postSortSql = "select * from Post where type="+type+" order by score desc";
        List<Post> posts = jdbcTemplate.query(postSortSql, new Object[]{}, new BeanPropertyRowMapper<Post>(Post.class));
        if(posts.size()>n){
            return posts.subList(0,n);
        }else {
            return posts;
        }
    }
    // 获得热度排序靠前的n个用户
    public List<User> getUsersBySort(Integer n) throws Exception{
        String userSortSql = "select * from User order by score desc";
        List<User> users = jdbcTemplate.query(userSortSql, new Object[]{}, new BeanPropertyRowMapper<User>(User.class));
        if(users.size()>n){
            return users.subList(0,n);
        }else {
            return users;
        }
    }

    // 修改用户热度
    public Integer updateUser(Integer id, Integer score) throws Exception{
        String sql;
        if(getUserById(id)==null){
            // 用户不存在
            sql = "insert into User(id,score) values ("+id+","+score+")";
        }else{
            sql = "update User set score="+score+" where id="+id;
        }
        return jdbcTemplate.update(sql);
    }

    // 修改帖子热度
    public Integer updatePost(Integer id, Integer score, Integer type) throws Exception{
        String sql;
        if(getPostById(id)==null){
            // 帖子不存在
            sql = "insert into Post(id,type,score) values ("+id+","+type+","+score+")";
        }else{
            sql = "update Post set score="+score+" where id="+id;
        }
        return jdbcTemplate.update(sql);
    }
}
