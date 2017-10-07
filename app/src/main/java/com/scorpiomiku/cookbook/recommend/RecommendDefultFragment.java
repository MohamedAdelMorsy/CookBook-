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

    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    ;
    private List<String> list = new ArrayList<>();

    private Adapter mAdapter = new Adapter(list);
    boolean isLoading;
    private Handler handler = new Handler();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public List<String> mListFoodNames;
    public List<String> mListFoodMetirals;


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

        private void bindView(int i, String s1,String s2) {
            if (i == 0) {
                mImageView.setImageResource(R.drawable.chroubing);

            }
            if (i == 1) {
                mImageView.setImageResource(R.drawable.chpaigu);
            }
            if (i == 2) {
                mImageView.setImageResource(R.drawable.chtudou);
            }
            if (i == 3) {
                mImageView.setImageResource(R.drawable.chliyu);
            }
            if (i == 4) {
                mImageView.setImageResource(R.drawable.chrouxie);
            }
            if (i != 1 && i != 0 && i != 2 && i != 3 && i != 4) {
                mImageView.setImageResource(R.drawable.food_test_1);
            }
            mFoodNameTextView.setText(s1);
            mFoodMatirialTextView.setText(s2);
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
            mListFoodNames = new ArrayList<>();
            mListFoodNames.add("梅菜蒸肉饼");
            mListFoodNames.add("梅干菜烧排骨");
            mListFoodNames.add("红烧土豆");
            mListFoodNames.add("榄角蒸立鱼");
            mListFoodNames.add("姜葱大肉蟹");
            mListFoodMetirals = new ArrayList<>();


            mListFoodMetirals.add("梅菜、里脊肉适量、葱适量、糖、生抽、料酒");
            mListFoodMetirals.add("排骨1.5磅、梅干菜、半碗水、蒜2粒、姜3片、八角1颗、生抽3大匙、老抽2茶匙、糖3茶匙、油1大匙、料酒1大匙");
            mListFoodMetirals.add("猪瘦肉适量、土豆适量、葱适量、八角适量、老抽适量、姜适量、盐适量");
            mListFoodMetirals.add("立鱼2条、榄角10来粒、姜4片、葱2根、蒸鱼豉油3大匙、盐1/2茶匙、料酒2茶匙、油1大匙");
            mListFoodMetirals.add("加拿大肉蟹2个、姜适量、葱适量、高汤、玉米油2大匙");
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
                holder.bindView(position, mListFoodNames.get(position),mListFoodMetirals.get(position));
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
        if(mAdapter.getItemCount()>150){

        }else{
            for (int i = 0; i < 6; i++) {
                list.add("1");
                mListFoodNames.add("1");
                mListFoodMetirals.add("1");
            }
            mAdapter.notifyDataSetChanged();
            //RecommendFragment.mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyItemRemoved(mAdapter.getItemCount());
        }
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
