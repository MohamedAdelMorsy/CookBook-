package com.scorpiomiku.cookbook.ownInformation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.scorpiomiku.cookbook.R;

public class InformationChangeActivity extends AppCompatActivity {

    private ImageView mAccountImageView;
    private EditText mNameEditText;
    private EditText mAccountEditText;
    private EditText mSexEditText;
    private EditText mBirthdayEditText;
    private EditText mPlaceEditText;
    private Button mChangeButton;

    private boolean isChanging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_change);
        mAccountEditText = (EditText) findViewById(R.id.change_information_account_edit_text);
        mAccountImageView = (ImageView) findViewById(R.id.change_information_account_image_view);
        mNameEditText = (EditText) findViewById(R.id.change_information_name_edit_text);
        mSexEditText = (EditText) findViewById(R.id.change_information_sex_edit_text);
        mBirthdayEditText = (EditText) findViewById(R.id.change_information_birthday_edit_text);
        mPlaceEditText = (EditText) findViewById(R.id.change_information_place_edit_text);
        mChangeButton = (Button) findViewById(R.id.change_information_change_button);

        mAccountImageView.setImageResource(R.drawable.test);
        mNameEditText.setText("ScorpioMiku");
        mAccountEditText.setText("1056992492");
        mSexEditText.setText("男");
        mBirthdayEditText.setText("1997.11.1");
        mPlaceEditText.setText("四川");

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChanging) {
                    mChangeButton.setText("修改");
                    mNameEditText.setEnabled(false);
                    mSexEditText.setEnabled(false);
                    mBirthdayEditText.setEnabled(false);
                    mPlaceEditText.setEnabled(false);
                    isChanging = false;
                } else {
                    mChangeButton.setText("确认并保存修改");
//                    mNameEditText.setFocusable(true);
//                    mNameEditText.setEnabled(true);
//                    mNameEditText.setFocusableInTouchMode(true);
                    mNameEditText.setEnabled(true);
//                    mSexEditText.setFocusable(true);
//                    mSexEditText.setEnabled(true);
//                    mSexEditText.setFocusableInTouchMode(true);
                    mSexEditText.setEnabled(true);

//                    mBirthdayEditText.setFocusable(true);
//                    mBirthdayEditText.setEnabled(true);
//                    mBirthdayEditText.setFocusableInTouchMode(true);
                    mBirthdayEditText.setEnabled(true);

//                    mPlaceEditText.setFocusable(true);
//                    mPlaceEditText.setEnabled(true);
//                    mPlaceEditText.setFocusableInTouchMode(true);
                    mPlaceEditText.setEnabled(true);

                    isChanging = true;
                }
            }
        });
    }
}
