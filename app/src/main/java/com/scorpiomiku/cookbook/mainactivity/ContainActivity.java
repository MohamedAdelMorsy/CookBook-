package com.scorpiomiku.cookbook.mainactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.combination.CombinationFragment;
import com.scorpiomiku.cookbook.recommend.RecommendFragment;

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
                case R.id.navigation_Combination:
                    createFragment(CombinationFragment.newInstance());
                    return true;
                case R.id.navigation_takephoto:

                    return true;
                case R.id.navigation_collection:

                    return true;
                case R.id.navigation_information:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contain);
        fm.beginTransaction()
                .add(R.id.fragment_container, RecommendFragment.newInstance())
                .commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
