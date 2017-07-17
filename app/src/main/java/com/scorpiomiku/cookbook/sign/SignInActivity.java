package com.scorpiomiku.cookbook.sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

public class SignInActivity extends AppCompatActivity {
    private TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignUpTextView = (TextView) findViewById(R.id.sign_in_sign_up_text_view);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
