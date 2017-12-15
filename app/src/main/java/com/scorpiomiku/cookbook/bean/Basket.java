package com.scorpiomiku.cookbook.bean;

import android.graphics.drawable.Drawable;

/**
 * Create d by  Administrator  on 2017/7/15.
 */

public class Basket {
    private String mName;
    private String mMaterial;

    public Basket(String name, String material) {
        mName = name;
        mMaterial = material;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setMaterial(String material) {
        mMaterial = material;
    }

    public String getName() {
        return mName;
    }

    public String getMaterial() {
        return mMaterial;
    }
}
