package com.scorpiomiku.cookbook.combination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CaiZuEC;
import com.scorpiomiku.cookbook.bean.CaiZuEC_Gai;
import com.scorpiomiku.cookbook.bean.ZuCai;
import com.scorpiomiku.cookbook.menuactivity.DefaultFragment;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommend.BreakFastFragment;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuItemClickFragment;
import com.yyydjk.library.HorizontalDropDownMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.URL;


/**
 * Created by Administrator on 2017/6/4.
 */

public class CombinationFragment extends FragmentModule {

    private HorizontalDropDownMenu mHorizontalDropDownMenu;
    private TastesAdapter mTastesAdapter;
    private TimeAdapter mTimeAdapter;
    private MakeWayAdapter mMakeWayAdapter;
    private RecyclerView mContextView;
    public static String SouSuoNeiRong = "2948692387";
    private String neirong;
    private int geshu=0;

    private Boolean panduan ;
    private ImageView mSearchImageView;
    private String mFoodName;
    private EditText mSearchEditView;

//    public static final String APPKEY = "0d6cb9431d04bba78300b0227867d48c";// 你的appkey
    public static final String APPKEY = "ab2bca01d0bce2ce2be8bbf93b54caa1";// 你的appkey
     static int cid = 37;
    private int tpyecid = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String date = null;
    private    int pn = 0;

    private String mHeaders[] = {"口味", "时段", "做法"};
    private String mTastes[] = {"不限", "清淡", "辛辣", "酸甜"};
    private String mTimes[] = {"不限", "早餐", "午餐", "晚餐"};
    private String mMakeWays[] = {"不限", "清蒸", "爆炒", "凉拌"};
    private List<View> mPopupViews = new ArrayList<>();

    private List<CaiZuEC_Gai> mCblecList = new ArrayList<>();


    public static CombinationFragment newInstance() {
        return new CombinationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
       /* for (int i = 0; i < 15; i++) {
            testList.add("");
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.combination_fragment_layout, container, false);
        mHorizontalDropDownMenu = (HorizontalDropDownMenu) v.findViewById(R.id.combination_fragment_dropmenu);
        mSearchImageView = (ImageView) v.findViewById(R.id.combination_tool_bar_search_image_view);
        mSearchEditView = (EditText) v.findViewById(R.id.combination_search_edit_view);

        initView();

        mContextView = new RecyclerView(getContext());
        mContextView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mContextView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContextView.setNestedScrollingEnabled(false);

        Drawable[] mDrawables = new Drawable[]{
                getResources().getDrawable(R.drawable.combination_taste_ico, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_time_ico_24dp, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_makeway_ico, getActivity().getTheme())
        };
        mHorizontalDropDownMenu.setDropDownMenu(Arrays.asList(mHeaders), mPopupViews, mContextView
                , mDrawables
                , getResources().getDrawable(R.drawable.swipe_background, getActivity().getTheme()));
        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFoodName = mSearchEditView.getText().toString();
                if (!mFoodName.equals("")) {
                    SouSuoNeiRong = mFoodName;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .replace(R.id.fragment_container, RecommendMenuItemClickFragment.newInstance())
                            .commit();
                    mSearchEditView.setText("");

                } else {
                    Toast.makeText(getActivity(), "请输入内容哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
        urlget(44, 88);
        return v;
    }
    private void urlget(final int cid1, final int cid2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url1 = null;
                if (tpyecid == 0){
                    url1 = URL +  APPKEY+"&cid="+cid1+"&rn=20";
                }else {
                    url1 = URL +  APPKEY+"&cid="+cid1+"&pn="+pn+"&rn=20";
                    tpyecid = 0;
                }
                String url2 = null;
                if (tpyecid == 0){
                    url2 = URL +  APPKEY+"&cid="+cid2+"&rn=20";
                }else {
                    url2 = URL +  APPKEY+"&cid="+cid2+"&pn="+pn+"&rn=20";
                    tpyecid = 0;
                }

                String data1 = null,data2 = null;
                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url(url1).build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                     data1=response.body().string();
                    Log.d("组菜界面：", "run: "+data1);
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //服务器返回的地址
                 request=new Request.Builder()
                        .url(url2).build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                     data2=response.body().string();
                    Log.d("组菜界面：", "run: "+data2);
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GetSorce(data1,data2);
            }
        }).start();
    }
    private void  GetSorce(String data1,String data2){
        String ZuoFaTu1 = null,ZuoFaTu2 = null;
        String name1= null,name2= null;
        String shicai1= null,shicai2= null;
        String id1 = null,id2=null;
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data1);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        ssobject = (JsonObject) parser.parse(data2);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei11 = ssobject.get("result").getAsJsonObject();

        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        final JsonArray array1 = nei11.get("data").getAsJsonArray();    //得到为json的数组
        if (array.size()<array1.size()){
            geshu = array.size();
        }else  geshu = array1.size();
        for (int i = 0; i < geshu; i++) {
            Log.d("组菜界面", "***********array.size(): "+array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            try{
                ZuoFaTu1 = subObject.get("albums").getAsString();
            }catch (Exception e1213){
            }
            Log.d("组菜界面", "检查点1");
            //int cishu = subObject.get("cishu").getAsInt();
            name1 = subObject.get("title").getAsString();
            shicai1 =  subObject.get("ingredients").getAsString();
            id1 = subObject.get("id").getAsString();
            Log.d("组菜界面", "***********array1.size(): "+array1.size());


            final JsonObject subObject1 = array1.get(finalI).getAsJsonObject();
            try{
                ZuoFaTu2 = subObject1.get("albums").getAsString();
            }catch (Exception e1213){
            }
            Log.d("组菜界面", "检查点1");
            //int cishu = subObject.get("cishu").getAsInt();
            name2 = subObject1.get("title").getAsString();
            shicai2 =  subObject1.get("ingredients").getAsString();
            id2 = subObject1.get("id").getAsString();
            Log.d("组菜界面", "检查点_开始导入");
            Log.d("组菜界面", "CaiZuEC_Gai:"+"name1:"+name1+"name2"+name2+"ZuoFaTu1"+ZuoFaTu1+"ZuoFaTu2"+ZuoFaTu1);
            mCblecList.add(new CaiZuEC_Gai(name1, ZuoFaTu1,ZuoFaTu2,ZuoFaTu1,ZuoFaTu1,"jieshao", shicai1,shicai2,id1,id2,id1,id2,false));
            Log.d("组菜界面", "检查点2");

            mContextView.post(new Runnable() {
                @Override
                public void run() {
                    mContextView.setAdapter(new MenuAdapter(mCblecList));
                }
            });
        }

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
            neirong=s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mMakeWays[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("做法")) {
                        mCblecList.clear();
                        urlget(40,70);
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("清蒸")) {
                        mCblecList.clear();
                        urlget(47,111);
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("爆炒")) {
                        mCblecList.clear();
                        urlget(46,173);
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("凉拌")) {
                        mCblecList.clear();
                        urlget(5,85);
                    }
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
            neirong = s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTimes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("时段")) {
                        mCblecList.clear();
                        ///all
                        urlget(57,66);
                    }
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("早餐")) {
                        mCblecList.clear();
                        cid = 37;
                        urlget(37,65);
                    }
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("午餐")) {
                        mCblecList.clear();
                        urlget(38,64);
                    }if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("晚餐")) {
                        mCblecList.clear();
                        urlget(40,70);
                        Log.d("组菜界面", "onClick: mTimes"+mTimes[position]);

                    }

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
            neirong = s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTastes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("口味")){
                        mCblecList.clear();
                        urlget(84,67);
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("清淡")){
                        mCblecList.clear();
                        urlget(83,85);
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("辛辣")){
                        mCblecList.clear();
                        urlget(54,83);
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("酸甜")){
                        mCblecList.clear();
                        urlget(52,53);
                    }

                }
            });

        }
    }


    /*---------------------------------------MenuAdapter----------------------------*/
    private class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        private List<CaiZuEC_Gai> mList;

        public MenuAdapter(List<CaiZuEC_Gai> l) {
            super();
            Log.d("组菜界面", "MenuAdapter");
            mList = l;
            Log.d("组菜界面", "mList = l");
        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("组菜界面", "onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combinstion_menu_recycler_view_item_change, parent, false);
            return new MenuHolder(v);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            Log.d("组菜界面", "onBindViewHolder");
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            Log.d("组菜界面", "getItemCount");
            return mList.size();
        }
    }

    /*----------------------------------------MenuHolder----------------------------*/
    private class MenuHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView1;
        private ImageView mImageView2;
        //private ImageView mImageView3;
        //private ImageView mImageView4;
        private TextView mNameTextView;
        private TextView mMatirialTextView;
        private CaiZuEC_Gai a213;
        public MenuHolder(View itemView) {
            super(itemView);
            Log.d("组菜界面", "MenuHolder");
            mImageView1 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_1);
            mImageView2 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_2);
            //mImageView3 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_3);
            //mImageView4 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_4);
            mNameTextView = (TextView) itemView.findViewById(R.id.combination_item_food_name_text_name);
            Log.d("组菜界面", "检查点u");
            mMatirialTextView = (TextView) itemView.findViewById(R.id.combination_item_food_matirial_text_shicai);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ZuCaiJieMian.class);
                    Log.d("Way_objectID", "onClick: "+a213.getObjId());
                    i.putExtra("CZID1",a213.getObjId1());
                    i.putExtra("CZID2",a213.getObjId2());
                    i.putExtra("CZID3",a213.getObjId3());
                    i.putExtra("CZID4",a213.getObjId4());
                    startActivity(i);
                }
            });
        }
        private void bindView(CaiZuEC_Gai mcblec){
            Log.d("组菜界面", "bindView:准备添加 ");
            a213=mcblec;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test);
            Glide.with(getActivity())
                    .load(mcblec.getImageurl1())
                    .apply(options)
                    .into(mImageView1);
            Glide.with(getActivity())
                        .load(mcblec.getImageurl2())
                        .apply(options)
                        .into(mImageView2);
            /*if(mcblec.getImageurl3()!=null){
                Glide.with(getActivity())
                        .load(mcblec.getImageurl3())
                        .apply(options)
                        .into(mImageView3);
            }else{
            }
            if(mcblec.getImageurl4()!=null){
                Glide.with(getActivity())
                        .load(mcblec.getImageurl4())
                        .apply(options)
                        .into(mImageView4);
            }else{
            }*/

            //mImageView.setImageResource(R.drawable.food_test_1);
            mNameTextView.setText(mcblec.getName());
            mMatirialTextView.setText(mcblec.getShiCai1()+mcblec.getShiCai2());
        }

    }
}
