package com.scorpiomiku.cookbook.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by a on 2017/10/6.
 */

public class Collection extends BmobObject {
    private String userObjID;
    private BmobRelation comment;//多对多关系：用于存储用户喜欢的帖子
    public void setComment(BmobRelation h){
        comment = h;
    }
    public void setUserObjID(String usid){
        userObjID = usid;
    }
}
