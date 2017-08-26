package com.scorpiomiku.cookbook.classifierresult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

import java.security.PublicKey;

/**
 * Created by Administrator on 2017/8/26.
 */

public class ClassifierResultFragment extends FragmentModule {

    private String mFoodName;
    private String mPicturePath;

    public static ClassifierResultFragment newInstance(String foodname, String picturePath) {
        ClassifierResultFragment mClassifierResultFragment = new ClassifierResultFragment();
        mClassifierResultFragment.mFoodName = foodname;
        mClassifierResultFragment.mPicturePath = picturePath;
        return mClassifierResultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.classifier_result_fragment,container,false);
        return v;
    }
}
