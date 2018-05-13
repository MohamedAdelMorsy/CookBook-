package com.scorpiomiku.cookbook.dynamic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.bean.Collection;
import com.scorpiomiku.cookbook.bean.SSEC;
import com.scorpiomiku.cookbook.combination.Way;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

public class MyDynamicActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<CBLEC> mList = new ArrayList<>();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<SSEC> mssec = new ArrayList<>();
    private String userObjId;
    //private String ZuoFaTu;
    private boolean FromUser;
    private String FangFa;
    private String FangFa2;
    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_dynamic_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        try {
            userObjId=pref.getString("user_objId", "您未登录");
            Log.d("我的分享界面", "onCreate:user_objId ="+userObjId);
        }catch (Exception e){
            userObjId="您未登录";
        }
        if (userObjId=="您未登录"){
            toast("您还没有登录");
        }else {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("user_objId", userObjId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
            ace.callEndpoint("get_commentByUserobjid", jsonObj, new CloudCodeListener() {
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
                            String CommentId = subObject.get("objectId").getAsString();
                            //str_1= subObject.get("username").getAsString();
                            //editor.putString("url",subObject.get("email").getAsString());
                            BmobQuery<Way> www = new BmobQuery<>();
                            Collection comment =new Collection();
                            comment.setObjectId(CommentId);
                            www.addWhereRelatedTo("comment", new BmobPointer(comment));
                            www.findObjects(new FindListener<Way>() {
                                @Override
                                public void done(List<Way> object,BmobException e) {
                                    if(e==null){
                                        Log.i("bmob","查询个数："+object.size());
                                        for (int i=0;i<object.size();i++){
                                            final Way way = object.get(i);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    initWay(way.getObjectId());
                                                }
                                            }).start();
                                        }

                                    }else{
                                        Log.i("bmob","失败："+e.getMessage());
                                        toast("bmob"+"失败："+e.getMessage());
                                    }
                                }

                            });

                        }

                    } else {
                        //toast(e.getMessage());
                        toast("收藏为空");
                    }//
                }
            });
        }
        /*for (int i = 0; i < 10; i++) {
            mList.add("1");
        }*/
        MyDynamicAdapter dynamicAdapter = new MyDynamicAdapter(mList,this);
        mRecyclerView.setAdapter(dynamicAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
    private void initWay(String objId){
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId",objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("分享界面initWay", "测试点5"+"存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("分享界面initWay", "测试点5"+"查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                try{
                                    FromUser= subObject.get("FromUser").getAsBoolean();
                                }catch (Exception d){
                                    FromUser=false;
                                }
                                String ZuoFaTu;
                                if(FromUser==true){
                                    FangFa = "get_zuofa";
                                    FangFa2 = "get_zuofa2";
                                }else {FangFa = "get_ZuoFa_sys";FangFa2= "get_ZuoFa_sys2";}
                                try {
                                    JsonObject ZuoFaTuyuan1 = subObject.get("zuoFaTuUser").getAsJsonObject();
                                    ZuoFaTu = ZuoFaTuyuan1.get("url").getAsString();
                                }catch (Exception as){
                                    ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                }
                                final String Way_objectID = subObject.get("objectId").getAsString();
                                try {
                                    int cishu = subObject.get("cishu").getAsInt();
                                }catch (Exception d){
                                    int cishu=0;
                                }
                                //int cishu = subObject.get("cishu").getAsInt();
                                final String ZuoFaMing = subObject.get("zuoFaMing").getAsString();
                                final String finalZuoFaTu = ZuoFaTu;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //identification
                                        try {
                                            jas.put("identification",Way_objectID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("分享界面initWay", "测试点5"+"开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
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
                                                        int ls_panding = 0;
                                                        for (int k = 0; k < 9; k++) {
                                                            if (k == 0) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao1").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang1").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 1) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao2").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang2").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 2) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao3").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang3").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 3) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao4").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang4").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 4) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao5").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang5").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 5) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao6").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang6").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 6) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao7").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang7").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 7) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao8").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang8").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 8) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao9").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang9").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 9) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao10").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang10").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+":";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            }
                                                        }
                                                        Log.d("分享界面initWay", "测试点5"+"查询食材成功");
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log.d("分享界面initWay", "测试点5"+"图片加载成功");
                                                                mList.add(new CBLEC(ZuoFaMing, finalZuoFaTu, "这里是介绍",Way_objectID,FromUser,mssec));
                                                                //*/
                                                                Log.d("分享界面initWay", "测试点5"+"放入对象成功");
                                                                mRecyclerView.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        MyDynamicAdapter  as =new MyDynamicAdapter(mList,MyDynamicActivity.this);
                                                                        Log.d("分享界面initWay", "测试点5"+"准备放入adapter");
                                                                        mRecyclerView.setAdapter(as);
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
                    }
                });
            }
        }).start();


    }

}
