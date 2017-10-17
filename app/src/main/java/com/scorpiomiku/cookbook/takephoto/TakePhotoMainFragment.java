package com.scorpiomiku.cookbook.takephoto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
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
    private static final int REQUEST_CAMERA = 0;
    private Button mTakePhotoButton;
    private boolean canTakePhoto;
    private static final String DIALOG_BUTTON = "ButtonDialog";
    private Intent mTakePhotoIntent;

    public static TakePhotoMainFragment newInstance() {
        return new TakePhotoMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhotoIntent = new Intent(getActivity(), CameraActivity.class);
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
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
                } else {
                    FragmentManager fm = getFragmentManager();
                    TakePhotoDialogFragment dialogFragment = TakePhotoDialogFragment.newInstance();
                    dialogFragment.show(fm, DIALOG_BUTTON);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(mTakePhotoIntent);
            }
        }
    }
}
