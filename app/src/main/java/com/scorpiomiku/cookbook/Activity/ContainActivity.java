package com.scorpiomiku.cookbook.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.Recommend.RecommendFragment;

public class ContainActivity extends AppCompatActivity {

    private Toolbar mRecommendToolbar;
    private Toolbar mCombinationToolbar;

    private static final String TAG = "ContainActivity";
    private FragmentManager fm = getSupportFragmentManager();

    /*----------------------------------CreateFragment---------------------------*/
    private void createFagment(Fragment fragment) {
        Fragment fr = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment == fr) || fr == null) {
            fr = fragment;
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fr)
                    .commit();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recommend:
                    createFagment(RecommendFragment.newInstance());
                    setSupportActionBar(mRecommendToolbar);
                    return true;
                case R.id.navigation_Combination:
                    setSupportActionBar(mCombinationToolbar);
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
        mRecommendToolbar = (Toolbar) findViewById(R.id.recommend_tool_bar);
        mCombinationToolbar = (Toolbar) findViewById(R.id.combination_tool_bar);

        createFagment(RecommendFragment.newInstance());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
