package com.scorpiomiku.cookbook.bean;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by a on 2017/3/2.
 */

public class Person extends BmobUser {
    private BmobFile Sculpture;
    private String name;
    private String AccountNumber;
    private String userPassword;
    private String sex;
    private String birthday;
    private String home;
    public void setSculpture(BmobFile drawable){
        Sculpture = drawable;
    }public BmobFile getSculpture(){
        return Sculpture;
    }
    public void setSex(String s){
        sex =s;
    }public void setBirthday(String b){
        birthday =b;
    }public void setHome(String h){
        home =h;
    }public String getSex(){
        return sex;
    }public String getBirthday(){
        return birthday;
    }public String getHome(){
        return home;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccountNumber() {
        return AccountNumber;
    }
    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }
    public String getuserPassword(){
        return userPassword;
    }
    public void setuserPassword(String userPassword){
        this.userPassword = userPassword;
    }
}
