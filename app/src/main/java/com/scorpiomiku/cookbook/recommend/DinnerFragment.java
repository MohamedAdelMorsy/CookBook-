package com.scorpiomiku.cookbook.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class DinnerFragment extends FragmentModule {
    private RecyclerView mRecyclerView;

    public static DinnerFragment newInstance() {
        return new DinnerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_dinner_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_dinner_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setNestedScrollingEnabled(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("1");
        }
        mRecyclerView.setAdapter(new DinnerFragment.Adapter(list));
        return v;
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView ;

        public holder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id
                    .recommend_dinner_item_food_image_view);
            mTextView= (TextView) itemView.findViewById(R.id
                    .recommend_dinner_item_food_name_text_view);
        }

        private void bindView(String s ) {
            mImageView.setImageResource(R.drawable.test_food);
            itemView.setOnClickListener(this);
            mTextView.setText("豌豆炒肉");
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MenuActivity.class);
            startActivity(i);
        }
    }


    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<DinnerFragment.holder> {
        List<String> mStringList;

        public Adapter(List<String> list) {
            mStringList = list;
        }

        @Override
        public DinnerFragment.holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.recommend_dinner_recycler_view_item, parent, false);
            return new DinnerFragment.holder(v);
        }

        @Override
        public void onBindViewHolder(DinnerFragment.holder holder, int position) {
            holder.bindView(mStringList.get(position));
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }
    }
}
