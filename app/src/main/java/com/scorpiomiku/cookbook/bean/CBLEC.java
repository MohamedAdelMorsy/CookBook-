package com.scorpiomiku.cookbook.bean;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class CBLEC {
    private String name;
    private Drawable image;
    private String Introduce;
    private String objId;
    private String ShiCaiMing1;
    private String ShiCaiMing2;
    private String ShiCaiMing3;
    private String ShiCaiMing4;
    private String ShiCaiMing5;
    private String ShiCaiMing6;
    private String ShiCaiMing7;
    private String ShiCaiMing8;
    private String ShiCaiMing9;
    private String ShiCaiLiang1;
    private String ShiCaiLiang2;
    private String ShiCaiLiang3;
    private String ShiCaiLiang4;
    private String ShiCaiLiang5;
    private String ShiCaiLiang6;
    private String ShiCaiLiang7;
    private String ShiCaiLiang8;
    private String ShiCaiLiang9;
    private List<SSEC> ssec;

    public void setSsec(List<SSEC> h) {
        ssec = h;
    }

    public List<SSEC> getSsec() {
        return ssec;
    }

    public void setObjId(String h) {
        objId = h;
    }

    public String getObjId() {
        return objId;
    }

    public CBLEC(String name, Drawable image, String Introduce, String ShiCaiMing1, String ShiCaiMing2, String ShiCaiMing3, String ShiCaiMing4, String ShiCaiMing5, String ShiCaiMing6, String ShiCaiMing7, String ShiCaiMing8, String ShiCaiMing9, String ShiCaiLiang1, String ShiCaiLiang2, String ShiCaiLiang3, String ShiCaiLiang4, String ShiCaiLiang5, String ShiCaiLiang6, String ShiCaiLiang7, String ShiCaiLiang8, String ShiCaiLiang9, String Way_objectID, List<SSEC> ssecs) {
        ssec = ssecs;
        this.name = name;
        this.image = image;
        this.Introduce = Introduce;
        this.ShiCaiMing1 = ShiCaiMing1;
        this.ShiCaiMing2 = ShiCaiMing2;
        this.ShiCaiMing3 = ShiCaiMing3;
        this.ShiCaiMing4 = ShiCaiMing4;
        this.ShiCaiMing5 = ShiCaiMing5;
        this.ShiCaiMing6 = ShiCaiMing6;
        this.ShiCaiMing7 = ShiCaiMing7;
        this.ShiCaiMing8 = ShiCaiMing8;
        this.ShiCaiMing9 = ShiCaiMing9;
        this.ShiCaiLiang1 = ShiCaiLiang1;
        this.ShiCaiLiang2 = ShiCaiLiang2;
        this.ShiCaiLiang3 = ShiCaiLiang3;
        this.ShiCaiLiang4 = ShiCaiLiang4;
        this.ShiCaiLiang5 = ShiCaiLiang5;
        this.ShiCaiLiang6 = ShiCaiLiang6;
        this.ShiCaiLiang7 = ShiCaiLiang7;
        this.ShiCaiLiang8 = ShiCaiLiang8;
        this.ShiCaiLiang9 = ShiCaiLiang9;
        this.objId = Way_objectID;
    }

    public CBLEC(String name, Drawable image, String Introduce, String ShiCaiMing1, String ShiCaiMing2, String ShiCaiMing3, String ShiCaiMing4, String ShiCaiMing5, String ShiCaiMing6, String ShiCaiMing7, String ShiCaiMing8, String ShiCaiMing9, String ShiCaiLiang1, String ShiCaiLiang2, String ShiCaiLiang3, String ShiCaiLiang4, String ShiCaiLiang5, String ShiCaiLiang6, String ShiCaiLiang7, String ShiCaiLiang8, String ShiCaiLiang9, String Way_objectID) {

        this.name = name;
        this.image = image;
        this.Introduce = Introduce;
        this.ShiCaiMing1 = ShiCaiMing1;
        this.ShiCaiMing2 = ShiCaiMing2;
        this.ShiCaiMing3 = ShiCaiMing3;
        this.ShiCaiMing4 = ShiCaiMing4;
        this.ShiCaiMing5 = ShiCaiMing5;
        this.ShiCaiMing6 = ShiCaiMing6;
        this.ShiCaiMing7 = ShiCaiMing7;
        this.ShiCaiMing8 = ShiCaiMing8;
        this.ShiCaiMing9 = ShiCaiMing9;
        this.ShiCaiLiang1 = ShiCaiLiang1;
        this.ShiCaiLiang2 = ShiCaiLiang2;
        this.ShiCaiLiang3 = ShiCaiLiang3;
        this.ShiCaiLiang4 = ShiCaiLiang4;
        this.ShiCaiLiang5 = ShiCaiLiang5;
        this.ShiCaiLiang6 = ShiCaiLiang6;
        this.ShiCaiLiang7 = ShiCaiLiang7;
        this.ShiCaiLiang8 = ShiCaiLiang8;
        this.ShiCaiLiang9 = ShiCaiLiang9;
        this.objId = Way_objectID;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage(
    ) {
        return image;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public String getShiCaiMing1() {
        return ShiCaiMing1;
    }

    public String getShiCaiMing2() {
        return ShiCaiMing2;
    }

    public String getShiCaiMing3() {
        return ShiCaiMing3;
    }

    public String getShiCaiMing4() {
        return ShiCaiMing4;
    }

    public String getShiCaiMing5() {
        return ShiCaiMing5;
    }

    public String getShiCaiMing6() {
        return ShiCaiMing6;
    }

    public String getShiCaiMing7() {
        return ShiCaiMing7;
    }

    public String getShiCaiMing8() {
        return ShiCaiMing8;
    }

    public String getShiCaiMing9() {
        return ShiCaiMing9;
    }

    public String getShiCaiLiang1() {
        return ShiCaiLiang1;
    }

    public String getShiCaiLiang2() {
        return ShiCaiLiang2;
    }

    public String getShiCaiLiang3() {
        return ShiCaiLiang3;
    }

    public String getShiCaiLiang4() {
        return ShiCaiLiang4;
    }

    public String getShiCaiLiang5() {
        return ShiCaiLiang5;
    }

    public String getShiCaiLiang6() {
        return ShiCaiLiang6;
    }

    public String getShiCaiLiang7() {
        return ShiCaiLiang7;
    }

    public String getShiCaiLiang8() {
        return ShiCaiLiang8;
    }

    public String getShiCaiLiang9() {
        return ShiCaiLiang9;
    }
}
