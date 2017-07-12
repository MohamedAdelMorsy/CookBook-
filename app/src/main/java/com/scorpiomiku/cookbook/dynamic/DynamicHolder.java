package com.scorpiomiku.cookbook.dynamic;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DynamicHolder extends RecyclerView.ViewHolder {
    private ImageView mAccountImageView;
    private TextView mAccountTextView;
    private TextView mTimeTextView;
    private TextView mDescribtionTextView;
    private ImageView mPhotoImageView;
    private ImageView mPraiseImageView;
    private ImageView mCollectionImageView;

    public DynamicHolder(View itemView) {
        super(itemView);
        mAccountImageView = (ImageView) itemView.findViewById(R.id.dynamic_account_iamge_view);
        mAccountTextView = (TextView) itemView.findViewById(R.id.dynamic_account_text_view);
        mTimeTextView = (TextView) itemView.findViewById(R.id.dynamic_time_text_view);
        mDescribtionTextView = (TextView) itemView.findViewById(R.id.dynamic_description_text_view);
        mPhotoImageView = (ImageView) itemView.findViewById(R.id.dynamic_photo_iamge_view);
        mPraiseImageView = (ImageView) itemView.findViewById(R.id.dynamic_praise_image_view);
        mCollectionImageView = (ImageView) itemView.findViewById(R.id.dynamic_collection_iamge_view);
    }

    public void bindView() {
        mAccountImageView.setImageResource(R.drawable.backtest);
        mAccountTextView.setText("ScorpioMiku");
        mTimeTextView.setText("12:00");
        mDescribtionTextView.setText("这个菜真的棒！");
        mPhotoImageView.setImageResource(R.drawable.food_test_1);
        mPraiseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPraiseImageView.setImageResource(R.drawable.dynamic_clickedpraise);
            }
        });
        mCollectionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectionImageView.setImageResource(R.drawable.dynamic_collected);
            }
        });

    }
}
