package com.scorpiomiku.cookbook.bean;

/**
 * Created by a on 2017/9/4.
 */

public class TestCPEC {

    private int mRank ;
    private String Name;
    private String Mimage;

    public TestCPEC(String Name, String drawable){
        this.Name=Name;
        Mimage=drawable;
    }
    public int getRank() {
        return mRank;
    }
    public String getName(){
        return Name;
    }
    public String getMimage(){
        return Mimage;
    }
     public void setRank(int rank) {
       mRank = rank;
      }
}
