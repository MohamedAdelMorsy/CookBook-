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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
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
        Bmob.initialize(this, "3bfd53d40a453ea66ce653ab658582d1");
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
        initWay(WayObjId);
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
    private void initWay(String objId){
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId",objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("收藏界面", "测试点5"+"存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            Log.d(TAG, "收藏界面: result："+result);
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("收藏界面", "测试点5"+"查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                try{
                                    panduan = subObject.get("FromUser").getAsBoolean();
                                }catch (Exception a){
                                    panduan = false;
                                }
                                String ZuoFaTu;
                                try{
                                    JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
                                    ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
                                    final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
                                }catch (Exception e1213){
                                    ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                }

                                //获取做法的图片url

                                final String Way_objectID = subObject.get("objectId").getAsString();
                                try {
                                    int cishu = subObject.get("cishu").getAsInt();
                                }catch (Exception d){
                                    int cishu=0;
                                }
                                //int cishu = subObject.get("cishu").getAsInt();
                                final String ZuoFaMing = subObject.get("zuoFaMing").getAsString();
                                final String finalZuoFaTu = ZuoFaTu;
                                final String finalZuoFaTu1 = ZuoFaTu;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //identification
                                        try {
                                            jas.put("identification",Way_objectID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("收藏界面", "测试点5"+"开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
                                                    Log.d("收藏界面", "测试点5"+"开始查询食材部分result = "+result);
                                                    JsonParser parser = new JsonParser();  //创建JSON解析器
                                                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                                                    for (int i = 0; i <= array.size(); i++) {
                                                        final JsonObject subObject = array.get(i).getAsJsonObject();
                                                        final String yongliao[] = new String[10];
                                                        final String yongliaoliang[] = new String[10];

                                                               /*{"results":
                                                                    [{"createdAt":"2017-07-10 16:11:26",
                                                                        "identification":"49b2867f32","objectId":"65149c467e",
                                                                        "updatedAt":"2017-07-10 16:11:26",
                                                                        "yongLiao1":"","yongLiao2":"",
                                                                        "yongLiao3":"",
                                                                        "yongLiao4":"",
                                                                        "yongLiao5":"","yongLiao6":"","yongLiao7":"","yongLiao8":"","yongLiao9":"","yongLiaoLiang1":"","yongLiaoLiang2":"","yongLiaoLiang3":"","yongLiaoLiang4":"","yongLiaoLiang5":"","yongLiaoLiang6":"","yongLiaoLiang7":"","yongLiaoLiang8":"","yongLiaoLiang9":""}]}
                                                                        "ShiCaiLiang":7,
                                                                        */
                                                        int ShiCaiLiang = 0;
                                                        ShiCaiLiang = 9;
                                                        int ls_panding = 0;
                                                        for (int k = 0; k < ShiCaiLiang; k++) {
                                                            if (k == 0) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao1").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang1").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 1) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao2").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang2").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 2) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao3").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang3").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 3) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao4").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang4").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 4) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao5").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang5").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 5) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao6").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang6").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 6) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao7").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang7").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 7) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao8").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang8").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 8) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao9").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang9").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 9) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao10").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang10").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            }
                                                        }
                                                        for(;ls_panding<10;ls_panding++){
                                                            yongliao[ls_panding] = " ";
                                                            yongliaoliang[ls_panding] = " ";
                                                        }
                                                        Log.d("收藏界面", "测试点5"+"图片加载成功");
                                                        String s= "";
                                                        for (int h= 0;i<ls_panding;i++){
                                                            s = s + yongliao[i] + yongliaoliang[i];
                                                        }
                                                        Shicai.setText(s);
                                                        RequestOptions options = new RequestOptions()
                                                                .centerCrop()
                                                                .placeholder(R.drawable.food_test_1);
                                                        Glide.with(ShareActivity.this)
                                                                .load(finalZuoFaTu1)
                                                                .apply(options)
                                                                .into(imageView);
                                                    }
                                                }
                                            }
                                        });
                                    }

                                }).start();
                            }

                        }
                    }
                });
            }
        }).start();


    }
}
