package com.itheima.model.relationship;

import com.itheima.model.node.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
public class Like {
    @RelationshipId
    private Long id;

    @Property("timestamp")
    private Integer timestamp;
    @TargetNode
    private User user;

    public Like(Integer timestamp, User user){
        this.timestamp = timestamp;
        this.user = user;
    }
}
