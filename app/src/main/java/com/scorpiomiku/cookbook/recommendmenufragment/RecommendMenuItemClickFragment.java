package com.scorpiomiku.cookbook.recommendmenufragment;

import android.content.Intent;
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
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommend.RecommendDefultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class RecommendMenuItemClickFragment extends FragmentModule {
    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    public List<String> mListFoodNames;
    public List<String> mListFoodMetirals;


    public static RecommendMenuItemClickFragment newInstance() {
        return new RecommendMenuItemClickFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++){
            mList.add("1");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_menu_item_click_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_item_click_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter(mList));
        mRecyclerView.setNestedScrollingEnabled(false);
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
                mImageView.setImageResource(R.drawable.chniurou);

            }
            if (i == 1) {
                mImageView.setImageResource(R.drawable.chlishikaorou);
            }
            if (i == 2) {
                mImageView.setImageResource(R.drawable.chbaoyu);
            }
            if (i == 3) {
                mImageView.setImageResource(R.drawable.chliyu);
            }
            if (i == 4) {
                mImageView.setImageResource(R.drawable.chrouxie);
            }
            if (i == 5) {
                mImageView.setImageResource(R.drawable.chkongqueyu);
            }
            if (i != 1 && i != 0 && i != 2 && i != 3 && i != 4 && i != 5) {
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
            mListFoodNames.add("烤牛肉");
            mListFoodNames.add("李氏烤肉");
            mListFoodNames.add("鲍鱼小炒肉");
            mListFoodNames.add("榄角蒸立鱼");
            mListFoodNames.add("姜葱大肉蟹");
            mListFoodNames.add("清蒸孔雀鱼");
            for (int i = 0 ; i < 10 ; i++){
                mListFoodNames.add("清蒸孔雀鱼");
            }
            mListFoodMetirals = new ArrayList<>();


            mListFoodMetirals.add("牛肉适量、姜片适量、食用油适量、盐适量、胡椒粉适量、孜然粉适量、孜然颗粒适量、辣椒粉或辣椒油适量");
            mListFoodMetirals.add("猪颈背肉适量、蜂蜜适量、葱适量、姜适量、蒜适量、洋葱适量、盐适量、生抽适量");
            mListFoodMetirals.add("青椒四个、鲍鱼三个、猪瘦肉适量、姜丝适量、葱段适量、淀粉适量、鲍鱼汁适量、黑胡椒适量、盐适量、柠檬汁适量、青岛啤酒适量、糖适量");
            mListFoodMetirals.add("立鱼2条、榄角10来粒、姜4片、葱2根、蒸鱼豉油3大匙、盐1/2茶匙、料酒2茶匙、油1大匙");
            mListFoodMetirals.add("加拿大肉蟹2个、姜适量、葱适量、高汤、玉米油2大匙");
            mListFoodMetirals.add("扁鱼一条、辣椒少许、生姜适量、葱适量、盐适量、味精适量、料酒适量");
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.recommend_default_recycler_view_item, parent, false);
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            holder.bindView(position, mListFoodNames.get(position),mListFoodMetirals.get(position));
        }


        @Override
        public int getItemCount() {
            return mStringList.size();
        }
    }
}
