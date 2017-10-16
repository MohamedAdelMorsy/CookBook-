package com.scorpiomiku.cookbook.recommendmenufragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.scorpiomiku.cookbook.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuHolder extends RecyclerView.ViewHolder {

    private Button mButton;
    public static String menusme;
    public static int charge  = 1;
    public MenuHolder(View itemView, final FragmentManager fragmentManager) {
        super(itemView);
        mButton = (Button) itemView.findViewById(R.id.recommend_menu_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menusme = mButton.getText().toString();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .replace(R.id.fragment_container,RecommendMenuItemClickFragment.newInstance())
                        .commit();
                //menusme = mButton.getText().toString();
                Log.d("标签界面", "onClick: mButton.getText().toString()"+mButton.getText().toString());
            }
        });
        //menusme = mButton.getText().toString();
        //Log.d("标签界面", "onCreate: mButton.getText().toString()"+mButton.getText().toString());
    }

    public void bindView(String s) {
        mButton.setText(s);

    }
}
