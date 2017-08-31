package com.scorpiomiku.cookbook.sign;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.User;
import com.scorpiomiku.cookbook.sql.BasketDataHelper;
import com.scorpiomiku.cookbook.sql.UserDataHelper;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class SignInActivity extends AppCompatActivity {
    private TextView mSignUpTextView;

    private EditText mEmailEditText;
    private EditText mPassWordEditText;


    private Button mOkButton;


    private CheckBox mRememberCheckBox;
    private TextView mForgetTextView;

    private String mEmail;
    private String mPassWord;
    private boolean isChecked;


    private UserDataHelper mUserDataHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignUpTextView = (TextView) findViewById(R.id.sign_in_sign_up_text_view);
        mEmailEditText = (EditText) findViewById(R.id.sign_in_email_edit_text);
        mPassWordEditText = (EditText) findViewById(R.id.sign_in_pass_word_edit_text);
        mOkButton = (Button) findViewById(R.id.sign_in_ok_button);
        mRememberCheckBox = (CheckBox) findViewById(R.id.sign_in_remember_checkbox);
        mForgetTextView = (TextView) findViewById(R.id.sign_in_forget_text_view);
        mUserDataHelper = new UserDataHelper(this, "UserStore.db", null, 1);
        init();
    }

    private void init() {
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                mPassWord = mPassWordEditText.getText().toString();
                if (mEmail != null) {
                    if (mPassWord != null) {
                        /*BmobUser.loginByAccount(mEmail, mPassWord, new LogInListener<User>() {
                            int h = 0;
                            @Override
                            public void done(User user, BmobException e) {
                                if (user == null) {
                                    toast("请检查您的邮箱和密码是否有误");
                                }
                                if (user != null) {
                                    h = h + 1;
                                    try {
                                        boolean s = user.getEmailVerified();
                                        if (s == true) {
                                            toast("1");
                                            h = h + 1;
                                           toast("信息为："+ user);
                                        }
                                        if (s == false) {
                                            toast("请在你的邮箱中认证");
                                        }
                                        if (2 == h) {
                                            if (isChecked) {
                                                translation = "queren";
                                            }
                                            Intent intent = new Intent(MainActivity.this, Activity_1.class);
                                            intent.putExtra("email", useremail);
                                            intent.putExtra("password", userpassword);
                                            intent.putExtra("shujv", translation);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (Exception k) {
                                        toast("请在你的邮箱中认证");
                                    }
                                }
                            }
                        });*/
                        final String useremail = mEmailEditText.getText().toString();
                        final String userpassword = mPassWordEditText.getText().toString();
                        BmobUser.loginByAccount(useremail, userpassword, new LogInListener<User>() {
                            int h = 0;

                            @Override
                            public void done(User user, BmobException e) {
                                if (user == null) {
                                    Toast.makeText(SignInActivity.this, "请检查您的邮箱和密码是否有误", Toast.LENGTH_LONG).show();
                                }
                                if (user != null) {
                                    h = h + 1;
                                    try {
                                        boolean s = user.getEmailVerified();
                                        if (s == true) {
                                            h = h + 1;
                                            // Toast.makeText(upSorce.this, "信息为：" + user, Toast.LENGTH_LONG).show();
                                        }
                                        if (s == false) {
                                            toast("请在你的邮箱中认证");
                                        }
                                        if (2 == h) {
                                            if (mRememberCheckBox.isChecked()) {
                                                SQLiteDatabase db = mUserDataHelper.getWritableDatabase();
                                                ContentValues values = new ContentValues();
                                                values.put("email",useremail);
                                                values.put("passwords",userpassword);
                                                db.insert("User",null,values);
                                                values.clear();
                                            }
                                            finish();
                                        }
                                    } catch (Exception k) {
                                        toast("请在你的邮箱中认证");
                                    }
                                }
                            }
                        });
                    } else {
                        toast("密码不能为空");
                    }
                } else {
                    toast("邮箱账号不能为空");
                }
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(SignInActivity.this, s, Toast.LENGTH_LONG).show();
    }
}
