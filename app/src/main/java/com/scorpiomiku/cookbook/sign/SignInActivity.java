package com.scorpiomiku.cookbook.sign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

public class SignInActivity extends AppCompatActivity {
    private TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignUpTextView = (TextView) findViewById(R.id.sign_in_sign_up_text_view);

    }
}
