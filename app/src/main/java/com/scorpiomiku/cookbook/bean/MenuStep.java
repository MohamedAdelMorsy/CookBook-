package com.scorpiomiku.cookbook.bean;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MenuStep {
    private int mRank ;
    private String mStepText ; //步骤描述
    private String mNutrition ;
    private String Imageurl;
    public void setNutrition(String nutrition) {
        mNutrition = nutrition;
    }

    public String getNutrition() {
        return mNutrition;
    }

    public MenuStep(String s , String nutrition,String Imageurl){
        mStepText = s ;
        mNutrition = nutrition ;
        this.Imageurl = Imageurl;
    }
    public String getImageurl(){
        return Imageurl;
    }
    public int getRank() {
        return mRank;
    }

    public String getStepText() {
        return mStepText;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public void setStepText(String stepText) {
        mStepText = stepText;
    }
}

