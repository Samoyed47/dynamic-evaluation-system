package com.itheima.model.relationship;

import com.itheima.model.node.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;


@RelationshipProperties
public class Hot {
    @RelationshipId
    private Long id;

    @Property("hot")
    private Integer hot;
    @TargetNode
    private Post post;
    public Hot(Integer hot, Integer timestamp, Post post){
        this.hot = hot;
        this.post = post;
    }

    public Integer getHot() {
        return hot;
    }

    public Post getPost() {
        return post;
    }
}
