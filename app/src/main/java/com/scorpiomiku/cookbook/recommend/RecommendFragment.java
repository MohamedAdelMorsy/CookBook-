package com.scorpiomiku.cookbook.recommend;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuFragment;

/**
 * Created by Administrator on 2017/6/3.
 */

public class RecommendFragment extends FragmentModule {

    private static final String TAG = "RecommendFragment";
    private FragmentManager fm;
    private Fragment mNowFragment;

    private ImageView mBreakFastImageView;
    private ImageView mLunchImageView;
    private ImageView mDinnerImageView;
    private Toolbar mToolbar;

    private ImageView mMenuImageView;

    private EditText mSearchEditText;
    private ImageView mSearchImageView;


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
        mBreakFastImageView = (ImageView) v.findViewById(R.id
                .recommend_tool_bar_breakfast_iamge_view);
        mDinnerImageView = (ImageView) v.findViewById(R.id
                .recommend_tool_bar_dinner_iamge_view);
        mLunchImageView = (ImageView) v.findViewById(R.id
                .recommend_tool_bar_lunch_iamge_view);
        mMenuImageView = (ImageView) v.findViewById(R.id.recommend_tool_bar_menu_image_view);
        mSearchEditText = (EditText) v.findViewById(R.id.recommend_search_edit_view);
        mSearchImageView = (ImageView) v.findViewById(R.id.recommend_search_image_view);
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
        mBreakFastImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(BreakFastFragment.newInstance());
            }
        });
        mLunchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(LunchFragment.newInstance());
            }
        });
        mDinnerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(DinnerFragment.newInstance());
            }
        });
        mMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(mMenuImageView, "scaleX", 1.2f).setDuration(100);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(mMenuImageView, "scaleY", 1.2f).setDuration(100);
                ObjectAnimator lowX = ObjectAnimator.ofFloat(mMenuImageView, "scaleX", 1f).setDuration(100);
                ObjectAnimator lowY = ObjectAnimator.ofFloat(mMenuImageView, "scaleY", 1f).setDuration(100);
                AnimatorSet anis = new AnimatorSet();
                anis.play(scaleX).with(scaleY).before(lowX).before(lowY);
                anis.start();
                Fragment nowFragment = fm.findFragmentById(R.id.fragment_container);
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_top,
                                android.R.anim.fade_out)
                        .replace(R.id.fragment_container, RecommendMenuFragment.newInstance())
                        .commit();
            }
        });
        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mSearchEditText.getText().toString();
                Intent i = new Intent(getActivity(), ClassifierResultActivity.class);
                i.putExtra("foodname", text);
                i.putExtra("FragmentSendMessage", "RecommendFragment");
                mSearchEditText.setText("");
                startActivity(i);
            }
        });
    }


}
