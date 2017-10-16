package com.scorpiomiku.cookbook.collection;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.bean.Way_obj;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.sql.Way_datahelper;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by Administrator on 2017/6/14.
 */

public class MyRecordsFragment extends FragmentModule {

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private List<Way_obj> mList = new ArrayList<>();
    private List<CBLEC> mCblecList = new ArrayList<>();
    private SwipeMenuAdapter mAdapter;
   // private Way_datahelper dbHelper;
    private Boolean panduan;
    public static MyRecordsFragment newInstance() {
        return new MyRecordsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mStringList = new ArrayList<>();



       /* for (int i = 10; i < 20; i++) {
            mStringList.add("我是菜品的描述1");
        }*/
    }
    private void initWay(String objId){
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId",objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("收藏界面", "测试点5"+"存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            Log.d(TAG, "收藏界面: result："+result);
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("收藏界面", "测试点5"+"查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                try{
                                    panduan = subObject.get("FromUser").getAsBoolean();
                                }catch (Exception a){
                                    panduan = false;
                                }
                                String ZuoFaTu;
                                try{
                                    JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
                                    ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
                                    final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
                                }catch (Exception e1213){
                                    ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                }

                                //获取做法的图片url

                                final String Way_objectID = subObject.get("objectId").getAsString();
                                try {
                                    int cishu = subObject.get("cishu").getAsInt();
                                }catch (Exception d){
                                    int cishu=0;
                                }
                                //int cishu = subObject.get("cishu").getAsInt();
                                final String ZuoFaMing = subObject.get("zuoFaMing").getAsString();
                                final String finalZuoFaTu = ZuoFaTu;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //identification
                                        try {
                                            jas.put("identification",Way_objectID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("收藏界面", "测试点5"+"开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
                                                    Log.d("收藏界面", "测试点5"+"开始查询食材部分result = "+result);
                                                    JsonParser parser = new JsonParser();  //创建JSON解析器
                                                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                                                    for (int i = 0; i <= array.size(); i++) {
                                                        final JsonObject subObject = array.get(i).getAsJsonObject();
                                                        final String yongliao[] = new String[10];
                                                        final String yongliaoliang[] = new String[10];

                                                               /*{"results":
                                                                    [{"createdAt":"2017-07-10 16:11:26",
                                                                        "identification":"49b2867f32","objectId":"65149c467e",
                                                                        "updatedAt":"2017-07-10 16:11:26",
                                                                        "yongLiao1":"","yongLiao2":"",
                                                                        "yongLiao3":"",
                                                                        "yongLiao4":"",
                                                                        "yongLiao5":"","yongLiao6":"","yongLiao7":"","yongLiao8":"","yongLiao9":"","yongLiaoLiang1":"","yongLiaoLiang2":"","yongLiaoLiang3":"","yongLiaoLiang4":"","yongLiaoLiang5":"","yongLiaoLiang6":"","yongLiaoLiang7":"","yongLiaoLiang8":"","yongLiaoLiang9":""}]}
                                                                        "ShiCaiLiang":7,
                                                                        */
                                                        int ShiCaiLiang = 0;
                                                        ShiCaiLiang = 9;
                                                        int ls_panding = 0;
                                                        for (int k = 0; k < ShiCaiLiang; k++) {
                                                            if (k == 0) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao1").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang1").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 1) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao2").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang2").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 2) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao3").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang3").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 3) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao4").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang4").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 4) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao5").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang5").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 5) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao6").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang6").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 6) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao7").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang7").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 7) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao8").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang8").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 8) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao9").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang9").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            } else if (k == 9) {
                                                                yongliao[ls_panding] = subObject.get("yongLiao10").getAsString();
                                                                yongliaoliang[ls_panding] = subObject.get("yongLiaoLiang10").getAsString();
                                                                if(yongliao[ls_panding]!=""){
                                                                    yongliao[ls_panding] = yongliao[ls_panding]+",";
                                                                    yongliaoliang[ls_panding] = yongliaoliang[ls_panding]+";";
                                                                    ls_panding= ls_panding+1;
                                                                }
                                                            }
                                                        }
                                                        for(;ls_panding<10;ls_panding++){
                                                            yongliao[ls_panding] = " ";
                                                            yongliaoliang[ls_panding] = " ";
                                                        }
                                                        Log.d("收藏界面", "测试点5"+"图片加载成功");
                                                        mCblecList.add(new CBLEC(ZuoFaMing, finalZuoFaTu, "这里是介绍", yongliao[0], yongliao[1], yongliao[2], yongliao[3], yongliao[4], yongliao[5], yongliao[6], yongliao[7], yongliao[8], yongliaoliang[0], yongliaoliang[1], yongliaoliang[2], yongliaoliang[3], yongliaoliang[4], yongliaoliang[5], yongliaoliang[6], yongliaoliang[7], yongliaoliang[8],Way_objectID,panduan));
                                                        mSwipeMenuRecyclerView.setAdapter(new Adapter(mCblecList));
                                                        //initView();
                                                        //mContextView.setAdapter(new MenuAdapter(mCblecList));
                                                    }
                                                }
                                            }
                                        });
                                    }

                                }).start();
                            }

                        }
                    }
                });
            }
        }).start();


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout
                .collection_myrecords_swipe_recyclerview, container, false);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) v.findViewById(R.id
                .collection_my_records_swipe_recyclerview);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mAdapter = new Adapter(mCblecList);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
        mSwipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mSwipeMenuRecyclerView.setNestedScrollingEnabled(false);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickLinstener);
        Bmob.initialize(getActivity(), "3bfd53d40a453ea66ce653ab658582d1");
        Way_datahelper dbHelper = new Way_datahelper(getActivity(), "way_objectIDStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Way_objectID", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Way_obj b = new Way_obj(cursor.getString(cursor.getColumnIndex("Way_objectID")));
                Log.d("获取的sql信息", "onCreate: Way_objectID = "+b.getmName());
                initWay(b.getmName());
                mList.add(b);
            } while (cursor.moveToNext());
            cursor.close();
        }
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
        return v;
    }

    /*---------------------------------Holder------------------------------*/
    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mItemImageView;
        private TextView mItemNameTextView;
        private TextView mItemMatirialTextView;

        public ItemHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id
                    .collection_item_image_view);
            mItemNameTextView = (TextView) itemView.findViewById(R.id
                    .collection_item_food_name_text_view);
            mItemMatirialTextView = (TextView) itemView.findViewById(R.id
                    .collection_item_food_matirial_text_view);
        }

        private void bindView(CBLEC mcblec) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(getActivity())
                    .load(mcblec.getImageurl())
                    .apply(options)
                    .into(mItemImageView);
            Log.d("收藏界面", "bindView: 加载图片完成");
            mItemNameTextView.setText(mcblec.getName());
            mItemMatirialTextView.setText(mcblec.getShiCaiMing1()+mcblec.getShiCaiMing2()+mcblec.getShiCaiMing3()+mcblec.getShiCaiMing4()+mcblec.getShiCaiMing5()+mcblec.getShiCaiMing6()+mcblec.getShiCaiMing7()+mcblec.getShiCaiMing8()+mcblec.getShiCaiMing9());
            itemView.setOnClickListener(this);
            /*mItemNameTextView.setText("青菜炒肉");
            mItemImageView.setImageResource(R.drawable.food_test_1);
            itemView.setOnClickListener(this);
            mItemMatirialTextView.setText("青菜、肉、青菜、肉、青菜、肉、青菜、肉、青菜、肉");
        */
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MenuActivity.class);
            startActivity(i);
        }
    }


    /*--------------------------------Adapter-----------------------------*/
    private class Adapter extends SwipeMenuAdapter<ItemHolder> {
        private List<CBLEC> mList;

        public Adapter(List<CBLEC> list) {
            super();
            mList = list;
        }


        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.collection_recycler_view_item, parent, false);
            return v;
        }

        @Override
        public ItemHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            return new MyRecordsFragment.ItemHolder(realContentView);
        }

        @Override
        public void onBindViewHolder(MyRecordsFragment.ItemHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    /*----------------------------------SwipeMenuCreator---------------------------*/
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = ViewGroup.LayoutParams.WRAP_CONTENT;

            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem closeItem = new SwipeMenuItem(getContext())
                    .setBackgroundDrawable(R.drawable.delete_color)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(R.color.colorRed)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem);
        }
    };

    /*--------------------------------------------menuItemClickLitener----------------------------*/
    private OnSwipeMenuItemClickListener menuItemClickLinstener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();
            if (menuPosition == 0) {
                Way_datahelper dbHelper = new Way_datahelper(getActivity(), "way_objectIDStore.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Way_obj b = mList.get(adapterPosition);
                String deletName = b.getmName();
                db.delete("way_objectID", "way_objectID == ?", new String[]{deletName});
               /* mList.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);*/
                mList.remove(adapterPosition);
                mCblecList.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);
                mSwipeMenuRecyclerView.setAdapter(mAdapter);
            }
        }
    };

}
