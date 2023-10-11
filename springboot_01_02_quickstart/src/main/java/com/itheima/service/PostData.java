package com.itheima.service;

public class PostData {
    private Integer postId;
    private Integer writerId;
    private Integer type;
    private Integer likeCount;
    private Integer hateCount;
    private Integer commentScore;
    public PostData(){}
    public PostData(Integer postId,Integer writerId,Integer type,Integer likeCount,Integer hateCount,Integer commentScore){
        this.postId = postId;
        this.writerId = writerId;
        this.type = type;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
        this.commentScore = commentScore;
    }

    public Integer getCommentScore() {
        return commentScore;
    }

    public Integer getHateCount() {
        return hateCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getType() {
        return type;
    }

    public Integer getWriterId() {
        return writerId;
    }

    public void setCommentScore(Integer commentScore) {
        this.commentScore = commentScore;
    }

    public void setHateCount(Integer hateCount) {
        this.hateCount = hateCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setWriterId(Integer writerId) {
        this.writerId = writerId;
    }
}
