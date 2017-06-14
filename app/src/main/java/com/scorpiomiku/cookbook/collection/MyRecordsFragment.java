package com.scorpiomiku.cookbook.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class MyRecordsFragment extends FragmentModule {

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView ;
    private List<String> mStringList ;

    public static MyRecordsFragment newInstance(){
        return new MyRecordsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStringList = new ArrayList<>();
        for(int i = 10 ; i < 10 ; i ++)
        {
            mStringList.add("我是菜品的描述1");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout
                .collection_myrecords_swipe_recyclerview,container,false);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) v.findViewById(R.id
                .collection_my_records_swipe_recyclerview);
        mSwipeMenuRecyclerView.setAdapter(new Adapter(mStringList));
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v ;
    }
    /*---------------------------------Holder------------------------------*/
    private class ItemHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImageView ;
        private TextView mItemTextView ;

        public ItemHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id
                    .collection_recycler_view_item_iamge_view);
            mItemTextView = (TextView) itemView.findViewById(R.id
                    .collection_recycler_view_item_text_view);
        }

        private void bindView(String text){
            mItemTextView.setText(text);
            mItemImageView.setImageResource(R.mipmap.ic_launcher_round);
        }
    }


    /*--------------------------------Adapter-----------------------------*/
    private class Adapter extends RecyclerView.Adapter<MyRecordsFragment.ItemHolder>{
        private List<String> mList ;

        public Adapter(List<String> list) {
            super();
            mList = list ;
        }

        @Override
        public MyRecordsFragment.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.collection_recycler_view_item,parent,false);
            return new MyRecordsFragment.ItemHolder(v) ;
        }

        @Override
        public void onBindViewHolder(MyRecordsFragment.ItemHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
