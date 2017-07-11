package com.scorpiomiku.cookbook.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/6/3.
 */

public class RecommendFragment extends FragmentModule {

    private static final String TAG = "RecommendFragment";
    private FragmentManager fm;
    private Fragment mNowFragment;

    private ImageButton mBreakFastImageButton;
    private ImageButton mLunchImageButton;
    private ImageButton mDinnerImageButton;
    private Toolbar mToolbar;


    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .add(R.id.recommend_container, RecommendDefultFragment.newInstance())
                .commit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_fragment_layout, container, false);
        /*----------------------------initView---------------------------------*/
        mBreakFastImageButton = (ImageButton) v.findViewById(R.id
                .recommend_tool_bar_breakfast_iamge_button);
        mDinnerImageButton = (ImageButton) v.findViewById(R.id
                .recommend_tool_bar_dinner_iamge_button);
        mLunchImageButton = (ImageButton) v.findViewById(R.id
                .recommend_tool_bar_lunch_iamge_button);
        mToolbar = (Toolbar) v.findViewById(R.id.recommend_tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setListener();
        return v;
    }


    /*------------------------------changeFragment-----------------------------*/
    private void changeFragment(Fragment fragment) {
        mNowFragment = fm.findFragmentById(R.id.recommend_container);
        if (mNowFragment.getClass().getName().equals(fragment.getClass().getName())) {

        } else {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .hide(mNowFragment)
                    .add(R.id.recommend_container, fragment)
                    .commit();
        }
    }


    /*-------------------------------------setListener--------------------------*/
    private void setListener() {
        mBreakFastImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(BreakFastFragment.newInstance());
            }
        });
        mLunchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(LunchFragment.newInstance());
            }
        });
        mDinnerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(DinnerFragment.newInstance());
            }
        });
    }


}
