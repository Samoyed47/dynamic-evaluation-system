package com.itheima.model;

public class User {
    private Integer id;
    private Integer score;
    public User(){}
    public User(Integer id,Integer score){
        this.id = id;
        this.score = score;
    }
    public Integer getId(){
        return id;
    }
    public Integer getScore(){
        return score;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setScore(Integer score){
        this.score = score;
    }
}
