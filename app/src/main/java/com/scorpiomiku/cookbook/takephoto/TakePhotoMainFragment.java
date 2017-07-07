package com.scorpiomiku.cookbook.takephoto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TakePhotoMainFragment extends FragmentModule {

    private Button mTakePhotoButton;
    private boolean canTakePhoto;
    private Intent mTakePhotoIntent;

    public static TakePhotoMainFragment newInstance() {
        return new TakePhotoMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhotoIntent = new Intent(getActivity(),CameraActivity.class);
        canTakePhoto = mTakePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.takephoto_fragment_layout, container, false);
        mTakePhotoButton = (Button) v.findViewById(R.id.takephoto_take_photo_button);
        mTakePhotoButton.setEnabled(canTakePhoto);
        setListener();
        return v;
    }

    /*---------------------------setListener-----------------------*/
    private void setListener() {
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(mTakePhotoIntent);
            }
        });
    }
}
