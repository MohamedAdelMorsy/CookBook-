package com.scorpiomiku.cookbook.combination;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by a on 2017/6/13.
 */

public class Way extends BmobObject {
    private String FenLei;
    private String zuoFaMing;
    private String zuoFaTu;
    private String id;
    private BmobFile zuoFaTuUser;
    private boolean FromUser;
    private String objid;
    public void setObjid(String s){
        objid=s;
    }
    public String getObjid(){
        return objid;
    }
    public void setZuoFaMing(String zuoFaMing){
        this.zuoFaMing=zuoFaMing;
    }
    public void setZuoFaTu (String zuoFaTu){
        this.zuoFaTu = zuoFaTu;
    }
    public void setZuoFaTuUser(BmobFile zuoFaTuUser){
        this.zuoFaTuUser =zuoFaTuUser;
    }
    public void setFromUser(boolean a){
        FromUser = a;
    }
    /*口味*/

    private String kouWei;
    public void getKouWei(String kouWei) {
        this.kouWei = kouWei;
    }
    public String getZuoFaMing(){
        return zuoFaMing;
    }
    public String getZuoFaTu(){
        return zuoFaTu;
    }
    /**
     * 适用时段*/
    private String shiDuan;
    public void setShiDuan(String shiduan){
        shiDuan= shiduan;
    }
    /**做法
     * */
    private String zuoFa;
    public void setZuoFa(String zuoFa){
        this.zuoFa=zuoFa;
    }
     /**耗时*/
    private String haoShi;
    private int cishu;
    public String UpUserName;
}
