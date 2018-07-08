package com.scorpiomiku.cookbook.bean;

/**
 * Created by Administrator on 2018/5/11.
 */


public class CaiZuEC_Gai {
    private String name;
    private String objId;
    private String objId1;
    private String objId2;
    private String objId3;
    private String objId4;
    private String Introduce;
    private boolean FromUser;
    private String imageurl1;
    private String imageurl2;
    private String imageurl3;
    private String imageurl4;
    private String ShiCai1;
    private String ShiCai2;
    public CaiZuEC_Gai(String name, String image1,String image2,String image3,String image4, String Introduce, String ShiCai1, String ShiCai2, String Way_objectID,String ID2,String ID3,String ID4, boolean FromUser){
        this.name = name;
        this.imageurl1 =image1;
        this.imageurl2 =image2;
        this.imageurl3 =image3;
        this.imageurl4 =image4;
        this.Introduce = Introduce;
        this.ShiCai1 = ShiCai1;
        this.ShiCai2 = ShiCai2;
        this.objId=Way_objectID;
        this.objId1=Way_objectID;
        this.objId2=ID2;
        this.objId3=ID3;
        this.objId4=ID4;
        this.FromUser = FromUser;
    }
    public String getObjId1(){
        return objId1;
    }
    public String getObjId2(){
        return objId2;
    }
    public String getObjId3(){
        return objId3;
    }
    public String getObjId4(){
        return objId4;
    }
    public String getName(){
        return name;
    }
    public String getImageurl1(
    ){
        return imageurl1;
    }
    public String getImageurl2(
    ){
        return imageurl2;
    }
    public String getImageurl3(
    ){
        return imageurl3;
    }
    public String getImageurl4(
    ){
        return imageurl4;
    }
    public String getObjId(){
        return objId;
    }
    public String getShiCai1(){
        return ShiCai1;
    }
    public String getShiCai2(){
        return ShiCai2;
    }
}
