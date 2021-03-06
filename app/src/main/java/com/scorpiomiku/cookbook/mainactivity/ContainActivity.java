package com.scorpiomiku.cookbook.mainactivity;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.collection.CollectionFragment;
import com.scorpiomiku.cookbook.combination.CombinationFragment;
import com.scorpiomiku.cookbook.ownInformation.MyInformationFragment;
import com.scorpiomiku.cookbook.recommend.RecommendDefultFragment;
import com.scorpiomiku.cookbook.recommend.RecommendFragment;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuFragment;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuItemClickFragment;
import com.scorpiomiku.cookbook.takephoto.TakePhotoMainFragment;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;


public class ContainActivity extends AppCompatActivity {

    private Toolbar mRecommendToolbar;
    private Toolbar mCombinationToolbar;
    private Fragment fr;
    private BottomNavigationView navigation;
    public static Toolbar mToolbar;
    private int mNo1Fragment;
    private int mNo2Fragment;
    private int mNo3Fragment;
    private int mNo4Fragment;
    private int mNo5Fragment;
    private static final String TAG = "ContainActivity";
    private FragmentManager fm = getSupportFragmentManager();



    private void howToCreateFragment(int nowFragment, int newFragment, Fragment f) {
        if (nowFragment < newFragment) {
            createFragmentRight(f);
        } else {
            createFragmentLeft(f);
        }
    }

    /*----------------------------------CreateFragment---------------------------*/
    private void createFragmentLeft(Fragment fragment) {
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        //Log.d(TAG, "createFragmentLeft: "+fragment.getClass().getName()+"    "+frag.getClass().getName());
        if (frag != null) {
            if (!(fragment.getClass().getName().equals(frag.getClass().getName()))) {
                frag = fragment;
                fm.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, frag)
                        .commit();
            }
        } else {
            frag = fragment;
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, frag)
                    .commit();
        }
    }
    private void createFragmentRight(Fragment fragment) {
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        if (frag != null) {
            if (!(fragment.getClass().getName().equals(frag.getClass().getName()))) {
                frag = fragment;
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left)
                        .replace(R.id.fragment_container, frag)
                        .commit();
            }
        }else{
            frag = fragment;
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left)
                    .replace(R.id.fragment_container, frag)
                    .commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recommend:
                    howToCreateFragment(navigation.getSelectedItemId(), mNo1Fragment,
                            RecommendFragment.newInstance());
                    return true;
                case R.id.navigation_combination:
                    howToCreateFragment(navigation.getSelectedItemId(), mNo2Fragment,
                            CombinationFragment.newInstance());
                    return true;
                case R.id.navigation_takephoto:
                    howToCreateFragment(navigation.getSelectedItemId(), mNo3Fragment,
                            TakePhotoMainFragment.newInstance());
                    return true;
                case R.id.navigation_collection:
                    howToCreateFragment(navigation.getSelectedItemId(), mNo4Fragment,
                            CollectionFragment.newInstance());
                    return true;
                case R.id.navigation_information:
                    howToCreateFragment(navigation.getSelectedItemId(), mNo5Fragment,
                            MyInformationFragment.newInstance());
                    return true;
            }
            return false;
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contain);
        Bmob.initialize(this, "accfd3a92dc224c9369613948c03c014");
        fm.beginTransaction()
                .add(R.id.fragment_container, TakePhotoMainFragment.newInstance())
                .commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mNo1Fragment = navigation.getSelectedItemId();
        mNo2Fragment = mNo1Fragment + 1;
        mNo3Fragment = mNo1Fragment + 2;
        mNo4Fragment = mNo1Fragment + 3;
        mNo5Fragment = mNo1Fragment + 4;
        navigation.setSelectedItemId(R.id.navigation_takephoto);
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        int[] colors = new int[]{ContextCompat.getColor(this, R.color.toolbar_and_menu_color),
                ContextCompat.getColor(this, R.color.testColor)
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        navigation.setItemTextColor(colorStateList);
        navigation.setItemIconTintList(colorStateList);

        //调用自动更新
        BmobUpdateAgent.initAppVersion();
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                // TODO Auto-generated method stub
                //根据updateStatus来判断更新是否成功
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (fm.findFragmentById(R.id.fragment_container).getClass().getName()
                .equals(RecommendFragment.class.getName())) {
            if (!fm.findFragmentById(R.id.recommend_container).getClass().getName()
                    .equals(RecommendDefultFragment.class.getName())) {
                Fragment mNowFragment = fm.findFragmentById(R.id.recommend_container);
                fm.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .hide(mNowFragment)
                        .add(R.id.recommend_container, RecommendDefultFragment.newInstance())
                        .commit();
            } else {
                super.onBackPressed();
            }
        } else {
            if (fm.findFragmentById(R.id.fragment_container).getClass().getName()
                    .equals(RecommendMenuFragment.class.getName())) {
                fm.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .replace(R.id.fragment_container, RecommendFragment.newInstance())
                        .commit();
            } else {
                if (fm.findFragmentById(R.id.fragment_container).getClass().getName()
                        .equals(RecommendMenuItemClickFragment.class.getName())) {
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .replace(R.id.fragment_container, RecommendMenuFragment.newInstance())
                            .commit();
                }else{
                    super.onBackPressed();
                }
            }
        }
    }
}
