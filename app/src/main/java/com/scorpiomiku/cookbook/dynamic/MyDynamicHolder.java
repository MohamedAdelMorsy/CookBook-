package com.scorpiomiku.cookbook.dynamic;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.ShowShare.SSadapter.PLAdapter;
import com.scorpiomiku.cookbook.bean.CBLEC;

/**
 * Created by a on 2017/10/5.
 */

public class MyDynamicHolder extends RecyclerView.ViewHolder {
    private ImageView mAccountImageView;
    private TextView mAccountTextView;
    private TextView mTimeTextView;
    private TextView mDescribtionTextView;
    private ImageView mPhotoImageView;
    private ImageView mPraiseImageView;
    private ImageView mCollectionImageView;
    private RecyclerView productList;
    private Context mContext;
    private CBLEC cblec1;
    public MyDynamicHolder(View itemView) {
        super(itemView);
        mAccountImageView = (ImageView) itemView.findViewById(R.id.dynamic_account_iamge_view);
        mAccountTextView = (TextView) itemView.findViewById(R.id.dynamic_account_text_view);
        mTimeTextView = (TextView) itemView.findViewById(R.id.dynamic_time_text_view);
        mDescribtionTextView = (TextView) itemView.findViewById(R.id.dynamic_description_text_view);
        mPhotoImageView = (ImageView) itemView.findViewById(R.id.dynamic_photo_iamge_view);
        mPraiseImageView = (ImageView) itemView.findViewById(R.id.dynamic_praise_image_view);
        mCollectionImageView = (ImageView) itemView.findViewById(R.id.dynamic_collection_iamge_view);
        productList = (RecyclerView) itemView.findViewById(R.id.share_pl_recyclerview);
    }
    public void bindView(CBLEC cblec,Context s) {
        mContext = s;
        cblec1 = cblec;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher);
        Glide.with(mContext)
                .load(cblec.getImageurl())
                .apply(options)
                .into(mPhotoImageView);
        mAccountImageView.setImageResource(R.drawable.backtest);
        mAccountTextView.setText(cblec.getName());
        mTimeTextView.setText("12:00");
        mDescribtionTextView.setText("cblec.getIntroduce()");
        //mPhotoImageView.setImageResource(R.drawable.food_test_1);
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
        PLAdapter pLAdapter = new PLAdapter(mContext, cblec.getSsec());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        productList.setLayoutManager(linearLayoutManager);
        productList.setAdapter(pLAdapter);
        productList.setVisibility(View.VISIBLE);
    }
}
