package com.scorpiomiku.cookbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/6/15.
 */

public class TestFragment extends FragmentModule {
    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_fragment_layout, container, false);
        return v ;
    }
}
