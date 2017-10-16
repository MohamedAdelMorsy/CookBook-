package com.scorpiomiku.cookbook.ShowShare.SSadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;

import java.util.List;


/**
 * Created by a on 2017/8/21.
 */


public class SSAdapter extends RecyclerView.Adapter<SSAdapter.MyViewHolder> {


    public interface OnRecyclerViewListener {

        // 点击商家图片
        void onPLImgClick(View view, int position);


    }

    private OnRecyclerViewListener listener;

    private Context mContext;

    private List<CBLEC> mData;

    public void setOnRecyclerViewListener(OnRecyclerViewListener mItemListener) {

        Log.d("SSAdapter加载", "onCreate: 检查点"+" setOnRecyclerViewListener开始");
        this.listener = mItemListener;
        Log.d("SSAdapter加载", "onCreate: 检查点"+" setOnRecyclerViewListener完成");
    }
    public SSAdapter(Context context, List<CBLEC> datas) {
        
        this.mContext = context;
        this.mData = datas;
        Log.d("SSAdapter加载", "onCreate: 检查点"+" 数据初始完成");
    }
    public void setSSAdapter(List<CBLEC> datas){
        mData = datas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SSAdapter加载", "onCreate: 检查点"+"MyViewHolder步骤1");
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_cblec_change, parent, false);
        Log.d("SSAdapter加载", "onCreate: 检查点"+" MyViewHolder步骤2");
        SSAdapter.MyViewHolder myViewHolder = new SSAdapter.MyViewHolder(view);
        Log.d("SSAdapter加载", "onCreate: 检查点"+" MyViewHolder步骤3");
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CBLEC cblec =  mData.get(position);
        holder.NameView.setText(cblec.getName());
        holder.NameView.setTag(cblec.getObjId());
        //图片加载
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher);
        Glide.with(mContext)
                .load(cblec.getImageurl())
                .apply(options)
                .into(holder.CaiPuNameView);

        holder.NeirongView.setText(cblec.getIntroduce());
        holder.ShiCaiMing1View.setText(cblec.getShiCaiMing1());
        holder.ShiCaiMing1View.setTag(cblec.getFromUser());
        holder.ShiCaiMing2View.setText(cblec.getShiCaiMing2());
        holder.ShiCaiMing3View.setText(cblec.getShiCaiMing3());
        holder.ShiCaiMing4View.setText(cblec.getShiCaiMing4());
        holder.ShiCaiMing5View.setText(cblec.getShiCaiMing5());
        holder.ShiCaiMing6View.setText(cblec.getShiCaiMing6());
        holder.ShiCaiMing7View.setText(cblec.getShiCaiMing7());
        holder.ShiCaiMing8View.setText(cblec.getShiCaiMing8());
        holder.ShiCaiMing9View.setText(cblec.getShiCaiMing9());
        holder.ShiCaiLiang1View.setText(cblec.getShiCaiLiang1());
        holder.ShiCaiLiang2View.setText(cblec.getShiCaiLiang2());
        holder.ShiCaiLiang3View.setText(cblec.getShiCaiLiang3());
        holder.ShiCaiLiang4View.setText(cblec.getShiCaiLiang4());
        holder.ShiCaiLiang5View.setText(cblec.getShiCaiLiang5());
        holder.ShiCaiLiang6View.setText(cblec.getShiCaiLiang6());
        holder.ShiCaiLiang7View.setText(cblec.getShiCaiLiang7());
        holder.ShiCaiLiang8View.setText(cblec.getShiCaiLiang8());
        holder.ShiCaiLiang9View.setText(cblec.getShiCaiLiang9());


        PLAdapter pLAdapter = new PLAdapter(mContext, cblec.getSsec());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.productList.setLayoutManager(linearLayoutManager);
        holder.productList.setAdapter(pLAdapter);
        holder.productList.setVisibility(View.VISIBLE);

        Log.d("SSAdapter加载", "onCreate: 检查点"+" onBindViewHolder完成");
        if (listener != null) {
            // 商家图片点击事件
            holder.CaiPuNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPLImgClick(view, position);
                    //String str = ((TextView) view.findViewById(R.id.Share_cblec_Name)).getTag().toString();
                    final CBLEC cblec =  mData.get(position);
                    Log.d("通往菜谱界面", "onClick: "+"点击了adapter");
                    Intent intent= new Intent(mContext, MenuActivity.class);
                    Log.d("通往菜谱界面", "onClick: "+"Intnet载入完毕");
                    //此处出现问题=
                   Log.d("通往菜谱界面", "onClick: "+"Tag获取完毕");
                    intent.putExtra("WayObjId", cblec.getObjId());
                    mContext.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView NameView;
        ImageView CaiPuNameView;
        TextView NeirongView;
         TextView ShiCaiMing1View;
        public TextView ShiCaiMing2View;
        public TextView ShiCaiMing3View;
        public TextView ShiCaiMing4View;
        public TextView ShiCaiMing5View;
        public TextView ShiCaiMing6View;
        public TextView ShiCaiMing7View;
        public TextView ShiCaiMing8View;
        public TextView ShiCaiMing9View;
        public TextView ShiCaiLiang1View;
        public TextView ShiCaiLiang2View;
        public TextView ShiCaiLiang3View;
        public TextView ShiCaiLiang4View;
        public TextView ShiCaiLiang5View;
        public TextView ShiCaiLiang6View;
        public TextView ShiCaiLiang7View;
        public TextView ShiCaiLiang8View;
        public TextView ShiCaiLiang9View;
        RecyclerView productList;

        public MyViewHolder(View itemView) {

            super(itemView);
            Log.d("SSAdapter加载", "onCreate: 检查点"+"布局配置开始");
            CaiPuNameView = (ImageView) itemView.findViewById(R.id.Share_cblec_Image);
            NeirongView = (TextView) itemView.findViewById(R.id.Share_cblec_JieShao);
            NameView = (TextView)itemView.findViewById(R.id.Share_cblec_Name);
            ShiCaiMing1View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing1);
            ShiCaiMing2View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing2);
            ShiCaiMing3View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing3);
            ShiCaiMing4View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing4);
            ShiCaiMing5View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing5);
            ShiCaiMing6View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing6);
            ShiCaiMing7View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing7);
            ShiCaiMing8View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing8);
            ShiCaiMing9View=(TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiMing9);
            ShiCaiLiang1View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang1);
            ShiCaiLiang2View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang2);
            ShiCaiLiang3View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang3);
            ShiCaiLiang4View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang4);
            ShiCaiLiang5View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang5);
            ShiCaiLiang6View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang6);
            ShiCaiLiang7View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang7);
            ShiCaiLiang8View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang8);
            ShiCaiLiang9View = (TextView)itemView.findViewById(R.id.cblec_Share_ShiCaiLiang9);




            productList = (RecyclerView) itemView.findViewById(R.id.share_pl_recyclerview);
            Log.d("SSAdapter加载", "onCreate: 检查点"+"布局配置结束");
        }
    }
}
