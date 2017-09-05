package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.FoodMaterials;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

public class ClassifierResultActivity extends AppCompatActivity {

    private ImageView mTestImageView;


    private String mPicturePath;
    private String mFoodName;

    private ImageView mImageView;
    private TextView mDescribeTextView;
    private TextView mNutritionTextView;
    private TextView mFoodNameTextView;

    private String mFragmentSendMessage;
    private Intent i;

    private int AllNU;
    private int t;


    private static final String TAG = "ClassifierResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier_result);

        i = getIntent();
        mFragmentSendMessage = i.getStringExtra("FragmentSendMessage");

        mImageView = (ImageView) findViewById(R.id.classifier_result_imageview);
        mDescribeTextView = (TextView) findViewById(R.id.classifier_result_describe_textview);
        mNutritionTextView = (TextView) findViewById(R.id.nutrition_know_text_view);
        mFoodNameTextView = (TextView) findViewById(R.id.classifier_result_food_name);
        mTestImageView = (ImageView) findViewById(R.id.test_iamge_view_result);

        /*CameraActivity  RecommendFragment  CombinationFragment*/
        chooseInit();
        //Log.d(TAG, "onCreate: " + mPicturePath + "    " + mFoodName);
    }

    /*---------------------------RecommendFragment-----------------------------*/
    private void recommendInitView() {
        mFoodName = i.getStringExtra("foodname");

        initUrlView();
    }

    /*-----------------------------TakePhotoAndClassifier---------------------------*/
    private void cameraInitView() {
        mPicturePath = i.getStringExtra("picturePath");
        mFoodName = i.getStringExtra("pictureResult");
        initUrlView();
    }

    /*---------------------------whichFragmentSendMassege------------------------*/
    private void chooseInit() {
        if (mFragmentSendMessage.equals("RecommendFragment")) {
            Log.d(TAG, "chooseInit: RecommendFragment");
            recommendInitView();
        }
        if (mFragmentSendMessage.equals("CameraActivity")) {
            Log.d(TAG, "chooseInit: CameraActivity");
            cameraInitView();
        }
        if (mFragmentSendMessage.equals("CombinationFragment")) {
            Log.d(TAG, "chooseInit: CombinationFragment");
        }
    }
    /*---------------------------initUrlView--------------------------*/

    private void initUrlView() {
        mImageView.setImageResource(R.drawable.potetotest);
        mFoodNameTextView.setText(mFoodName);
        mDescribeTextView.setText("   今天需要在TextView上面添加一个边框，但是TextView本身不支持边框，" +
                "所以只能采用其他方式，在网上查询了一下，主要有三种方式可以实现1.带有边框的透明图片2.使用x" +
                "ml的shape设置3继承TextView覆写onDraw方法。");
        mNutritionTextView.setText("   大致意思是说我使用的 commit方法是在Activity的onSaveInstanceState()之后调用的" +
                "，这样会出错，因为onSaveInstanceState\n" +
                "方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后再给它添加Fragment就会出错" +
                "。解决办法就\n" +
                "是把commit（）方法替换成 commitAllowingStateLoss()就行了，其效果是一样的。\n");
    }


}
