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
        showOne();
        Log.d("第一图片显示界面", "onCreateView: 开始执行");
        //initView();
        return v;
    }
    private void showOne(){
        String data =  pref.getString("data","");
        Log.d("第一图片显示界面", "data: data:" +data);
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject resultOBJ = ssobject.get("result").getAsJsonObject();
         JsonArray dataARRAY = resultOBJ.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < dataARRAY.size(); i++) {
            final JsonObject dataARRAY_OLN = dataARRAY.get(i).getAsJsonObject();
            JsonArray stepsARRAY = dataARRAY_OLN.get("steps").getAsJsonArray();    //得到为json的数组
            mNutritionTextView.setText(dataARRAY_OLN.get("imtro").getAsString());

                JsonObject stepsARRAY_OLN = stepsARRAY.get(0).getAsJsonObject();
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.test);
                Glide.with(getActivity())
                        .load(stepsARRAY_OLN.get("img").getAsString())
                        .apply(options)
                        .into(mImageView);
                mStepsTextView.setText(stepsARRAY_OLN.get("step").getAsString());



        }
    }

    /*---------------------------------initView--------------------------*/
    private void initView() {

        mImageView.setImageResource(R.drawable.test);
        mStepsTextView.setText("这里放第一步的操作");
        mNutritionTextView.setText("这里放营养介绍");

    }
}
