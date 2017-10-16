package com.scorpiomiku.cookbook.menuactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by Administrator on 2017/7/1.
 */

public class DefaultFragment extends FragmentModule {

    private ImageView mImageView;
    private TextView mStepsTextView;
    private TextView mNutritionTextView;
    private String FangFa;
    private String FangFa2;
    private String ZuoFaTu;
    private boolean FromUser;
    private String Way_objectID;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public static DefaultFragment newInstance() {
        return new DefaultFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_steps_default_layout, container, false);
        mImageView = (ImageView) v.findViewById(R.id.menu_defualt_fragment_steps_iamge_view);
        mStepsTextView = (TextView) v.findViewById(R.id.menu_defualt_fragment_steps_text_view);
        mNutritionTextView = (TextView) v.findViewById(R.id.menu_defualt_fragment_nutrition_text_view);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        initWay();
        Log.d("第一图片显示界面", "onCreateView: 开始执行");
        //initView();
        return v;
    }
    private void initWay(){
        JSONObject jas = new JSONObject();
        try {
            jas.put("objectId",pref.getString("LS_Way_objectID",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("第一图片显示界面", "onCreateView: LS_Way_objectID"+pref.getString("LS_Way_objectID",""));
        AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
            public static final String TAG = "thing";
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result = object.toString();
                    /*{"results":[{
                    "cishu":0,"createdAt":"2017-07-04 20:03:31",
                    "objectId":"47295b4608",
                    "shiDuan":"中","updatedAt":"2017-07-04 20:03:31",
                    "zuoFa":"炒","zuoFaMing":"酱肉丁",
                    "zuoFaTu":{"__type":"File","filename":"酱肉丁.gif","group":"","url":"http://bmob-cdn-12102.b0.upaiyun.com/2017/07/04/40aae1d7aa67496985dba4499167ced9.gif"}}]}
                    * */
                    JsonParser parser = new JsonParser();  //创建JSON解析器
                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                    for (int i = 0; i < array.size(); i++) {
                        final JsonObject subObject = array.get(i).getAsJsonObject();
                        try{
                            FromUser=subObject.get("FromUser").getAsBoolean();
                        }catch (Exception s){
                        }
                        if(FromUser==true){
                            FangFa = "get_zuofa";
                            FangFa2 = "get_zuofa2";
                        }else {FangFa = "get_ZuoFa_sys";FangFa2= "get_ZuoFa_sys2";}
                        Way_objectID = subObject.get("objectId").getAsString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initZuofa();
                            }
                        }).start();
                        Log.d("第一图片显示界面", "onCreateView: way查看完毕");
                        Log.d("第一图片显示界面", "onCreateView:FangFa= "+FangFa);

                    }
                }
                else {
                    Log.e(TAG, " " + e.getMessage());
                }
            }
        });
    }
    private void initZuofa(){
        final JSONObject jas = new JSONObject();
        try {
            jas.put("identification",Way_objectID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("第一图片显示界面", "onCreateView: initZuofa开始");
        AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();

        ace1.callEndpoint(FangFa, jas, new CloudCodeListener() {
            public static final String TAG = "thing";

            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result = object.toString();
                    Log.d("第一图片显示界面", "initZuofa:"+"获取步骤结果为:"+result);
                    JsonParser parser = new JsonParser();  //创建JSON解析器
                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject subObject = array.get(i).getAsJsonObject();
                        int h =0;
                        for (; h <= 6; h = h + 1) {
                            if (h == 1) {
                                final String buZhou1NeiRong = subObject.get("buZhou1NeiRong").getAsString();
                                String buZhou1photo = "";
                                try {
                                    JsonObject ZuoFaTuyuan1 = subObject.get("buZhou1photo").getAsJsonObject();
                                    buZhou1photo = ZuoFaTuyuan1.get("url").getAsString();
                                }catch (Exception as){
                                    buZhou1photo = subObject.get("buZhou1photo").getAsString();
                                }
                                // mImageView.setImageResource(R.drawable.test);
                                ///修改部分
                                Log.d("第一图片显示界面", "onCreateView: buZhou1photo="+buZhou1photo);
                                Log.d("第一图片显示界面", "onCreateView: initZuofa开始导入图片");
                                RequestOptions options = new RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.drawable.test);
                                Glide.with(getActivity())
                                        .load(buZhou1photo)
                                        .apply(options)
                                        .into(mImageView);
                                Log.d("第一图片显示界面", "onCreateView: 图片导入完成");
                                mStepsTextView.setText(buZhou1NeiRong);
                                mNutritionTextView.setText("这里放营养介绍");

                            }
                        }
                    }

                }else{

                }
            }
        });



    }
    /*---------------------------------initView--------------------------*/
    private void initView() {

        mImageView.setImageResource(R.drawable.test);
        mStepsTextView.setText("这里放第一步的操作");
        mNutritionTextView.setText("这里放营养介绍");

    }
}
