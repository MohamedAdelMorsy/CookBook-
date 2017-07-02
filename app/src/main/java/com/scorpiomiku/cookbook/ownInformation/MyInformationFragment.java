package com.scorpiomiku.cookbook.ownInformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.collection.MyCollectionFragment;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MyInformationFragment extends FragmentModule {

    private ImageView mAccoutImageView;
    private TextView mAccountNameTextView;


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
        return v;
    }
}
