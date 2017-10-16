package com.scorpiomiku.cookbook.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by a on 2017/7/21.
 */

    public class Comment extends BmobObject {

    private String content;//评论内容

    private Person user;//评论的用户，Pointer类型，一对一关系

    private Post post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博

    public void setContent(String a){
        content=a;
    }
    public void setUser(Person s){
        user=s;
    }
    public void setPost(Post hh){
        post =hh;
    }
    public Person getUser(){
        return user;
    }
    public String getContent(){
        return content;
    }
    public Post getPost(){
        return post;
    }
}
