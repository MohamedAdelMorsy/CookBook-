package com.scorpiomiku.cookbook.sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.Person;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.LogInListener;

public class SignInActivity extends AppCompatActivity {
    private TextView mSignUpTextView;
    private EditText mSignInEmail;
    private EditText mSignInPassword;
    private Button mLogin;
    private TextView foreget;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
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
        mSignUpTextView = (TextView) findViewById(R.id.sign_in_sign_up_text_view);
        //新用户注册
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        Bmob.initialize(this, "accfd3a92dc224c9369613948c03c014");
        Log.d("登录界面", "onCreate: "+"bmob载入完成");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        foreget=(TextView)findViewById(R.id.foregetpassword);
        mSignInEmail=(EditText)findViewById(R.id.id_sign_in_email);
        mSignInPassword=(EditText)findViewById(R.id.id_sign_in_password);
        mLogin=(Button)findViewById(R.id.id_sign_in_loginin);
        mSignUpTextView = (TextView) findViewById(R.id.sign_in_sign_up_text_view);
        Log.d("登录界面", "onCreate: "+"各设置载入完成");
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        foreget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String useremail = mSignInEmail.getText().toString();
                final String userpassword = mSignInPassword.getText().toString();
                BmobUser.loginByAccount(useremail, userpassword, new LogInListener<Person>() {
                    @Override
                    public void done(Person user, BmobException e) {
                        if (user == null) {
                            Toast.makeText(SignInActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(SignInActivity.this, "请检查您的邮箱和密码是否有误", Toast.LENGTH_LONG).show();
                        }
                        if (user != null) {
                            int h = 0;
                            h = h + 1;
                            try {
                                boolean s = user.getEmailVerified();
                                if (s == true) {
                                    h = h + 1;
                                    // Toast.makeText(upSorce.this, "信息为：" + user, Toast.LENGTH_LONG).show();
                                }
                                if (s == false) {
                                    toast("请在你的邮箱中认证");
                                    h = h + 1;
                                }
                                if (2 == h) {
                                    editor = pref.edit();
                                    Log.d("登录界面", "done: "+user.getSculpture().toString());
                                    Log.d("登录界面", "done: "+ user.getEmail()+"   "+user.getName()+"   "+user.getHome());
                                    editor.putString("useremail", user.getEmail());
                                    editor.putString("user_objId", user.getObjectId());
                                    editor.putString("username", user.getName());
                                    editor.putString("sex", user.getSex());
                                    editor.putString("birthday", user.getBirthday());
                                    editor.putString("home", user.getHome());
                                    JSONObject jsonObj = new JSONObject();
                                    try {
                                        jsonObj.put("username", useremail);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                                    ace.callEndpoint("yx", jsonObj, new CloudCodeListener() {
                                        public static final String TAG = "thing";
                                        @Override
                                        public void done(Object object, BmobException e) {
                                            if (e == null) {
                                                String result =object.toString();
                                                JsonParser parser = new JsonParser();  //创建JSON解析器
                                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                                JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                                                for (int i = 0; i < array.size(); i++) {
                                                    editor = pref.edit();
                                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                                    JsonObject Sculpture = subObject.get("Sculpture").getAsJsonObject();
                                                    String url_r = Sculpture.get("url").getAsString();
                                                    Log.d("登录界面", "done:url "+url_r);
                                                    editor.putString("url_r", url_r);
                                                    editor.apply();
                                                }
                                            } else {
                                                //toast(e.getMessage());
                                                Log.e(TAG, " " + e.getMessage());
                                            }//
                                        }
                                    });
                                    //editor.putString("url",user.getSculpture().toString());
                                    editor.apply();
                                    toast("登录成功");
                                    finish();
                                }
                            } catch (Exception k) {
                                toast("请在你的邮箱中认证");
                            }
                        }
                    }
                });
            }
        });
    }
}
