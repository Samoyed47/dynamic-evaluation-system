package com.itheima.model;

public class Post {
    private Integer id;
    private Integer type;
    private Integer score;
    public Post(){
    }
    public Post(Integer id, Integer type, Integer score){
        this.id = id;
        this.type =type;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public Integer getType(){
        return type;
    }

    public  Integer getScore(){
        return score;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setType(Integer type){
        this.type = type;
    }

    public void setScore(Integer score){
        this.score = score;
    }
}
