package com.scorpiomiku.cookbook.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/6/14.
 */

public class CollectionFragment extends FragmentModule {

    private LinearLayout mCollectionLinearLayout;
    private LinearLayout mRecordsLinearLayout;

    private ImageView mCollectionImage;
    private ImageView mRecordImage;
    private TextView mCollectionTextView;
    private TextView mRecordTextView;

    private FragmentManager mFragmentManager;


    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.collection_container, MyCollectionFragment.newInstance())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.collection_main_fragment_layout, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.collection_tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mCollectionLinearLayout = (LinearLayout) v.findViewById(R.id
                .collection_mycolletion);
        mRecordsLinearLayout = (LinearLayout) v.findViewById(R.id
                .collection_myrecords);
        mCollectionImage = (ImageView) v.findViewById(R.id.collection_collection_iamge_view);
        mCollectionTextView = (TextView) v.findViewById(R.id.collection_collection_text_view);
        mRecordImage = (ImageView) v.findViewById(R.id.collection_record_iamge_view);
        mRecordTextView = (TextView) v.findViewById(R.id.collection_record_text_view);
        setClickListener();
        return v;
    }

    /*------------------------------------changeViewColor---------------------------*/
    private void changeViewColorToCollection() {
        mCollectionImage.setImageResource(R.drawable.collection_image_red);
        mRecordImage.setImageResource(R.drawable.collection_record_image_white);
        mCollectionTextView.setTextColor(getResources().getColor(R.color.testColor));
        mRecordTextView.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void changeViewColorToRecord() {
        mCollectionImage.setImageResource(R.drawable.collection_image_white);
        mRecordImage.setImageResource(R.drawable.collection_record_image_red);
        mCollectionTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        mRecordTextView.setTextColor(getResources().getColor(R.color.testColor));
    }

    /*------------------------------------------------clickListener---------------------------*/
    private void setClickListener() {
        mCollectionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(MyCollectionFragment.newInstance());
                changeViewColorToCollection();
            }
        });

        mRecordsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(MyRecordsFragment.newInstance());
                changeViewColorToRecord();
            }
        });
    }

    /*-----------------------------------changeFragment--------------------------*/
    private void changeFragment(Fragment fragment) {
        Fragment nowFragment = mFragmentManager.findFragmentById(R.id.collection_container);
        if (nowFragment.getClass().getName().equals(fragment.getClass().getName())) {

        } else {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .hide(nowFragment)
                    .add(R.id.collection_container, fragment)
                    .commit();
        }
    }
}
