package com.scorpiomiku.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.bean.Person;
import com.scorpiomiku.cookbook.bean.Post;
import com.scorpiomiku.cookbook.menuactivity.DefaultFragment;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.APPKEY;

public class ShareActivity extends AppCompatActivity {
    private String user_objId;
    private String WayObjId;
    private List<CBLEC> mCookBookList = new ArrayList<>();
    private EditText inEdit;
    private Button right_S;
    private ImageView imageView;
    private TextView Shicai;
    private String ZuoFaTu;
    private boolean panduan;
    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Bmob.initialize(this, "accfd3a92dc224c9369613948c03c014");
        try{
            Intent intent =getIntent();
            user_objId=intent.getStringExtra("user_objId");
            WayObjId=intent.getStringExtra("WayObjId");
            //intent.putExtra("user_objId",user_objId);
        }catch (Exception e){
        }
        Log.d("转发界面", "onCreate: ");
        imageView=(ImageView)findViewById(R.id.zf_image);
        Shicai = (TextView)findViewById(R.id.zf_shicai);
        inEdit = (EditText)findViewById(R.id.share_Et);
        right_S = (Button)findViewById(R.id.right_share);

        Log.d("转发界面", "onCreate  WayObjId: " +WayObjId);
        urlget();
        Log.d("转发界面", "onCreate:initway完成");
        right_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_LS=inEdit.getText().toString();
                Person user = BmobUser.getCurrentUser(Person.class);
                // 创建帖子信息
                Post post = new Post();
                post.setObjID(WayObjId);
                post.setContent(str_LS);
                //添加一对一关联
                post.setAuthor(user);
                post.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
                            toast("成功" );
                            Log.i("bmob","保存成功");
                        }else{
                            Log.i("bmob","测试点 ：保存失败："+e.getMessage());
                            toast("失败"+"信息为"+e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void urlget(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;

                url = "http://apis.juhe.cn/cook/queryid?key="+  APPKEY+"&id=" +WayObjId;



                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url(url).build();
                String date = null;
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    date=response.body().string();
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("转发界面", "onCreate:date"+date);
                ShowTitle(date);

            }
        }).start();
    }
    private void ShowTitle(String data){
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < array.size(); i++) {
            Log.d("数据显示", "***********array.size(): "+array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            try{
                ZuoFaTu = subObject.get("albums").getAsString();
            }catch (Exception e1213){
            }
            Log.d("转发界面", "onCreate:监测点" +
                    "1");
            Log.d("显示菜谱界面", "检查点1");
            //int cishu = subObject.get("cishu").getAsInt();
            final String ZuoFaMing = subObject.get("title").getAsString();
            Shicai.setText(subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString());
            Log.d("转发界面", "onCreate:监测点2");
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.food_test_1);
                    Glide.with(ShareActivity.this)
                            .load(ZuoFaTu)
                            .apply(options)
                            .into(imageView);
                }
            });

        }
    }

}
