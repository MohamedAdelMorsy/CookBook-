package com.scorpiomiku.cookbook.combination;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.yyydjk.library.HorizontalDropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2017/6/4.
 */

public class CombinationFragment extends FragmentModule {

    private HorizontalDropDownMenu mHorizontalDropDownMenu;
    private TastesAdapter mTastesAdapter;
    private TimeAdapter mTimeAdapter;
    private MakeWayAdapter mMakeWayAdapter;

    private EditText mSearchEditView;
    private ImageView mSearchImageView;

    private String mFoodName;


    private String mHeaders[] = {"口味", "时段", "做法"};
    private String mTastes[] = {"不限", "清淡", "辛辣", "酸甜"};
    private String mTimes[] = {"不限", "早餐", "午餐", "晚餐"};
    private String mMakeWays[] = {"不限", "清蒸", "爆炒", "凉拌"};
    private List<View> mPopupViews = new ArrayList<>();

    private List<String> testList = new ArrayList<>();


    public static CombinationFragment newInstance() {
        return new CombinationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 15; i++) {
            testList.add("");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.combination_fragment_layout, container, false);
        mHorizontalDropDownMenu = (HorizontalDropDownMenu) v.findViewById(R.id.combination_fragment_dropmenu);
        mSearchEditView = (EditText) v.findViewById(R.id.combination_search_edit_view);
        mSearchImageView = (ImageView) v.findViewById(R.id.combination_tool_bar_search_image_view);
        initView();
        return v;
    }


    /*-------------------------------------initView---------------------------------*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        /*----------------------------------------TastesView----------------------------------*/
        RecyclerView mTastesView = new RecyclerView(getContext());
        mTastesAdapter = new TastesAdapter(Arrays.asList(mTastes));
        mTastesView.setAdapter(mTastesAdapter);
        mTastesView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------TimeView----------------------------------*/
        RecyclerView mTimeView = new RecyclerView(getContext());
        mTimeAdapter = new TimeAdapter(Arrays.asList(mTimes));
        mTimeView.setAdapter(mTimeAdapter);
        mTimeView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------MakeWayView----------------------------------*/
        RecyclerView mMakeWayView = new RecyclerView(getContext());
        mMakeWayAdapter = new MakeWayAdapter(Arrays.asList(mMakeWays));
        mMakeWayView.setAdapter(mMakeWayAdapter);
        mMakeWayView.setLayoutManager(new LinearLayoutManager(getContext()));



        /*-----------------initView-----------------*/
        mPopupViews.add(mTastesView);
        mPopupViews.add(mTimeView);
        mPopupViews.add(mMakeWayView);

        /*-------------contextView-------------*/
        RecyclerView mContextView = new RecyclerView(getContext());
        mContextView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContextView.setAdapter(new MenuAdapter(testList));
        mContextView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContextView.setNestedScrollingEnabled(false);

        Drawable[] mDrawables = new Drawable[]{
                getResources().getDrawable(R.drawable.combination_taste_ico, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_time_ico_24dp, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_makeway_ico, getActivity().getTheme())
        };
        mHorizontalDropDownMenu.setDropDownMenu(Arrays.asList(mHeaders), mPopupViews, mContextView
                , mDrawables
                , getResources().getDrawable(R.drawable.backtest, getActivity().getTheme()));


        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFoodName = mSearchEditView.getText().toString();
                Intent i = new Intent(getActivity(), ClassifierResultActivity.class);
                i.putExtra("FragmentSendMessage", "CombinationFragment");
                i.putExtra("foodname", mFoodName);
                mSearchEditView.setText("");
                startActivity(i);

            }
        });
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

    /*------------------------------------------------MakeWayholder------------------------------------*/
    private class MakeWayHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MakeWayHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_makeway_item_test_view);
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


    /*------------------------------------------TimeAdapter-------------------------------------*/
    private class TimeAdapter extends RecyclerView.Adapter<TimeHolder> {

        private List<String> mStringList;

        public TimeAdapter(List<String> list) {
            mStringList = list;
        }

        @Override
        public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
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


    /*--------------------------------------------TasterAdapter----------------------------------*/
    private class TastesAdapter extends RecyclerView.Adapter<TasteHolder> {

        private List<String> mStringlist;

        public TastesAdapter(List<String> list) {
            super();
            mStringlist = list;
        }

        @Override
        public TasteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
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


    /*---------------------------------------MenuAdapter----------------------------*/
    private class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        private List<String> mList;

        public MenuAdapter(List<String> l) {
            super();
            mList = l;
        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combination_menu_recycler_view_item, parent, false);
            return new MenuHolder(v);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.bindView();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    /*----------------------------------------MenuHolder----------------------------*/
    private class MenuHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mNameTextView;
        private TextView mMatirialTextView;

        public MenuHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.combination_item_image_view);
            mNameTextView = (TextView) itemView.findViewById(R.id.combination_item_food_name_text_view1);
            mMatirialTextView = (TextView) itemView.findViewById(R.id.combination_item_food_matirial_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MenuActivity.class);
                    startActivity(i);
                }
            });
        }

        private void bindView() {
            mImageView.setImageResource(R.drawable.food_test_1);
            mNameTextView.setText("豌豆炒肉");
            mMatirialTextView.setText("豌豆。豌豆。豌豆。豌豆。豌豆。豌豆。豌豆。");
        }
    }
}
