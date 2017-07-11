package com.scorpiomiku.cookbook.mainactivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.collection.CollectionFragment;
import com.scorpiomiku.cookbook.combination.CombinationFragment;
import com.scorpiomiku.cookbook.ownInformation.MyInformationFragment;
import com.scorpiomiku.cookbook.recommend.RecommendDefultFragment;
import com.scorpiomiku.cookbook.recommend.RecommendFragment;
import com.scorpiomiku.cookbook.takephoto.TakePhotoMainFragment;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ContainActivity extends AppCompatActivity {

    private Toolbar mRecommendToolbar;
    private Toolbar mCombinationToolbar;
    private Fragment fr;

    public static Toolbar mToolbar;

    private static final String TAG = "ContainActivity";
    private FragmentManager fm = getSupportFragmentManager();


    /*----------------------------------CreateFragment---------------------------*/
    private void createFragment(Fragment fragment) {
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment == frag) || frag == null) {
            frag = fragment;
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
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
                    createFragment(RecommendFragment.newInstance());
                    return true;
                case R.id.navigation_combination:
                    createFragment(CombinationFragment.newInstance());
                    return true;
                case R.id.navigation_takephoto:
                    createFragment(TakePhotoMainFragment.newInstance());
                    return true;
                case R.id.navigation_collection:
                    createFragment(CollectionFragment.newInstance());
                    return true;
                case R.id.navigation_information:
                    createFragment(MyInformationFragment.newInstance());
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
        fm.beginTransaction()
                .add(R.id.fragment_container, TakePhotoMainFragment.newInstance())
                .commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_takephoto);
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        int[] colors = new int[]{getResources().getColor(R.color.toolbar_and_menu_color, getTheme()),
                getResources().getColor(R.color.testColor, getTheme())
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        navigation.setItemTextColor(colorStateList);
        navigation.setItemIconTintList(colorStateList);
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
            }
        }else{
            super.onBackPressed();
        }
    }
}
