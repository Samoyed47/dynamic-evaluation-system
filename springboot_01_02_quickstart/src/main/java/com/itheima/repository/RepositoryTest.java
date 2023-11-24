package com.itheima.repository;

import com.itheima.Springboot0102QuickstartApplication;
import com.itheima.model.node.Post;
import com.itheima.model.node.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Springboot0102QuickstartApplication.class)
public class RepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    void getUser() throws Exception{
        User user = userRepository.findByUserId(1);
        System.out.print(user);
    }

    @Test
    void getPost() throws Exception{
        Post post = postRepository.findByPostId(1);
        System.out.print(post);
    }

    @Test
    void getUsers() throws Exception{
        List<Integer> users = userRepository.findUserBySort(1,1);
        System.out.print(users);
    }

    @Test
    void create() throws Exception{
        userRepository.createFanRelation(1,2,1);
    }
}
