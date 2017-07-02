package com.scorpiomiku.cookbook.bean;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MenuStep {
    private int mRank ;
    private String mStepText ;
    private String mNutrition ;

    public void setNutrition(String nutrition) {
        mNutrition = nutrition;
    }

    public String getNutrition() {
        return mNutrition;
    }

    public MenuStep(String s , String nutrition){
        mStepText = s ;
        mNutrition = nutrition ;
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
