package com.scorpiomiku.cookbook.bean;

import java.util.List;


/**
 * Created by a on 2017/7/12.
 */

public class CBLEC {
    private String postId;
    private String name;
    private String imageurl;
    // private Drawable image;

    private String objId;
    private String ShiCaiMing;
    private String userImage;
    private boolean FromUser;
    private List<SSEC> ssec;
    private int geshu;

    public void setSsec(List<SSEC> h){
        ssec = h;
    }
    public List<SSEC> getSsec (){
        return ssec;
    }
    public void setObjId(String h){
        objId = h;
    }
    public String getObjId(){
        return objId;
    }
    public void setName(String s){
        name = s;
    }
    public void setImageurl(String s){
        imageurl = s;
    }
    public CBLEC(String name, String image, String ShiCaiMing1,String Way_objectID, boolean FromUser, List<SSEC> ssecs){
        ssec = ssecs;
        this.name = name;
        this.imageurl =image;
        this.ShiCaiMing = ShiCaiMing1;
        this.objId=Way_objectID;
        this.FromUser = FromUser;
        postId = "";
    }
    public CBLEC(String name, String image, String ShiCaiMing1, String Way_objectID, boolean FromUser){

        this.name = name;
        this.imageurl =image;

        this.ShiCaiMing = ShiCaiMing1;
        this.objId=Way_objectID;
        this.FromUser = FromUser;
        postId ="";
    }
    public CBLEC(String name, String image,  String ShiCaiMing1, String Way_objectID, boolean FromUser, List<SSEC> ssecs,String postId,int s,String userImage){
        this.userImage = userImage;
        geshu = s;
        ssec = ssecs;
        this.name = name;
        this.imageurl =image;
        this.ShiCaiMing = ShiCaiMing1;
        this.objId=Way_objectID;
        this.FromUser = FromUser;
        this.postId = postId;
    }
    public String getName(){
        return name;
    }
    public String getImageurl(
    ){
        return imageurl;
    }
    public String getShiCaiMing(){
        return ShiCaiMing;
    }
    public boolean getFromUser(){
        return FromUser;
    }
    public String getPostId(){
        return postId;
    }
    public int getGeshu(){
        return geshu;
    }
}
