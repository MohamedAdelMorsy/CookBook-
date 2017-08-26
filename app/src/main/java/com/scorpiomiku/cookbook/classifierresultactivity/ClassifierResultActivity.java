package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

public class ClassifierResultActivity extends AppCompatActivity {
    private String mPicturePath;
    private String mPictureResult;

    private ImageView mImageView;
    private TextView mDescribeTextView;
    private TextView mNutritionTextView;
    private TextView mFoodNameTextView;


    private static final String TAG = "ClassifierResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier_result);
        Intent i = getIntent();
        mPicturePath = i.getStringExtra("picturePath");
        mPictureResult = i.getStringExtra("pictureResult");
        mImageView = (ImageView) findViewById(R.id.classifier_result_imageview);
        mDescribeTextView = (TextView) findViewById(R.id.classifier_result_describe_textview);
        mNutritionTextView = (TextView) findViewById(R.id.nutrition_know_text_view);
        mFoodNameTextView = (TextView) findViewById(R.id.classifier_result_food_name);
        initView();
        Log.d(TAG, "onCreate: " + mPicturePath + "    " + mPictureResult);
    }
    private void initView(){
        mImageView.setImageResource(R.drawable.potetotest);
        mFoodNameTextView.setText(mPictureResult);
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
