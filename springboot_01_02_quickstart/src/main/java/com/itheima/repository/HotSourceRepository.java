package com.itheima.repository;

import com.itheima.model.node.HotSource;
import com.itheima.model.node.Post;
import com.itheima.model.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotSourceRepository extends Neo4jRepository<HotSource, Long> {
    @Query("MATCH (s:hotSource) RETURN max(s.timestamp)")
    Integer getMaxTimestamp();
    @Query("CREATE (s:hotSource{timestamp:$timestamp})")
    void createHotSource(Integer timestamp);
}
