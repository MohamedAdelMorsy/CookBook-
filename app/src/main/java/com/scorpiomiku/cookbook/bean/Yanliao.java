package com.scorpiomiku.cookbook.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by a on 2017/10/9.
 */

public class Yanliao extends BmobObject {
    private String name;
    private String jieshao;
    private String yingyang;
    public String getName(){
        return name;
    }
    public String getJieshao(){
        return jieshao;
    }
    public String getYingyang(){
        return yingyang;
    }
    public Yanliao(String n,String j,String y){
        name=n;
        jieshao=j;
        yingyang = y;
    }
}
