package com.scorpiomiku.cookbook.takephoto;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
import com.scorpiomiku.cookbook.tensorflow.Classifier;

/**
 * Created by Administrator on 2017/10/17.
 */

public class TakePhotoDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {

    private ImageButton mSingleButton;
    private ImageButton mMoreButton;

    public static TakePhotoDialogFragment newInstance() {
        return new TakePhotoDialogFragment();
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();
        switch (Id) {
            case R.id.button_take_photo_single:
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
                CameraActivity.mPictureResult = "";
                break;
            case R.id.button_take_photo_more:

                Intent intent = new Intent(getActivity(), MoreCameraActivity.class);
                startActivity(intent);
                MoreCameraActivity.moreResults.clear();
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);
        mSingleButton = (ImageButton) v.findViewById(R.id.button_take_photo_single);
        mMoreButton = (ImageButton) v.findViewById(R.id.button_take_photo_more);
        mSingleButton.setOnClickListener(this);
        mMoreButton.setOnClickListener(this);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }


}
