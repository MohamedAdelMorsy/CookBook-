package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.scorpiomiku.cookbook.R;

public class ClassifierResultActivity extends AppCompatActivity {
    private String mPicturePath;
    private String mPictureResult;

    private static final String TAG = "ClassifierResultActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier_result);
        Intent i = getIntent();
        mPicturePath = i.getStringExtra("picturePath");
        mPictureResult = i.getStringExtra("pictureResult");
        Log.d(TAG, "onCreate: "+mPicturePath+"    "+mPictureResult);
    }
}
