package com.scorpiomiku.cookbook.dynamic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.combination.CombinationFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicHolder> {
    private Context mContext;
    private List<String> mList;

    public DynamicAdapter(List<String> list, Context context) {
        super();
        mList = list;
        mContext = context;
    }

    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.dynamic_item_recycler_view, parent, false);
        return new DynamicHolder(v);
    }

    @Override
    public void onBindViewHolder(DynamicHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
