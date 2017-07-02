package com.scorpiomiku.cookbook.menuActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/7/1.
 */

public class DefaultFragment extends FragmentModule {

    private ImageView mImageView;
    private TextView mStepsTextView;
    private TextView mNutritionTextView;

    public static DefaultFragment newInstance() {
        return new DefaultFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_steps_default_layout, container, false);
        mImageView = (ImageView) v.findViewById(R.id.menu_defualt_fragment_steps_iamge_view);
        mStepsTextView = (TextView) v.findViewById(R.id.menu_defualt_fragment_steps_text_view);
        mNutritionTextView = (TextView) v.findViewById(R.id.menu_defualt_fragment_nutrition_text_view);
        initView();
        return v;
    }

    /*---------------------------------initView--------------------------*/
    private void initView() {
        mImageView.setImageResource(R.drawable.test);
        mStepsTextView.setText("这里放第一步的操作");
        mNutritionTextView.setText("这里放营养介绍");
    }
}
