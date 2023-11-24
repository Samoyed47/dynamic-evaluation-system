package com.itheima.service;

import com.itheima.Springboot0102QuickstartApplication;
import com.itheima.model.node.Post;
import com.itheima.model.node.User;
import com.itheima.repository.PostRepository;
import com.itheima.repository.UserRepository;
import com.itheima.service.dataService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


@SpringBootTest(classes = Springboot0102QuickstartApplication.class)
public class dataServiceTest {
    @Autowired
    private dataService dataService;

    @Test
    void getUser() throws Exception{
        User user = dataService.getUserById(1);
        System.out.print(user);
    }

    @Test
    void getPost() throws Exception{
        Post post = dataService.getPostById(1);
        System.out.print(post);
    }
}
