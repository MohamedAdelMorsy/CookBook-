package com.scorpiomiku.cookbook.sign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.Person;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SignUpActivity extends AppCompatActivity {
    private Button register;
    private Button back;
    private EditText accountNumber;
    private EditText password;
    private EditText emailAdrss;

    private ImageView reg_TouXiang;
    private String imagepath;
    void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        register = (Button)findViewById(R.id.register_send);
        accountNumber = (EditText)findViewById(R.id.register_username);
        password = (EditText)findViewById(R.id.register_passwod);
        emailAdrss = (EditText)findViewById(R.id.register_email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("注册界面", "done: 返回");
                String username = accountNumber.getText().toString();
                String userpassword = password.getText().toString();
                final String useremail = emailAdrss.getText().toString();
                Person bu = new Person();
                bu.setUsername(username);
                bu.setPassword(userpassword);
                bu.setEmail(useremail);
                bu.setSex("未填写");
                bu.setBirthday("未填写");
                bu.setHome("未填写");
                //注意：不能用save方法进行注册
                bu.signUp(new SaveListener<Person>() {
                    @Override
                    public void done(Person person, BmobException e) {
                        Log.i("look", "i+"+e.toString());
                        String s ="errorCode:203,errorMsg:email '"+useremail+"' already taken.";
                        //Toast.makeText(Register.this,"返回信息为:" +e.toString(),Toast.LENGTH_SHORT).show();
                        if(s.equals(e.toString())){
                            toast("邮箱已经被注册过了");
                        }else if (e.toString().equals("errorCode:304,errorMsg:username or password is null.")){
                            toast("邮箱不能为空");
                        }
                        else if (e.toString().equals("errorCode:304,errorMsg:username or password is null.")){
                            toast("用户名或密码不能为空");
                        }
                        else if (e.toString().equals("errorCode:301,errorMsg:email Must be a valid email address")){
                            toast("请正确填写邮箱");
                        }
                        else {
                            BmobUser.requestEmailVerify(useremail, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        toast("请求验证邮件成功，请到" + useremail + "邮箱中进行激活。");
                                        JSONObject jsonObj = new JSONObject();
                                        try {
                                            jsonObj.put("username", useremail);
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                                        Log.e("thing","检查点2");
                                    }else{
                                        toast("失败:" + e.getMessage());
                                        Log.d("faile", e.toString());
                                    }
                                }
                            });
                        }

                    }

                });
            }
        });
    }
}
