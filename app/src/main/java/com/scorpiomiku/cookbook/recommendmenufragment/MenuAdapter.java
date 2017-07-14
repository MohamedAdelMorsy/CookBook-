package com.scorpiomiku.cookbook.recommendmenufragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpiomiku.cookbook.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {
    private List<String> mList;
    private Context mContext;
    private Drawable mClickedDrawable;
    private Drawable mUnClickedDrawable;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuAdapter(List<String> list, Context context) {
        super();
        mList = list;
        mContext = context;
        mClickedDrawable = mContext.getDrawable(R.drawable.recommend_menu_item_circle_clicked);
        mUnClickedDrawable = mContext.getDrawable(R.drawable.recommend_menu_item_circle_unclicked);
    }

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.recommend_menu_recycler_view_item, parent, false);
        return new MenuHolder(v, mUnClickedDrawable, mClickedDrawable);
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
