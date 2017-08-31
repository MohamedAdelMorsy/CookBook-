package com.scorpiomiku.cookbook.ownInformation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.basket.BasketActivity;
import com.scorpiomiku.cookbook.dynamic.DynamicActivity;
import com.scorpiomiku.cookbook.dynamic.MyDynamicActivity;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.sign.SignInActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MyInformationFragment extends FragmentModule {

    private ImageView mAccoutImageView;
    private TextView mAccountNameTextView;

    private ImageView mBasketImageView;
    private TextView mMyDynamicTextView;
    private TextView mDynamicTextView;
    private TextView mAccountSetTextView;
    private TextView mHelpTextView;
    private TextView mAccountManagerTextView;
    private TextView mCleanTextView;
    private Timer timer = new Timer();
    private TimerTask mTimerTask = null;
    private static final int MSG_WHAT_TIME_IS_UP = 1;//时间到了
    private static final int MSG_WHAT_TIME_IS_TICK = 2;//时间减少中

    private int mTimeCount = 0;

    private int mLabel = 1;

    private FrameLayout mCoverFrameLayout;

    public static MyInformationFragment newInstance() {
        return new MyInformationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_information_fragment_layout, container, false);
        mAccountNameTextView = (TextView) v.findViewById(R.id.my_information_name_text_view);
        mAccoutImageView = (ImageView) v.findViewById(R.id.my_information_image_view);
        mBasketImageView = (ImageView) v.findViewById(R.id.my_information_basket_image_view);
        mMyDynamicTextView = (TextView) v.findViewById(R.id.my_information_my_dynamic_text_view);
        mDynamicTextView = (TextView) v.findViewById(R.id.my_information_dynamic_text_view);
        mAccountSetTextView = (TextView) v.findViewById(R.id.my_information_account_set_text_view);
        mHelpTextView = (TextView) v.findViewById(R.id.my_information_help_text_view);
        mAccountManagerTextView = (TextView) v.findViewById(R.id.my_information_account_manage_text_view);
        mCleanTextView = (TextView) v.findViewById(R.id.my_information_clean_text_view);
        mCoverFrameLayout = (FrameLayout) v.findViewById(R.id.cover_linearlayout);

        mAccountManagerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                startActivity(i);
            }
        });

        mCleanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCoverFrameLayout.setVisibility(View.VISIBLE);
                if (mTimerTask == null) {
                    mTimeCount = 3;
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mTimeCount--;
                            handler.sendEmptyMessage(MSG_WHAT_TIME_IS_TICK);
                            if (mTimeCount <= 0) {   //时间到了就弹出对话框
                                handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                                stopTimer();
                            }
                        }
                    };
                    timer.schedule(mTimerTask, 1000, 1000);

                }
            }
        });


        mBasketImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BasketActivity.class);
                startActivity(i);
            }
        });
        mHelpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HelpActivity.class);
                startActivity(i);
            }
        });

        mAccoutImageView.setImageResource(R.drawable.test);

        mDynamicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DynamicActivity.class);
                startActivity(i);
            }
        });
        mMyDynamicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyDynamicActivity.class);
                startActivity(i);
            }
        });

        mAccountSetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), InformationChangeActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_TIME_IS_UP:
                    if (mLabel == 1) {
                        Random random = new Random();
                        int num = random.nextInt(50) % (50 - 10 + 1) + 10;
                        mLabel++;
                        mCoverFrameLayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "成功清理缓存空间，共清理"+num+"M缓存数据", Toast.LENGTH_LONG).show();
                    } else {
                        mCoverFrameLayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "缓存空间很整洁，不需要清理哦~~", Toast.LENGTH_LONG).show();
                    }
                    break;
                case MSG_WHAT_TIME_IS_TICK://显示动态时间

                    break;
            }
        }
    };

    private void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

}
