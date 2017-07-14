package com.scorpiomiku.cookbook.recommendmenufragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuHolder extends RecyclerView.ViewHolder {
    private TextView mTextView;

    public MenuHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.recommend_menu_text_view);
    }
    public void bindView(String s){
        mTextView.setText(s);
    }
}
