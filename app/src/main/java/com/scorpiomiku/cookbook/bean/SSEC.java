package com.scorpiomiku.cookbook.bean;

/**
 * Created by a on 2017/8/10.
 */

public class SSEC {
    private String Share_usserObjID;
    private String Share_name;
    private String Share_neirong;
    public SSEC(String Share_name, String Share_neirong){
        this.Share_name = Share_name;
        this.Share_neirong = Share_neirong;
    }
    public SSEC(String Share_name, String Share_neirong, String Share_usserObjID){
        this.Share_name = Share_name;
        this.Share_neirong = Share_neirong;
        this.Share_usserObjID = Share_usserObjID;
    }
    public String getShare_name(){
        return Share_name;

    }
    public String getShare_neirong(){
        return Share_neirong;
    }
}
