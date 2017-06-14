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

public class MyCollectionFragment extends FragmentModule {

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView ;
    private List<String>mStringList ;

    public static MyCollectionFragment newInstance(){
        return new MyCollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStringList = new ArrayList<String>();
        for(int i = 10 ; i < 10 ; i ++)
        {
            mStringList.add("我是菜品的描述2");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout
                .colleciont_mycollections_swipe_recyclerview,container,false);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) v.findViewById(R.id
                .collection_my_collection_swipe_recyclerview);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeMenuRecyclerView.setAdapter(new Adapter(mStringList));
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
    private class Adapter extends RecyclerView.Adapter<ItemHolder>{
        private List<String> mList ;

        public Adapter(List<String>list) {
            super();
            mList = list ;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.collection_recycler_view_item,parent,false);
            return new ItemHolder(v) ;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
