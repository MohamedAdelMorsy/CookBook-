package com.scorpiomiku.cookbook.sign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;

public class ForgetActivity extends AppCompatActivity {

    private Button mForgetButton;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        mForgetButton = (Button) findViewById(R.id.forget_button);
        mEditText = (EditText) findViewById(R.id.forget_edit_text_view);
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgetActivity.this,"重置密码请求成功，请到" + mEditText.getText()
                        .toString() + "邮箱进行密码重置操作",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
