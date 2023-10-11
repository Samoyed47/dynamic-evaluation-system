package com.itheima.mapper;

import com.itheima.model.Post;
import com.itheima.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
public class dataMapperTest {
    @Autowired
    private dataMapper dataMapper;

    @Test
    void getUser() throws Exception{
        User user = dataMapper.getUserById(1);
        System.out.printf("user_id: %d, score: %d\n",user.getId(),user.getScore());
    }

    @Test
    void getPost() throws Exception{
        Post post = dataMapper.getPostById(17);
        System.out.printf("post_id: %d, type: %d, score: %d\n",post.getId(),post.getType(),post.getScore());
    }

    @Test
    void getUserSort() throws Exception{
        List<User> users = dataMapper.getUsersBySort(3);
        for(User user : users){
            System.out.printf("user_id: %d, score: %d\n",user.getId(),user.getScore());
        }
    }

    @Test
    void getPostSort() throws Exception{
        List<Post> posts = dataMapper.getPostsBySort(3,2);
        for(Post post : posts){
            System.out.printf("post_id: %d, type: %d, score: %d\n",post.getId(),post.getType(),post.getScore());
        }
    }
    @Rollback(false)
    @Test
    void updatePost() throws Exception{
        Integer i = dataMapper.updatePost(17, 90, 2);
        System.out.printf("%d\n",i);
    }

    @Rollback(false)
    @Test
    void updateUser() throws Exception{
        Integer i = dataMapper.updateUser(12, 100);
        System.out.printf("%d\n",i);
    }
}
