package com.scorpiomiku.cookbook.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by a on 2017/7/21.
 */

public class Post extends BmobObject {

    private String title;//帖子标题

    private String content;// 帖子内容

    private String objID; //连接到的菜谱ID

    private Person author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private BmobFile image;//帖子图片

    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户
    private int geshu;
    //自行实现getter和setter方法
    public void setGeshu(int s){
        geshu = s;
    }
    public void  setAuthor(Person A){
        author = A;
    }
    public void setContent(String A){
        content = A;
    }
    public void setObjID(String ID){
        objID = ID;
    }
    public String getObjID(){
        return objID;
    }
    public String getContent(){
        return content;
    }
    public Person getAuthor(){
        return author;
    }
    public void setLikes(BmobRelation h ){
        likes=h;
    }
    public  int getGeshu(){
        return geshu;
    }
}
