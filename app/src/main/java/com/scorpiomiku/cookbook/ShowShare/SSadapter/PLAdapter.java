package com.scorpiomiku.cookbook.ShowShare.SSadapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.SSEC;

import java.util.List;

/**
 * Created by a on 2017/8/21.
 */

public class PLAdapter extends RecyclerView.Adapter<PLAdapter.MyViewHolder> {
    DisplayMetrics dm;

    private Context mContext;

    private List<SSEC> mDatas;

    public PLAdapter(Context context, List<SSEC> data) {
        this.mContext = context;
        this.mDatas = data;
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
    @Override
    public PLAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ssl_pl_item, parent, false);
        PLAdapter.MyViewHolder myViewHolder = new PLAdapter.MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(PLAdapter.MyViewHolder holder, int position) {
        SSEC entity = mDatas.get(position);
        holder.SPLName.setText(entity.getShare_name()+":");
        holder.SPLneiRong.setText(entity.getShare_neirong());
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView SPLName;
        TextView SPLneiRong;

        public MyViewHolder(View itemView) {
            super(itemView);
            SPLName = (TextView) itemView.findViewById(R.id.ssl_pl_name);
            SPLneiRong = (TextView) itemView.findViewById(R.id.ssl_pl_nr);

        }
    }

}
