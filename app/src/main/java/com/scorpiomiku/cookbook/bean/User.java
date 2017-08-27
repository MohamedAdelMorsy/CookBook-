package com.scorpiomiku.cookbook.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/27.
 */

public class User extends BmobUser {
    private String name;
    private String AccountNumber;
    private String userPassword;

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

    public String getuserPassword() {
        return userPassword;
    }

    public void setuserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
