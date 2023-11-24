package com.itheima.repository;

import com.itheima.model.node.InfluenceSource;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluenceSourceRepository extends Neo4jRepository<InfluenceSource,Long> {

    @Query("CREATE (s:influenceSource{timestamp:$timestamp})")
    void createInfluenceSource(Integer timestamp);
    @Query("MATCH (s:influenceSource) RETURN max(s.timestamp)")
    Integer getMaxTimestamp();
}
