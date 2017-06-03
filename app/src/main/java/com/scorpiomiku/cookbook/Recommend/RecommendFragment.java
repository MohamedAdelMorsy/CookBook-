package com.scorpiomiku.cookbook.Recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.scorpiomiku.cookbook.R;

/**
 * Created by Administrator on 2017/6/3.
 */

public class RecommendFragment extends Fragment {

    private static final String TAG = "RecommendFragment";
    private FragmentManager fm;

    private ImageButton mBreakFastImageButton;
    private ImageButton mLunchImageButton;
    private ImageButton mDinnerImageButton;


    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
        changeFragment(BreakFastFragment.newInstance());
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
        setListener();
        return v;
    }

    /*-------------------------------------changeFragment--------------------------*/
    private void changeFragment(Fragment fragment) {
        Fragment fra = fm.findFragmentById(R.id.recommend_container);
        if (!(fragment == fra)) {
            fra = fragment;
        }
        fm.beginTransaction()
                .replace(R.id.recommend_container, fra)
                .commit();
    }

    /*-------------------------------------setListener--------------------------*/
    private void setListener(){
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
