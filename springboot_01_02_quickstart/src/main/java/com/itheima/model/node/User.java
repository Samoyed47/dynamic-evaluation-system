package com.itheima.model.node;

import com.itheima.model.relationship.Fans;
import com.itheima.model.relationship.Write;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Node("user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Property("userId")
    private Integer userId;
    @Property("score")
    private Integer score;
    @Property("type")
    private Integer type;

    private Integer fansCount;

    private Integer mostValuePostScore;

    @Relationship(type="fans",direction = Relationship.Direction.OUTGOING)
    private List<Fans> fans;
    @Relationship(type="write",direction = Relationship.Direction.OUTGOING)
    private List<Write> writes;

    public User(Integer userId, Integer score, Integer type){
        this.userId = userId;
        this.score = score;
        this.type = type;
    }

    public User(Integer userId, Integer type){
        this.userId = userId;
        this.type = type;
    }


    public void setScore(Integer score) {
        this.score = score;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public Integer getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public List<Fans> getFans() {
        return fans;
    }

    public List<Write> getWrites() {
        return writes;
    }

    public List<Post> getWritePost(){
        List<Post> posts = new ArrayList<>();
        for(Write write: writes){
            posts.add(write.getPost());
        }
        return posts;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public Integer getMostValuePostScore() {
        return mostValuePostScore;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public void setMostValuePostScore(Integer mostValuePostScore) {
        this.mostValuePostScore = mostValuePostScore;
    }
}
