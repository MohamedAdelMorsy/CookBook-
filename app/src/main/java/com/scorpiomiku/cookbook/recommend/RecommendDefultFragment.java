package com.scorpiomiku.cookbook.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by Administrator on 2017/6/5.
 */

public class RecommendDefultFragment extends FragmentModule {

    private LinearLayoutManager mLinearLayoutManager= new LinearLayoutManager(getContext()); ;
    private List<String> list = new ArrayList<>();

    private Adapter mAdapter = new Adapter(list);
    boolean isLoading;
    private Handler handler = new Handler();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public static RecommendDefultFragment newInstance() {
        return new RecommendDefultFragment();
    }

    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_default_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_default_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        initData();
        setRefresh();
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mFoodNameTextView;
        private TextView mFoodMatirialTextView;
        private View mWholeView;

        public holder(View itemView) {
            super(itemView);
            mWholeView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id
                    .recommend_default_item_image_button);
            mFoodMatirialTextView = (TextView) itemView.findViewById(R.id
                    .recommend_default_item_food_matirial_text_view);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id
                    .recommend_default_item_food_name_text_view);
        }

        private void bindView(String s) {
            mImageView.setImageResource(R.drawable.test_food);
            mFoodNameTextView.setText("豌豆炒肉");
            mFoodMatirialTextView.setText("豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、肉" +
                    "豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、豌豆、");
            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MenuActivity.class);
            startActivity(i);
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
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout
                        .recommend_default_recycler_view_item, parent, false);
                return new holder(v);
            } else if (viewType == TYPE_FOOTER) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout
                        .item_foot, parent, false);
                return new holder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {

            if (position + 1 == getItemCount()) {

            } else {
                holder.bindView(mStringList.get(position));
            }
        }


        @Override
        public int getItemCount() {
            return mStringList.size();
        }
        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    private void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);
    }

    private void getData() {
        for (int i = 0; i < 6; i++) {
            list.add("1");
        }
        mAdapter.notifyDataSetChanged();
        //RecommendFragment.mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
    }
    private void setRefresh() {
        /*RecommendFragment.mSwipeRefreshLayout.setColorSchemeResources(R.color.toolbar_and_menu_color);
        RecommendFragment.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                RecommendFragment.mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        RecommendFragment.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: clear");
                        list.clear();
                        getData();
                    }
                }, 2000);
            }
        });*/
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "在滑动");
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                Log.d("test", "onScrolled:最后一个可见的位子 " + lastVisibleItemPosition);
                Log.d("test", "onScrolled: adapter" + mAdapter.getItemCount());
                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    Log.d("test", "loading executed在加载新的");

                    /*boolean isRefreshing = RecommendFragment.mSwipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                        return;
                    }*/
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }
}
