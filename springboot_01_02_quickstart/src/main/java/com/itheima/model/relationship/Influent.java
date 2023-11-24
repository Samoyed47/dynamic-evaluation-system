package com.itheima.model.relationship;

import com.itheima.model.node.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
public class Influent {
    @RelationshipId
    private Long id;


    @Property("influence")
    private Integer influence;
    @TargetNode
    private User user;
    public Influent(Integer influence, Integer timestamp, User user){
        this.influence = influence;
        this.user = user;
    }

}
