package com.scorpiomiku.cookbook.menuactivity;

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
import com.scorpiomiku.cookbook.bean.MenuStep;
import com.scorpiomiku.cookbook.module.FragmentModule;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MenuRecyclerStepsFragment extends FragmentModule {

    private RecyclerView mRecyclerView;
    private ArrayList<MenuStep> mMenuStepsList;

    public static MenuRecyclerStepsFragment newInstance() {
        return new MenuRecyclerStepsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMenuStepsList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mMenuStepsList.add(new MenuStep("我是第" + i + "步步骤描述","我是营养介绍"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_steps_recycler_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.steps_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new StepsAdapter(mMenuStepsList));
        mRecyclerView.setNestedScrollingEnabled(false);
        return v;
    }

    /*--------------------------------------Adapter-------------------------------------*/

    private class StepsAdapter extends RecyclerView.Adapter<StepsHolder> {

        private ArrayList<MenuStep> mMenuStepsList;
        private View v;
        private final int FOOTERTYPE = 1056992492;

        public StepsAdapter(ArrayList<MenuStep> list) {
            super();
            mMenuStepsList = list;
        }

        @Override
        public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (FOOTERTYPE == viewType) {
                v = layoutInflater.inflate(R.layout.menu_steps_recycler_view_footer_item, parent, false);
            } else {
                v = layoutInflater.inflate(R.layout.menu_steps_recycler_view_default_item, parent, false);
            }
            return new StepsHolder(v);
        }

        @Override
        public void onBindViewHolder(StepsHolder holder, int position) {
            if (position < mMenuStepsList.size()) {
                mMenuStepsList.get(position).setRank(position);
                holder.bindDefaultView(mMenuStepsList.get(position));
            } else {
                holder.bindFooterView(mMenuStepsList.get(position-1));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < mMenuStepsList.size()) {
                return super.getItemViewType(position);
            } else {
                return FOOTERTYPE;
            }
        }

        @Override
        public int getItemCount() {
            return mMenuStepsList.size() + 1;
        }
    }

    /*--------------------------------------holder-------------------------------------*/
    private class StepsHolder extends RecyclerView.ViewHolder {

        private TextView mRankTextView;
        private ImageView mItemImageView;
        private TextView mHowToDoTextView;

        private TextView mNutritionTextView;

        public StepsHolder(View itemView) {
            super(itemView);
            mRankTextView = (TextView) itemView.findViewById(R.id.recycler_view_item_rank_text_view);
            mItemImageView = (ImageView) itemView.findViewById(R.id.menu_recycler_item_steps_iamge_view);
            mHowToDoTextView = (TextView) itemView.findViewById(R.id.menu_recycler_item_steps_text_view);
            mNutritionTextView = (TextView) itemView.findViewById(R.id.menu_recycler_footer_nutrition_text_view);
        }

        private void bindDefaultView(MenuStep m) {
            mRankTextView.setText(m.getRank() + "");
            mHowToDoTextView.setText(m.getStepText());
        }

        private void bindFooterView(MenuStep m) {
            mNutritionTextView.setText(m.getNutrition());
        }

    }


}
