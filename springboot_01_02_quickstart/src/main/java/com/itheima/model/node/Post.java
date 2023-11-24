package com.itheima.model.node;

import com.itheima.model.relationship.Comment;
import com.itheima.model.relationship.Hate;
import com.itheima.model.relationship.Like;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Data
@NoArgsConstructor
@Node("post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @Property("postId")
    private Integer postId;
    @Property("score")
    private Integer score;
    @Property("type")
    private Integer type;

    @Relationship(type="like",direction = Relationship.Direction.OUTGOING)
    private List<Like> likeSet;
    @Relationship(type="hate",direction = Relationship.Direction.OUTGOING)
    private List<Hate> hateSet;
    @Relationship(type="comment",direction = Relationship.Direction.OUTGOING)
    private List<Comment> commentSet;

    public Post(Integer score, Integer type, Integer postId){
        this.postId = postId;
        this.score = score;
        this.type = type;
    }

    public Post(Integer postId, Integer type){
        this.postId = postId;
        this.type = type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public Long getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getType() {
        return type;
    }
}
