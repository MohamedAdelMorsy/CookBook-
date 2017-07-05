package com.scorpiomiku.cookbook.combination;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.yyydjk.library.HorizontalDropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/6/4.
 */

public class CombinationFragment extends FragmentModule {

    private HorizontalDropDownMenu mHorizontalDropDownMenu;
    private TastesAdapter mTastesAdapter;
    private TimeAdapter mTimeAdapter;
    private MakeWayAdapter mMakeWayAdapter;


    private String mHeaders[] = {"口味", "时段", "做法"};
    private String mTastes[] = {"不限", "清淡", "辛辣", "酸甜"};
    private String mTimes[] = {"不限", "早餐", "午餐", "晚餐"};
    private String mMakeWays[] = {"不限", "清蒸", "爆炒", "凉拌"};
    private List<View> mPopupViews = new ArrayList<>();


    public static CombinationFragment newInstance() {
        return new CombinationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.combination_fragment_layout, container, false);
        mHorizontalDropDownMenu = (HorizontalDropDownMenu) v.findViewById(R.id.combination_fragment_dropmenu);
        initView();
        return v;
    }


    /*-------------------------------------initView---------------------------------*/
    private void initView() {

        /*----------------------------------------TastesView----------------------------------*/
        RecyclerView mTastesView = new RecyclerView(getContext());
        mTastesAdapter = new TastesAdapter(Arrays.asList(mTastes), getContext());
        mTastesView.setAdapter(mTastesAdapter);
        mTastesView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------TimeView----------------------------------*/
        RecyclerView mTimeView = new RecyclerView(getContext());
        mTimeAdapter = new TimeAdapter(Arrays.asList(mTimes), getContext());
        mTimeView.setAdapter(mTimeAdapter);
        mTimeView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------MakeWayView----------------------------------*/
        RecyclerView mMakeWayView = new RecyclerView(getContext());
        mMakeWayAdapter = new MakeWayAdapter(Arrays.asList(mMakeWays));
        mTimeView.setAdapter(mMakeWayAdapter);
        mTimeView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*-----------------initView-----------------*/
        mPopupViews.add(mTastesView);
        mPopupViews.add(mTimeView);
        mPopupViews.add(mMakeWayView);


    }


    /*---------------------------------------------MakeWayAdapter-------------------------------------*/
    private class MakeWayAdapter extends RecyclerView.Adapter<MakeWayHolder> {

        private List<String> mStringlist;


        public MakeWayAdapter(List<String> list) {
            super();
            mStringlist = list;
        }


        @Override
        public MakeWayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combination_make_way_item, parent, false);
            return new MakeWayHolder(v);
        }

        @Override
        public void onBindViewHolder(MakeWayHolder holder, int position) {
            holder.bindView(mStringlist.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringlist.size();
        }

    }

    /*------------------------------------------TimeAdapter-------------------------------------*/
    public class TimeAdapter extends RecyclerView.Adapter<TimeHolder> {


        private List<String> mStringList;
        private Context mContext;


        public TimeAdapter(List<String> list, Context c) {
            mStringList = list;
            mContext = c;
        }

        @Override
        public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View v = layoutInflater.inflate(R.layout.combination_time_item, parent, false);
            return new TimeHolder(v);
        }

        @Override
        public void onBindViewHolder(TimeHolder holder, int position) {
            holder.bindView(mStringList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }


    }


    /*--------------------------------------------TasterAdapter----------------------------------*/
    private class TastesAdapter extends RecyclerView.Adapter<TasteHolder> {

        private List<String> mStringlist;
        private Context mContext;

        public TastesAdapter(List<String> list, Context c) {
            super();
            mStringlist = list;
            mContext = c;
        }

        @Override
        public TasteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View v = layoutInflater.inflate(R.layout.combination_tastes_item, parent, false);
            return new TasteHolder(v);
        }

        @Override
        public void onBindViewHolder(TasteHolder holder, int position) {
            holder.bindView(mStringlist.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringlist.size();
        }

    }


    /*------------------------------------------------MakeWayholder------------------------------------*/
    private class MakeWayHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MakeWayHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_taste_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mMakeWays[position]);
                    mHorizontalDropDownMenu.closeMenu();
                }
            });
        }
    }

    /*-----------------------------------TimeHolder-------------------------*/
    private class TimeHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TimeHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_time_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTimes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                }
            });
        }
    }

    /*------------------------------------------Tasteholder------------------------*/
    private class TasteHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public TasteHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_taste_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTastes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                }
            });

        }
    }
}
