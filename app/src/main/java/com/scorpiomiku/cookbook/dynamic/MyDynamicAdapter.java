package com.scorpiomiku.cookbook.dynamic;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;

import java.util.List;

/**
 * Created by a on 2017/10/5.
 */

public class MyDynamicAdapter extends RecyclerView.Adapter<MyDynamicHolder> {
private Context mContext;
private List<CBLEC> mList;

public MyDynamicAdapter(List<CBLEC> list, Context context) {
        super();
        mList = list;
        mContext = context;
        }

@Override
public MyDynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.dynamic_item_recycler_view, parent, false);
        return new MyDynamicHolder(v);

        }

@Override
public void onBindViewHolder(MyDynamicHolder holder, int position) {

        holder.bindView(mList.get(position),mContext);

        }

@Override
public int getItemCount() {
        return mList.size();
        }
}