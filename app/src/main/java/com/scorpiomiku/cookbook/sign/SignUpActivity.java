package com.scorpiomiku.cookbook.sign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPassWordEditText;
    private EditText mPassWordCheckEditText;
    private Button mOkButton;


    private String mName;

    private String mPassWord;
    private String mPassWordCheck;


    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mNameEditText = (EditText) findViewById(R.id.sign_up_user_name_edit_text);
        mPassWordEditText = (EditText) findViewById(R.id.sign_up_pass_word_edit_text);
        mPassWordCheckEditText = (EditText) findViewById(R.id.sign_up_pass_word_check_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.sign_up_account_edit_text);

        mOkButton = (Button) findViewById(R.id.sign_up_ok_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = mNameEditText.getText().toString();
                mPassWord = mPassWordEditText.getText().toString();
                mPassWordCheck = mPassWordCheckEditText.getText().toString();

                final String mEmail= mEmailEditText.getText().toString();
                if (mPassWord.equals(mPassWordCheck)) {

                    Log.d(TAG, "done: ok1");

                    BmobUser mUser = new BmobUser();
                    mUser.setEmail(mEmail);
                    mUser.setPassword(mPassWord);
                    mUser.setUsername(mName);

                    mUser.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            String s = "errorCode:203,errorMsg:email '" + mEmail + "' already taken.";
                            //Toast.makeText(Register.this,"返回信息为:" +e.toString(),Toast.LENGTH_SHORT).show();
                            if (s.equals(e.toString())) {
                                toast("邮箱已经被注册过了");
                            } else if (e.toString().equals("errorCode:304,errorMsg:username or password is null.")) {
                                toast("邮箱不能为空");
                            } else if (e.toString().equals("errorCode:304,errorMsg:username or password is null.")) {
                                toast("用户名或密码不能为空");
                            } else if (e.toString().equals("errorCode:301,errorMsg:email Must be a valid email address")) {
                                toast("请正确填写邮箱");
                            } else {
                                BmobUser.requestEmailVerify(mEmail, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            toast("请求验证邮件成功，请到" + mEmail + "邮箱中进行激活。");
                                            finish();
                                        } else {
                                            toast("失败:" + e.getMessage());
                                            Log.d("faile", e.toString());
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    toast("两次输入密码不一致");
                }
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
    }
}
