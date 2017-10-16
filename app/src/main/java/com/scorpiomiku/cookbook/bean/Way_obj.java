package com.scorpiomiku.cookbook.bean;

/**
 * Created by a on 2017/10/5.
 */

public class Way_obj {
    private String Way_objectID;
    public Way_obj(String Way_objectID){
        this.Way_objectID = Way_objectID;
    }
    public void setmName(String mName){
        this.Way_objectID = mName;
    }
    public String getmName(){
        return Way_objectID;
    }
}
