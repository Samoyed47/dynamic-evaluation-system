package com.itheima.model.relationship;

import com.itheima.model.node.User;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
public class Comment {
    @RelationshipId
    private Long id;

    @Property("timestamp")
    private Integer timestamp;

    @TargetNode
    private User user;

    public Comment(Integer timestamp, User user){
        this.timestamp = timestamp;
        this.user = user;
    }
}
