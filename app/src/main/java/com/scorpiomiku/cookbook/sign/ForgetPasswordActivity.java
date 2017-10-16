package com.scorpiomiku.cookbook.sign;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText forgetPasswordEmail;
    private Button forgetEnter;
   void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);
        forgetPasswordEmail = (EditText)findViewById(R.id.forget_email);
        forgetEnter  = (Button)findViewById(R.id.forget_enter);
        forgetEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = forgetPasswordEmail.getText().toString();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                        }else{
                           // toast("失败:" + e.getMessage());
                            Log.d("修改密码部分", "done:"+e.getMessage()+"|");
                            if(e.getMessage().equals("email Must be a valid email address")){
                                toast("无效邮箱");
                            }else{
                                toast("无此用户");
                            }
                        }
                    }
                });
            }
        });
    }
}
