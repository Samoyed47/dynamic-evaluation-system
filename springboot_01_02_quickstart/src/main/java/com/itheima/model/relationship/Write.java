package com.itheima.model.relationship;

import com.itheima.model.node.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
public class Write {
    @RelationshipId
    private Long id;

    @Property("timestamp")
    private Integer timestamp;

    @TargetNode
    private Post post;

    public Write(Integer timestamp, Post post){
        this.timestamp = timestamp;
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
