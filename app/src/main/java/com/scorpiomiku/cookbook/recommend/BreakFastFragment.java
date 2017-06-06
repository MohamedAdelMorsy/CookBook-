package com.scorpiomiku.cookbook.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class BreakFastFragment extends FragmentModule {

    private RecyclerView mRecyclerView;

    public static BreakFastFragment newInstance() {
        return new BreakFastFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_breakfast_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_breakfast_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setNestedScrollingEnabled(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("1");
        }
        mRecyclerView.setAdapter(new Adapter(list));
        return v;
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder {
        private ImageButton mImageButton;

        public holder(View itemView) {
            super(itemView);
            mImageButton = (ImageButton) itemView.findViewById(R.id
                    .recommend_breakfast_item_iamge_button);
        }

        private void bindView(String s ) {
            mImageButton.setImageResource(R.drawable.ic_home_black_24dp);
        }
    }


    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<holder> {
        List<String> mStringList;

        public Adapter(List<String> list) {
            mStringList = list;
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.recommend_breakfast_recycler_view_item, parent, false);
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            holder.bindView(mStringList.get(position));
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }
    }

}
