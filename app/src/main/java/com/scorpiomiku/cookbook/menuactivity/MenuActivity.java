package com.scorpiomiku.cookbook.menuactivity;

import android.app.AlertDialog;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.Clock;
import com.scorpiomiku.cookbook.bean.Sound;

import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    private String TAG = "MenuActivity";
    private Clock mClock;

    private ImageView mDishImageView;
    private TextView mDishNameTextView;
    private TextView mDishMaterialsTextView;
    private RecyclerView mStepsRecyclerView;
    private TextView mNutritionTextView;
    private ImageView mClockImageView;
    private TextView mLookAllTextView;

    private int mTimeCount = 0;
    private Timer timer = new Timer();
    private TimerTask mTimerTask = null;

    private static final int MSG_WHAT_TIME_IS_UP = 1;//时间到了
    private static final int MSG_WHAT_TIME_IS_TICK = 2;//时间减少中


    private Sound mSound;

    private FragmentManager fm;


    /*---------------------------changeFragment--------------------------*/

    private void changeFragment(Fragment fragment) {
        Fragment fragment1 = fm.findFragmentById(R.id.menu_activity_container);
        if (!fragment1.getClass().getName().equals(fragment.getClass().getName())) {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .hide(fragment1)
                    .add(R.id.menu_activity_container, fragment)
                    .commit();
        }
    }

    /*--------------------------isFragmentFooter------------------------*/
    private boolean isFragmentFooter() {
        Fragment f = fm.findFragmentById(R.id.menu_activity_container);
        if (f.getClass().getName().equals(MenuRecyclerStepsFragment.class.getName())) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_layout);
        mDishImageView = (ImageView) findViewById(R.id.menu_fragment_image_view_dish_images);
        mDishNameTextView = (TextView) findViewById(R.id.menu_fragment_text_view_dish_name);
        mDishMaterialsTextView = (TextView) findViewById(R.id.menu_fragment_text_view_dish_materials);
        mLookAllTextView = (TextView) findViewById(R.id.menu_activity_lookall_text_view);
        mClockImageView = (ImageView) findViewById(R.id.menu_alarm_clock_image_view);
        mClock = new Clock(MenuActivity.this);
        mSound = mClock.getSounds().get(0);
        fm = getSupportFragmentManager();
        initView();
        setListener();
    }
    /*----------------------------------Clorck----------------------------------*/


    private void startTimer() {
        if (mTimerTask == null) {
            mTimeCount = 5;
            Toast.makeText(MenuActivity.this, "测试时间为5秒", Toast.LENGTH_SHORT).show();
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

    private void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_TIME_IS_UP:
                    mClock.play(mSound);
                    Toast.makeText(MenuActivity.this,"时间到了",Toast.LENGTH_LONG).show();
                    break;
                case MSG_WHAT_TIME_IS_TICK://显示动态时间

                    break;
            }
        }
    };

    /*---------------------------------initView-------------------------------*/
    private void initView() {
        mDishImageView.setImageResource(R.drawable.test);
        mDishNameTextView.setText("菜名");
        mDishMaterialsTextView.setText("菜材料");
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .add(R.id.menu_activity_container, DefaultFragment.newInstance())
                .commit();
        mClockImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    /*----------------------------------setListener--------------------------------*/
    private void setListener() {
        mLookAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFragmentFooter()) {
                    changeFragment(DefaultFragment.newInstance());
                } else {
                    changeFragment(MenuRecyclerStepsFragment.newInstance());
                }
            }
        });

    }

    /*--------------------------------------Back----------------------*/
    @Override
    public void onBackPressed() {
        if (isFragmentFooter()) {
            changeFragment(DefaultFragment.newInstance());
        } else {
            super.onBackPressed();
        }
    }
}
