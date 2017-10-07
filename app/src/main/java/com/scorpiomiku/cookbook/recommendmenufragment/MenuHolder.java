package com.scorpiomiku.cookbook.recommendmenufragment;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuHolder extends RecyclerView.ViewHolder {

    private Button mButton;


    public MenuHolder(View itemView, final FragmentManager fragmentManager) {
        super(itemView);
        mButton = (Button) itemView.findViewById(R.id.recommend_menu_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .replace(R.id.fragment_container,RecommendMenuItemClickFragment.newInstance())
                        .commit();
            }
        });

    }

    public void bindView(String s) {
        mButton.setText(s);
    }
}
