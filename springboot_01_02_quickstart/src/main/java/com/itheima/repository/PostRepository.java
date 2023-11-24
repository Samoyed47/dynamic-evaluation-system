package com.itheima.repository;

import com.itheima.model.node.Post;
import org.springframework.data.domain.Example;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PostRepository extends Neo4jRepository<Post,Long> {
    // 根据帖子id查询帖子
    @Query("MATCH (p:post{postId:$postId}) RETURN p")
    Post findByPostId(Integer postId);
//    // 按热度对帖子排序
    @Query("MATCH (p:post)-[r:hot]->(s:hotSource{timestamp:$timestamp}) \n" +
            "WHERE p.type = $type\n" +
            "RETURN p ORDER BY r.hot DESC")
    List<Post> findPostBySort(Integer timestamp, Integer type);
    // 创建点赞关系
    @Query("MATCH (u:user{userId:$userId}),(p:post{postId:$postId})"+
    " CREATE (p)-[:like{timestamp:$timestamp}]->(u)")
    void createLikeRelation(Integer userId, Integer postId, Integer timestamp);
    // 创建点踩关系
    @Query("MATCH (u:user{userId:$userId}),(p:post{postId:$postId})"+
            " CREATE (p)-[:hate{timestamp:$timestamp}]->(u)")
    void createHateRelation(Integer userId, Integer postId, Integer timestamp);
    // 创建评论关系
    @Query("MATCH (u:user{userId:$userId}),(p:post{postId:$postId})"+
            " CREATE (p)-[:comment{timestamp:$timestamp}]->(u)")
    void createCommentRelation(Integer userId, Integer postId, Integer timestamp);
    // 创建撰写关系
    @Query("MATCH (u:user{userId:$userId}),(p:post{postId:$postId})"+
            " CREATE (u)-[r:write{timestamp:$timestamp}]->(p)")
    void createWriteRelation(Integer userId, Integer postId, Integer timestamp);

    boolean existsPostByPostId(Integer postId);
}
