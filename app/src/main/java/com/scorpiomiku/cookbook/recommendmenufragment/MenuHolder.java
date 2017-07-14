package com.scorpiomiku.cookbook.recommendmenufragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuHolder extends RecyclerView.ViewHolder {
    private TextView mTextView;
    private boolean isClicked = false;
    private Drawable mClickedDrawable;
    private Drawable mUnClickedDrawable;

    public MenuHolder(View itemView, final Drawable unClicked, final Drawable clicked) {
        super(itemView);
        mClickedDrawable = clicked;
        mUnClickedDrawable = unClicked;
        mTextView = (TextView) itemView.findViewById(R.id.recommend_menu_text_view);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    mTextView.setBackground(clicked);
                    isClicked = true ;
                }else{
                    mTextView.setBackground(unClicked);
                    isClicked = false ;
                }
            }
        });
    }

    public void bindView(String s) {
        mTextView.setText(s);
    }
}
