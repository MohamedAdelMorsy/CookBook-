package com.scorpiomiku.cookbook.recommendmenufragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommend.RecommendFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class RecommendMenuFragment extends FragmentModule {

    private RecyclerView mTasteRecyclerView;
    private RecyclerView mKindRecyclerView;
    private RecyclerView mPlaceRecyclerView;

    private MenuAdapter mTasteAdapter;
    private MenuAdapter mKindAdapter;
    private MenuAdapter mPlaceAdapter;

    private ImageView mBackImageView;

    private FragmentManager fm;


    private String[] mTastes = {"酸", "甜", "辣", "酸辣", "酸甜", "微苦"};
    private String[] mKinds = {"素菜", "荤菜", "甜品", "汤粥类", "水果类", "腌制类", "面食", "水产", "豆类"};
    private String[] mPlaces = {"家常", "聚会", "宿舍", "野餐", "宵夜", "生日"};

    public static RecommendMenuFragment newInstance() {
        return new RecommendMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View v = layoutInflater.inflate(R.layout.recommend_menu_fragment, container, false);
        mBackImageView = (ImageView) v.findViewById(R.id.recommend_tool_bar_back_image_view);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .replace(R.id.fragment_container, RecommendFragment.newInstance())
                        .commit();
            }
        });

        mTasteRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_taste_recycler_view);
        mTasteAdapter = new MenuAdapter(Arrays.asList(mTastes), getContext());
        mTasteRecyclerView.setAdapter(mTasteAdapter);
        mTasteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mKindRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_kind_recycler_view);
        mKindAdapter = new MenuAdapter(Arrays.asList(mKinds), getContext());
        mKindRecyclerView.setAdapter(mKindAdapter);
        mKindRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        mPlaceRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_place_recycler_view);
        mPlaceAdapter = new MenuAdapter(Arrays.asList(mPlaces), getContext());
        mPlaceRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        return v;
    }
}
