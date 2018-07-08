package com.scorpiomiku.cookbook.menuactivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.ShareActivity;
import com.scorpiomiku.cookbook.bean.Clock;
import com.scorpiomiku.cookbook.bean.Collection;
import com.scorpiomiku.cookbook.bean.Sound;
import com.scorpiomiku.cookbook.bean.TestCPEC;
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.recommend.BreakFastFragment;
import com.scorpiomiku.cookbook.sql.BasketDataHelper;
import com.scorpiomiku.cookbook.sql.Way_datahelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.APPKEY;
import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.URL;

public class MenuActivity extends AppCompatActivity {

    private String TAG = "MenuActivity";
    private Clock mClock;

    private int h;
    private Way_datahelper mWaydatahelper;
    private BasketDataHelper mBasketDataHelper;
    private boolean FromUser;
    private List<TestCPEC> mShowCPist = new ArrayList<>();
    private ImageView mDishImageView;
    private TextView mDishNameTextView;
    private TextView mDishMaterialsTextView;
    private RecyclerView mStepsRecyclerView;
    private TextView mNutritionTextView;
    private ImageView mClockImageView;
    private TextView mLookAllTextView;
    private ImageView mShouCang;
    private ImageView mZhuanFa;
    private ImageView mCaiLanZi;

    private int mTimeCount = 0;
    private Timer timer = new Timer();
    private TimerTask mTimerTask = null;

    private String Way_objectID;
    private String FangFa;
    private String FangFa2;
    private String ZuoFaTu;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final int MSG_WHAT_TIME_IS_UP = 1;//时间到了
    private static final int MSG_WHAT_TIME_IS_TICK = 2;//时间减少中

    private String name_basket;
    private String shicai_basket;
    private String charge = "0";
    private Sound mSound;

    private FragmentManager fm;

    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    /*---------------------------changeFragment--------------------------*/

    private void changeFragment(Fragment fragment) {
        Fragment fragment1 = fm.findFragmentById(R.id.menu_activity_container);
        if (!fragment1.getClass().getName().equals(fragment.getClass().getName())) {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .hide(fragment1)
                    .add(R.id.menu_activity_container, fragment)
                    .commit();
        }
    }

    /*--------------------------isFragmentFooter------------------------*/
    private boolean isFragmentFooter() {
        Fragment f = fm.findFragmentById(R.id.menu_activity_container);
        if (f.getClass().getName().equals(MenuRecyclerStepsFragment.class.getName())) {
            return true;
        } else {
            return false;
        }
    }
    private void adasda(){
//    private void initWay(){
//        JSONObject jas = new JSONObject();
//        try {
//            jas.put("objectId",Way_objectID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
//        ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
//            public static final String TAG = "thing";
//            @Override
//            public void done(Object object, BmobException e) {
//                if (e == null) {
//                    String result = object.toString();
//                    /*{"results":[{
//                    "cishu":0,"createdAt":"2017-07-04 20:03:31",
//                    "objectId":"47295b4608",
//                    "shiDuan":"中","updatedAt":"2017-07-04 20:03:31",
//                    "zuoFa":"炒","zuoFaMing":"酱肉丁",
//                    "zuoFaTu":{"__type":"File","filename":"酱肉丁.gif","group":"","url":"http://bmob-cdn-12102.b0.upaiyun.com/2017/07/04/40aae1d7aa67496985dba4499167ced9.gif"}}]}
//                    * */
//                    JsonParser parser = new JsonParser();  //创建JSON解析器
//                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
//                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
//                    for (int i = 0; i < array.size(); i++) {
//                        final JsonObject subObject = array.get(i).getAsJsonObject();
//                        try{
//                            FromUser=subObject.get("FromUser").getAsBoolean();
//                        }catch (Exception s){
//
//                        }
//                        if(FromUser==true){
//                            FangFa = "get_zuofa";
//                            FangFa2 = "get_zuofa2";
//                        }else {FangFa = "get_ZuoFa_sys";FangFa2= "get_ZuoFa_sys2";}
//                        Way_objectID = subObject.get("objectId").getAsString();
//                        final String ZuoFaMing=subObject.get("zuoFaMing").getAsString();
//                        name_basket=ZuoFaMing;
//                        mDishNameTextView.setText(ZuoFaMing);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try{
//                                    JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
//                                    ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
//                                    final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
//                                }catch (Exception e1213){
//                                    ZuoFaTu = subObject.get("zuoFaTu").getAsString();
//                                }
//                                mDishImageView.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        RequestOptions options = new RequestOptions()
//                                                .centerCrop()
//                                                .placeholder(R.mipmap.ic_launcher);
//                                        Glide.with(MenuActivity.this)
//                                                .load(ZuoFaTu)
//                                                .apply(options)
//                                                .into(mDishImageView);
//                                        initShiCai();
//                                        initZuofa();
//                                    }
//                                });
//                            }
//                        }).start();
//
//                    }
//                }
//                else {
//                    Log.e(TAG, " " + e.getMessage());
//                }
//            }
//        });
//        fm.beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out)
//                .add(R.id.menu_activity_container, DefaultFragment.newInstance())
//                .commit();
//
//    }
//    private void initShiCai()  {
//        Log.d("菜谱界面", "initShiCai: "+"开始食材加载");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("菜谱界面", "initShiCai: "+"开始主线程");
//                JSONObject jas = new JSONObject();
//                try {
//                    jas.put("identification", Way_objectID);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
//                ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
//                    public static final String TAG = "thing";
//                    @Override
//                    public void done(Object object, BmobException e) {
//                        if (e == null) {
//                            String result = object.toString();
//                            Log.e("食材部分", "测试点：result" + result);
//                            JsonParser parser = new JsonParser();  //创建JSON解析器
//                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
//                            JsonArray array = ssobject.get("results").getAsJsonArray();
//                            Log.d("菜谱界面", "initShiCai: "+"获得返回结果成功"+result);//得到为json的数组
//                            for (int i = 0; i <= array.size(); i++) {
//                                final JsonObject subObject = array.get(i).getAsJsonObject();
//                                String yongliao[] = new String[10];
//                                String yongliaoliang[] = new String[10];
//                                int ls_panding  = 0;
//                                for (int k = 0; k < 9; k++) {
//                                    if (k == 0) {
//                                        yongliao[0] = subObject.get("yongLiao1").getAsString();
//                                        yongliaoliang[0] = subObject.get("yongLiaoLiang1").getAsString();
//                                        if(yongliao[0]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 1) {
//                                        yongliao[1] = subObject.get("yongLiao2").getAsString();
//                                        yongliaoliang[1] = subObject.get("yongLiaoLiang2").getAsString();
//                                        if(yongliao[1]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 2) {
//                                        yongliao[2] = subObject.get("yongLiao3").getAsString();
//                                        yongliaoliang[2] = subObject.get("yongLiaoLiang3").getAsString();
//                                        if(yongliao[2]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 3) {
//                                        yongliao[3] = subObject.get("yongLiao4").getAsString();
//                                        yongliaoliang[3] = subObject.get("yongLiaoLiang4").getAsString();
//                                        if(yongliao[3]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 4) {
//                                        yongliao[4] = subObject.get("yongLiao5").getAsString();
//                                        yongliaoliang[4] = subObject.get("yongLiaoLiang5").getAsString();
//                                        if(yongliao[4]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 5) {
//                                        yongliao[5] = subObject.get("yongLiao6").getAsString();
//                                        yongliaoliang[5] = subObject.get("yongLiaoLiang6").getAsString();
//                                        if(yongliao[5]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 6) {
//                                        yongliao[6] = subObject.get("yongLiao7").getAsString();
//                                        yongliaoliang[6] = subObject.get("yongLiaoLiang7").getAsString();
//                                        if(yongliao[6]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 7) {
//                                        yongliao[7] = subObject.get("yongLiao8").getAsString();
//                                        yongliaoliang[7] = subObject.get("yongLiaoLiang8").getAsString();
//                                        if(yongliao[7]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 8) {
//                                        yongliao[8] = subObject.get("yongLiao9").getAsString();
//                                        yongliaoliang[8] = subObject.get("yongLiaoLiang9").getAsString();
//                                        if(yongliao[8]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    } else if (k == 9) {
//                                        yongliao[9] = subObject.get("yongLiao10").getAsString();
//                                        yongliaoliang[9] = subObject.get("yongLiaoLiang10").getAsString();
//                                        if(yongliao[9]!=""){
//                                            yongliao[ls_panding] = yongliao[ls_panding]+":";
//                                            yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
//                                            ls_panding= ls_panding+1;
//                                        }
//                                    }
//                                }
//                                for(;ls_panding<10;ls_panding++){
//                                    yongliao[ls_panding] = "";
//                                    yongliaoliang[ls_panding] = "";
//                                }
//                                initShicai(yongliao,yongliaoliang);
//                            }
//                        }
//                        else {
//                            Log.e(TAG, " " + e.getMessage());
//                        }
//                    }
//                });
//            }
//        }).start();
//
//    }
//    private void initShicai(String[] yongliao,String[] yongliaoliang){
//        mDishMaterialsTextView.setText(yongliao[1]+yongliaoliang[1]
//                +yongliao[2]+yongliaoliang[2]
//                +yongliao[3]+yongliaoliang[3]+yongliao[4]+yongliaoliang[4]+yongliao[5]+yongliaoliang[5]
//                +yongliao[6]+yongliaoliang[6]+yongliao[7]+yongliaoliang[7]+yongliao[8]+yongliaoliang[8]
//                +yongliao[9]+yongliaoliang[9]);
//        shicai_basket = yongliao[1]+yongliao[2]+yongliao[3]+yongliao[4]+yongliao[5]+yongliao[6]+yongliao[7]+yongliao[8]+yongliao[9];
//    }
        //    public void initZuofa(){
//        final JSONObject jas = new JSONObject();
//        try {
//            jas.put("identification",Way_objectID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
//        Log.d("菜谱界面", "initZuofa:"+"使用方法为:"+FangFa);
//        ace1.callEndpoint(FangFa, jas, new CloudCodeListener() {
//            public static final String TAG = "thing";
//
//            @Override
//            public void done(Object object, BmobException e) {
//                if (e == null) {
//                    String result = object.toString();
//                    Log.d("菜谱界面", "initZuofa:"+"获取步骤结果为:"+result);
//                    JsonParser parser = new JsonParser();  //创建JSON解析器
//                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
//                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
//                    for (int i = 0; i < array.size(); i++) {
//                        JsonObject subObject = array.get(i).getAsJsonObject();
//                        for (; h <= 6; h = h + 1) {
//                            if (h == 1) {
//                                final String buZhou1NeiRong = subObject.get("buZhou1NeiRong").getAsString();
//                                String buZhou1photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou1photo").getAsJsonObject();
//                                    buZhou1photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou1photo = subObject.get("buZhou1photo").getAsString();
//                                }
//                                editor.putString("buZhou1NeiRong",buZhou1NeiRong);
//                                editor.putString("buZhou1photo",buZhou1photo);
//                                editor.apply();
//                            } else if (h == 2) {
//                                final String buZhou2NeiRong = subObject.get("buZhou2NeiRong").getAsString();
//                                String buZhou2photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1= subObject.get("buZhou2photo").getAsJsonObject();
//                                    buZhou2photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou2photo = subObject.get("buZhou2photo").getAsString();
//                                }
//                                editor.putString("buZhou2NeiRong",buZhou2NeiRong);
//                                editor.putString("buZhou2photo",buZhou2photo);
//                                editor.apply();
//                            } else if (h == 3) {
//                                final String buZhou3NeiRong = subObject.get("buZhou3NeiRong").getAsString();
//                                String buZhou3photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou3photo").getAsJsonObject();
//                                    buZhou3photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou3photo = subObject.get("buZhou3photo").getAsString();
//                                }
//                                editor.putString("buZhou3NeiRong",buZhou3NeiRong);
//                                editor.putString("buZhou3photo",buZhou3photo);
//                                editor.apply();
//                            } else if (h == 4) {
//                                final String buZhou4NeiRong = subObject.get("buZhou4NeiRong").getAsString();
//                                String buZhou4photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou4photo").getAsJsonObject();
//                                    buZhou4photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou4photo = subObject.get("buZhou4photo").getAsString();
//                                }
//                                editor.putString("buZhou4NeiRong",buZhou4NeiRong);
//                                editor.putString("buZhou4photo",buZhou4photo);
//                                editor.apply();
//                            } else if (h == 5) {
//                                final String buZhou5NeiRong = subObject.get("buZhou5NeiRong").getAsString();
//                                String buZhou5photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou5photo").getAsJsonObject();
//                                    buZhou5photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou5photo = subObject.get("buZhou5photo").getAsString();
//                                }
//                                editor.putString("buZhou5NeiRong",buZhou5NeiRong);
//                                editor.putString("buZhou5photo",buZhou5photo);
//                                editor.apply();
//                            } else if (h == 6) {
//                                final String buZhou6NeiRong = subObject.get("buZhou6NeiRong").getAsString();
//                                String buZhou6photo = "";
//                                try {
//                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou6photo").getAsJsonObject();
//                                    buZhou6photo = ZuoFaTuyuan1.get("url").getAsString();
//                                }catch (Exception as){
//                                    buZhou6photo = subObject.get("buZhou6photo").getAsString();
//                                }
//                                editor.putString("buZhou6NeiRong",buZhou6NeiRong);
//                                editor.putString("buZhou6photo",buZhou6photo);
//                                editor.apply();
//                            }
//                        }
//                    }
//                    AsyncCustomEndpoints ace2 = new AsyncCustomEndpoints();
//                    ace2.callEndpoint(FangFa2, jas, new CloudCodeListener() {
//                        public static final String TAG = "thing";
//                        @Override
//                        public void done(Object object, BmobException e) {
//                            if (e == null) {
//                                String result = object.toString();
//                                Log.d("菜谱界面", "initZuofa:"+"获取步骤结果为:"+result);
//                                JsonParser parser = new JsonParser();  //创建JSON解析器
//                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
//                                JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
//                                for (int i = 0; i < array.size(); i++) {
//                                    JsonObject subObject = array.get(i).getAsJsonObject();
//                                    for (h=1; h <= 6; h = h + 1) {
//                                        if (h == 1) {
//                                            final String buZhou1NeiRong = subObject.get("buZhou7NeiRong").getAsString();
//                                            String buZhou1photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1 = subObject.get("buZhou7photo").getAsJsonObject();
//                                                buZhou1photo = ZuoFaTuyuan1.get("url").getAsString();
//                                            }catch (Exception as){
//                                                buZhou1photo = subObject.get("buZhou7photo").getAsString();
//                                            }
//                                            editor.putString("buZhou7NeiRong",buZhou1NeiRong);
//                                            editor.putString("buZhou7photo",buZhou1photo);
//                                            editor.apply();
//                                        } else if (h == 2) {
//                                            final String buZhou2NeiRong = subObject.get("buZhou8NeiRong").getAsString();
//                                            String buZhou2photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1= subObject.get("buZhou8photo").getAsJsonObject();
//                                                buZhou2photo = ZuoFaTuyuan1.get("url").getAsString();
//                                            }catch (Exception as){
//                                                buZhou2photo = subObject.get("buZhou8photo").getAsString();
//                                            }
//                                            editor.putString("buZhou8NeiRong",buZhou2NeiRong);
//                                            editor.putString("buZhou8photo",buZhou2photo);
//                                            editor.apply();
//                                        } else if (h == 3) {
//                                            final String buZhou3NeiRong = subObject.get("buZhou9NeiRong").getAsString();
//                                            String buZhou3photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1 = subObject.get("buZhou9photo").getAsJsonObject();
//                                                buZhou3photo = ZuoFaTuyuan1.get("url").getAsString();
//                                            }catch (Exception as){
//                                                buZhou3photo = subObject.get("buZhou9photo").getAsString();
//                                            }
//                                            editor.putString("buZhou9NeiRong",buZhou3NeiRong);
//                                            editor.putString("buZhou9photo",buZhou3photo);
//                                            editor.apply();
//                                            mShowCPist.add(new TestCPEC(buZhou3NeiRong,buZhou3photo));
//
//                                        } else if (h == 4) {
//                                            final String buZhou4NeiRong = subObject.get("buZhou10NeiRong").getAsString();
//                                            String buZhou4photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1 = subObject.get("buZhou10photo").getAsJsonObject();
//                                                buZhou4photo = ZuoFaTuyuan1.get("url").getAsString();
//                                            }catch (Exception as){
//                                                buZhou4photo = subObject.get("buZhou10photo").getAsString();
//                                            }
//                                            editor.putString("buZhou10NeiRong",buZhou4NeiRong);
//                                            editor.putString("buZhou10photo",buZhou4photo);
//                                            editor.apply();
//
//                                        } else if (h == 5) {
//                                            final String buZhou5NeiRong = subObject.get("buZhou11NeiRong").getAsString();
//                                            String buZhou5photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1 = subObject.get("buZhou11photo").getAsJsonObject();
//                                                buZhou5photo = ZuoFaTuyuan1.get("url").getAsString();
//
//                                            }catch (Exception as){
//                                                buZhou5photo = subObject.get("buZhou11photo").getAsString();
//                                            }
//                                            editor.putString("buZhou11NeiRong",buZhou5NeiRong);
//                                            editor.putString("buZhou11photo",buZhou5photo);
//                                            editor.apply();
//
//                                        } else if (h == 6) {
//                                            final String buZhou6NeiRong = subObject.get("buZhou12NeiRong").getAsString();
//                                            String buZhou6photo = "";
//                                            try {
//                                                JsonObject ZuoFaTuyuan1 = subObject.get("buZhou12photo").getAsJsonObject();
//                                                buZhou6photo = ZuoFaTuyuan1.get("url").getAsString();
//                                            }catch (Exception as){
//                                                buZhou6photo = subObject.get("buZhou12photo").getAsString();
//                                            }
//                                            editor.putString("buZhou12NeiRong",buZhou6NeiRong);
//                                            editor.putString("buZhou12photo",buZhou6photo);
//                                            editor.apply();
//                                        }
//
//                                    }
//                                }
//                            }else{
//                            }
//                        }
//                    });
//
//                }else{
//
//                }
//            }
//        });
//    }
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_layout);
        mCaiLanZi = (ImageView)findViewById(R.id.menu_cailanzi) ;
        mShouCang=(ImageView)findViewById(R.id.menu_shoucang) ;
        mZhuanFa = (ImageView)findViewById(R.id.menu_zhuanfa) ;
        mDishImageView = (ImageView) findViewById(R.id.menu_fragment_image_view_dish_images);
        mDishNameTextView = (TextView) findViewById(R.id.menu_fragment_text_view_dish_name);
        mDishMaterialsTextView = (TextView) findViewById(R.id.menu_fragment_text_view_dish_materials);
        mLookAllTextView = (TextView) findViewById(R.id.menu_activity_lookall_text_view);


        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        try{
            Intent intent =getIntent();
            Way_objectID=intent.getStringExtra("Way_objectID");
            charge=intent.getStringExtra("charge").toString();
        }catch (Exception e){
            Intent intent =getIntent();
            Way_objectID=intent.getStringExtra("Way_objectID");
        }
        //charge =charge.toString();
        String a = "1";
        if (charge.equals(a)){

        }else sqldata(Way_objectID);
        Log.d("显示菜谱界面", "onCreate: Way_objectID="+Way_objectID);



        fm = getSupportFragmentManager();
        urlget();


        AllListener();


        //initWay();
        //initView();
        setListener();

    }

    private void urlget(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;

                    url = "http://apis.juhe.cn/cook/queryid?key="+  APPKEY+"&id=" +Way_objectID;



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
            Log.d("显示菜谱界面", "检查点1");
            //int cishu = subObject.get("cishu").getAsInt();
            final String ZuoFaMing = subObject.get("title").getAsString();
            name_basket=ZuoFaMing;
            mDishNameTextView.setText(ZuoFaMing);
            Log.d("显示菜谱界面", "检查点2");
            mDishImageView.post(new Runnable() {
                @Override
                public void run() {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher);
                    Glide.with(MenuActivity.this)
                            .load(ZuoFaTu)
                            .apply(options)
                            .into(mDishImageView);
                }
            });
            Log.d("显示菜谱界面", "检查点3");
            mDishMaterialsTextView.setText(subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString());
            shicai_basket = subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString();
            editor.putString("data",data);
            editor.apply();
            Log.d("显示菜谱界面", "检查点4");
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .add(R.id.menu_activity_container, DefaultFragment.newInstance())
                    .commit();
            Log.d("显示菜谱界面", "检查点5");
        }
    }
    private void AllListener(){
        //菜篮子
        mCaiLanZi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasketDataHelper = new BasketDataHelper(MenuActivity.this,"BasketStore.db",null,1);
                SQLiteDatabase db = mBasketDataHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("foodname", name_basket);
                values.put("material", shicai_basket);
                db.insert("Basket", null, values);
                values.clear();
                toast("已将配料加入菜篮子");
            }
        });
        //收藏
        mShouCang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] WayOBjID = {null};
                JSONObject objid = new JSONObject();
                try {
                    objid.put("identification", Way_objectID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCustomEndpoints go = new AsyncCustomEndpoints();
                go.callEndpoint("get_id", objid, new CloudCodeListener() {
                    @Override

                    public void done(Object object, BmobException e) {
                        if (e == null) {

                                    Log.d("收藏部错误 == null", "done: 返回objid "+object.toString());
                            if (object.toString().equals("{\"results\":[]}")){
                                Log.d("收藏部分","需要创建Way");
                                Way p2 = new Way();
                                p2.setObjid(Way_objectID);
                                p2.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId,BmobException e) {
                                        if(e==null){
                                           // toast("添加数据成功，返回objectId为："+objectId);
                                        }else{
                                           // toast("创建数据失败：" + e.getMessage());
                                        }
                                    }
                                });
                                Log.d("收藏部分","创建成工开始查询");
                                String result =object.toString();
                                JsonParser parser = new JsonParser();  //创建JSON解析器
                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                JsonArray array = ssobject.get("results").getAsJsonArray();
                                //得到为json的数组
                                for (int i = 0; i < array.size(); i++) {
                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                     WayOBjID[0] = subObject.get("objectId").getAsString();
                                    Log.d("主函数", "检查点"+"数据加载完成");
                                }

                            }else{
                                Log.d("收藏部分","已有直接查询");
                                String result =object.toString();
                                JsonParser parser = new JsonParser();  //创建JSON解析器
                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                JsonArray array = ssobject.get("results").getAsJsonArray();
                                //得到为json的数组
                                for (int i = 0; i < array.size(); i++) {
                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                     WayOBjID[0] = subObject.get("objectId").getAsString();
                                    Log.d("主函数", "检查点"+"数据加载完成");
                                }
                            }
                        } else {

                        }//
                    }
                });



                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("user_objId", pref.getString("user_objId",""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                Log.d("收藏部分", "ace设置完成");

                ace.callEndpoint("get_commentByUserobjid", jsonObj, new CloudCodeListener() {
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            Log.d("收藏部分e == null", "done: "+object.toString());
                            if (object.toString().equals("{\"results\":[]}")){
                                //即表是空的
                                Log.d("收藏部分e == null", "done: "+object.toString()+"str2是空的");
                                Collection comment = new Collection();
                                BmobRelation relation = new BmobRelation();
                                comment.setObjectId(pref.getString("user_objId",""));
                                Way way = new Way();
                                way.setObjectId(WayOBjID[0]);
                                relation.add(way);
                                comment.setUserObjID(pref.getString("user_objId",""));
                                comment.setComment(relation);
                                comment.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId, BmobException e) {
                                        if(e==null){
                                            toast("创建数据成功：" + objectId);
                                        }else{
                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }else{
                                String result =object.toString();
                                JsonParser parser = new JsonParser();  //创建JSON解析器
                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                JsonArray array = ssobject.get("results").getAsJsonArray();
                                //得到为json的数组
                                for (int i = 0; i < array.size(); i++) {
                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                    String CommentId = subObject.get("objectId").getAsString();
                                    Log.d("主函数", "检查点"+"数据加载完成");
                                    Collection comment = new Collection();
                                    BmobRelation relation = new BmobRelation();
                                    comment.setObjectId(CommentId);
                                    Way way = new Way();

                                    way.setObjectId(WayOBjID[0]);
                                    relation.add(way);
                                    comment.setComment(relation);
                                    comment.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                toast("成功");
                                                Log.i("bmob","多对多关联添加成功");
                                            }else{
                                                Log.i("bmob","失败："+e.getMessage());
                                                toast("失败："+e.getMessage());
                                            }
                                        }

                                    });
                                }
                            }
                        } else {

                        }//
                    }
                });
                //toast("已加入收藏列表");
            }
        });
        //转发
        mZhuanFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ShareActivity.class);
                intent.putExtra("user_objId",pref.getString("user_objId",""));
                intent.putExtra("WayObjId",Way_objectID);
                startActivity(intent);
            }
        });
    }
    private void sqldata(String way_objectID){
        Way_datahelper dbHelper = new Way_datahelper(this, "way_objectIDStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Way_objectID",way_objectID );
        db.insert("Way_objectID", null, values);
        values.clear();
    }

    /*---------------------------------initView-------------------------------*/
    private void initView() {
        mDishImageView.setImageResource(R.drawable.test_food);
        mDishNameTextView.setText("菜名");
        mDishMaterialsTextView.setText("菜材料");
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .add(R.id.menu_activity_container, DefaultFragment.newInstance())
                .commit();

    }

    /*----------------------------------setListener--------------------------------*/
    private void setListener() {
        mLookAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFragmentFooter()) {
                    changeFragment(DefaultFragment.newInstance());
                } else {
                    changeFragment(MenuRecyclerStepsFragment.newInstance());
                }
            }
        });

    }

    /*--------------------------------------Back----------------------*/
    @Override
    public void onBackPressed() {
        if (isFragmentFooter()) {
            changeFragment(DefaultFragment.newInstance());
        } else {
            super.onBackPressed();
        }
    }
}
