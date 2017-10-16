package com.scorpiomiku.cookbook.ownInformation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.content.ContentValues.TAG;

public class InformationChangeActivity extends AppCompatActivity {
    //账号信息设置
    private String imagepath;
    private ImageView mAccountImageView;
    private EditText mNameEditText;
    private EditText mAccountEditText;
    private EditText mSexEditText;
    private EditText mBirthdayEditText;
    private EditText mPlaceEditText;
    private Button mChangeButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean isChanging = false;
    private String url;
    private boolean panduan = false;
    private boolean mAccountImagepanduan = false;
    private Person per= new Person();
    private Context mContext;
    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
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
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        Bmob.initialize(this, "3bfd53d40a453ea66ce653ab658582d1");
        mAccountImageView.setImageResource(R.drawable.test);
       /* String uN =pref.getString("username","");
        NickName.setText(uN);
        email.setText(pref.getString("email",""));
        sex.setText(pref.getString("sex","未设置"));
        birthday.setText(pref.getString("birthday","未设置"));
        home.setText(pref.getString("home","未设置"));*/
        mContext = this;
        mNameEditText.setText(pref.getString("username", "您未登录"));
        mAccountEditText.setText(pref.getString("useremail", "您未登录"));
        mSexEditText.setText(pref.getString("sex", "未设置"));
        mBirthdayEditText.setText(pref.getString("birthday", "未设置"));
        mPlaceEditText.setText(pref.getString("home", "未设置"));
        SetSculpture();
        Log.d("资料修改界面", "onCreate: SetSculpture()完成");
        mAccountImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountImagepanduan=true;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 111);
                isChanging=true;
                panduan = true;
                mChangeButton.setText("确认并保存修改");
            }
        });
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
                    if(panduan){
                        if(mAccountImagepanduan==true){
                            final String birthday = mBirthdayEditText.getText().toString();
                            final String Ssex = mSexEditText.getText().toString();
                            final String Shome= mPlaceEditText.getText().toString();
                            per.setHome(Shome);
                            final String SNname = mNameEditText.getText().toString();
                            per.setName(SNname);
                            per.setSex(Ssex);
                            per.setBirthday(birthday);
                            //错误产生处
                            final BmobFile icon = new BmobFile(new File(imagepath));
                            icon.uploadblock(new UploadFileListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        per.setSculpture(icon);
                                        per.update(pref.getString("user_objId", "0874b39eb9"), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e==null){
                                                    toast("修改成功");
                                                    editor.putString("home",Shome);
                                                    editor.putString("username",SNname);
                                                    editor.putString("sex",Ssex);
                                                    editor.putString("birthday",birthday);
                                                    JSONObject jsonObj = new JSONObject();
                                                    try {
                                                        jsonObj.put("username", pref.getString("useremail","点击登录"));
                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                                                    Log.e("thing","检查点2");
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
                                                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                                        /*
                                                        * {"results":[{"Sculpture":
                                                        * {"__type":"File",
                                                        * "filename":"Screenshot_2017-07-13-12-56-51.png",
                                                        * "group":"",
                                                        * "url":"http://bmob-cdn-12102.b0.upaiyun.com/2017/07/14/e7daa839f64c4d418b42107f0c2e24ff.png"},"birthday":"1997年1月1日","createdAt":"2017-03-19 14:50:24","email":"1286023426@qq.com","emailVerified":true,
                                                        * "home":"大大","name":"fyc","objectId":"8064882518","sex":"男","updatedAt":"2017-07-14 10:36:49","username":"fyc"}]}
                                                        *
                                                        * */
                                                                    JsonObject tx = subObject.get("Sculpture").getAsJsonObject();
                                                                    String url = tx.get("url").getAsString();
                                                                    editor.putString("url",url);
                                                                    editor.apply();
                                                                    SetSculpture();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            mAccountImagepanduan=false;
                        }else {
                            final String birthday = mBirthdayEditText.getText().toString();
                            final String Ssex = mSexEditText.getText().toString();
                            final String Shome= mPlaceEditText.getText().toString();
                            per.setHome(Shome);
                            final String SNname = mNameEditText.getText().toString();
                            per.setName(SNname);
                            per.setSex(Ssex);
                            per.setBirthday(birthday);
                            //错误产生处
                            per.update(pref.getString("user_objId", ""), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        toast("修改成功");
                                        editor.putString("home", Shome);
                                        editor.putString("username", SNname);
                                        editor.putString("sex", Ssex);
                                        editor.putString("birthday", birthday);
                                        editor.apply();
                                    }
                                }
                            });
                        }
                    }
                    SetSculpture();
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
                    panduan = true;
                    isChanging = true;
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 标记点1");
        //这里只进行了一个图片显示
        if (requestCode == 111 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            Log.d(TAG, "onActivityResult: 标记点2");
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            imagepath = picturePath;
            Log.d(TAG, "onActivityResult: 标记点3");
            ImageView imageView = (ImageView) findViewById(R.id.change_information_account_image_view);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            cursor.close();
            /**/
        }
    }
    private void SetSculpture(){
        Log.d("资料修改界面", "函数SetSculpture: 启动");

        url=pref.getString("url_r","http://img3.duitang.com/uploads/item/201602/20/20160220011711_4kzYy.jpeg");
        Log.d("资料修改界面", "函数SetSculpture: url_r"+url);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.test);
        Log.d("资料修改界面", "onCreate: SetSculpture()开始加载图片");
        Glide.with(mContext)
                .load(url)
                .thumbnail( 0.2f )
                .apply(options)
                .into(mAccountImageView);
        Log.d("资料修改界面", "onCreate: SetSculpture()放入图片");
    }
}