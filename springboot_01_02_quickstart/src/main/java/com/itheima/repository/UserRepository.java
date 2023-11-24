package com.itheima.repository;

import com.itheima.model.node.User;
import org.springframework.data.domain.Example;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends Neo4jRepository<User,Long> {
    // 根据用户id查询用户
    @Query("MATCH (u:user{userId:$userId}) RETURN u")
    User findByUserId(Integer userId);
    // 按影响力对用户排序
    @Query("MATCH (u:user)-[r:influent]->(s:influenceSource{timestamp:$timestamp}) " +
            "WHERE u.type = $type " +
            "RETURN u.userId ORDER BY r.influence DESC")
    List<Integer> findUserBySort(Integer timestamp, Integer type);

    // 查询用户粉丝数
    @Query("MATCH (u:user{userId:$userId})-[r:fans]-() RETURN count(r)")
    Integer getUserFans(Integer userId);

    // 查询用户帖子的最大值
    @Query("MATCH (u:user{userId:$userId})-[r:write]->(p:post) RETURN max(p.score)")
    Integer getMAXUserPostScore(Integer userId);

    @Override
    <S extends User> S save(S entity);

    @Query("MATCH (u:user{userId:$userId}),(f:user{userId:$fanId})"+
            "CREATE (u)-[:fans{timestamp:$timestamp}]->(f)")
    void createFanRelation(Integer userId, Integer fanId, Integer timestamp);

    boolean existsUserByUserId(Integer userId);
}
