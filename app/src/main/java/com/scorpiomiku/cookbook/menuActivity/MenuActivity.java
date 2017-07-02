package com.scorpiomiku.cookbook.menuActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

public class MenuActivity extends AppCompatActivity {
    
    private String TAG = "MenuActivity" ;

    private ImageView mDishImageView;
    private TextView mDishNameTextView;
    private TextView mDishMaterialsTextView;
    private RecyclerView mStepsRecyclerView;
    private TextView mNutritionTextView;
    private ImageView mClockImageView;
    private TextView mLookAllTextView;

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
        fm = getSupportFragmentManager();
        initView();
        setListener();
    }

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
    }

    /*----------------------------------setListener--------------------------------*/
    private void setListener() {
        mLookAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(MenuRecyclerStepsFragment.newInstance());
            }
        });
    }

    /*--------------------------------------Back----------------------*/
    @Override
    public void onBackPressed() {
        if(isFragmentFooter()){
            changeFragment(DefaultFragment.newInstance());
        }else {
            super.onBackPressed();
        }
    }
}
