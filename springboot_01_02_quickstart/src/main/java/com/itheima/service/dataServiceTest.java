package com.itheima.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class dataServiceTest {
    @Autowired
    private dataService dataService;

    @Test
    void testUpdate() throws Exception{
        List<PostData> posts = new ArrayList<PostData>();
        posts.add(new PostData(1,34,3,4,5,6));
        posts.add(new PostData(2,4,56,42,2,64));
        dataService.update(posts);
    }
}
