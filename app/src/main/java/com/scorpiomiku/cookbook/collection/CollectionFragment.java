package com.scorpiomiku.cookbook.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/6/14.
 */

public class CollectionFragment extends FragmentModule {

    private ImageButton mCollectionImageButton;
    private ImageButton mRecordsImageButton;
    private FragmentManager mFragmentManager ;

    public static CollectionFragment newInstance(){
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.collection_container,MyCollectionFragment.newInstance())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.collection_main_fragment_layout,container,false);
        Toolbar toolbar = (Toolbar)v.findViewById(R.id.collection_tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        mCollectionImageButton = (ImageButton) v.findViewById(R.id
                .collection_mycolletion_image_button);
        mRecordsImageButton = (ImageButton) v.findViewById(R.id
                .collection_myrecords_image_button);
        setClickListener();
        return v ;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recommend_menu, menu);
    }

    /*------------------------------------------------clickListener---------------------------*/
    private void setClickListener(){
        mCollectionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(MyCollectionFragment.newInstance());
            }
        });

        mRecordsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(MyRecordsFragment.newInstance());
            }
        });
    }

    /*-----------------------------------changeFragment--------------------------*/
    private void changeFragment(Fragment fragment){
        Fragment nowFragment = mFragmentManager.findFragmentById(R.id.collection_container);
        if(nowFragment.getClass().getName().equals(fragment.getClass().getName())){

        }else{
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .hide(nowFragment)
                    .add(R.id.collection_container, fragment)
                    .commit();
        }
    }
}
