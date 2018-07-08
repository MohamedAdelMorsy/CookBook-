package com.scorpiomiku.cookbook.collection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.scorpiomiku.cookbook.combination.ZuCaiJieMian;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.APPKEY;

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
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mStringList = new ArrayList<>();



       /* for (int i = 10; i < 20; i++) {
            mStringList.add("我是菜品的描述1");
        }*/
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container
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
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = pref.edit();
        Way_datahelper dbHelper = new Way_datahelper(getActivity(), "way_objectIDStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Way_objectID", null, null, null, null, null, null);
        int jishu = 0;
         List<String> mstring1 = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                jishu++;
                final Way_obj b = new Way_obj(cursor.getString(cursor.getColumnIndex("Way_objectID")));
                Log.d("获取的sql信息", "onCreate: Way_objectID = "+b.getmName());
                if (jishu<=8){
                    urlget(b.getmName());
                    mList.add(b);
                }


                //                if(b.getmName()!=null){
//                    urlget(b.getmName());
//                    mList.add(b);
//                }
//                mList.add(b);
//                mstring1.add(b.getmName());
//                if(jishu%8==0){
//                    final int finalJishu = jishu;
//                    final List<String> mstring = mstring1;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            urlget(mstring.get(finalJishu - 8));
//                            urlget(mstring.get(finalJishu - 7));
//                            urlget(mstring.get(finalJishu - 6));
//                            urlget(mstring.get(finalJishu - 5));
//                            urlget(mstring.get(finalJishu - 4));
//                            urlget(mstring.get(finalJishu - 3));
//                            urlget(mstring.get(finalJishu - 2));
//                            urlget(mstring.get(finalJishu - 1));
//                        }
//                    }).start();
//
//                }


            } while (cursor.moveToNext());
            cursor.close();
        }
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
        return v;
    }
    private void urlget(final String Way_objectID){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;

                url = "http://apis.juhe.cn/cook/queryid?key="+  APPKEY+"&id=" +Way_objectID;



                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url(url).build();
                String date = null;
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    date=response.body().string();
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GetSorce(date);

            }
        }).start();
    }
    private void  GetSorce(String data){
        String ZuoFaTu = null;
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < array.size(); i++) {
            Log.d("数据显示", "***********array.size(): "+array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            try{
                ZuoFaTu = subObject.get("albums").getAsString();
            }catch (Exception e1213){
            }

            //int cishu = subObject.get("cishu").getAsInt();
            final String ZuoFaMing = subObject.get("title").getAsString();
            // final CBLEC cblec = new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false);
            mCblecList.add(new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false));
            mSwipeMenuRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeMenuRecyclerView.setAdapter(new Adapter(mCblecList));
                }
            });
        }
    }
    /*---------------------------------Holder------------------------------*/
    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CBLEC s;
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
            s = mcblec;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(getActivity())
                    .load(mcblec.getImageurl())
                    .apply(options)
                    .into(mItemImageView);
            Log.d("收藏界面", "bindView: 加载图片完成");
            mItemNameTextView.setText(mcblec.getName());
            mItemMatirialTextView.setText(mcblec.getShiCaiMing());
            itemView.setOnClickListener(this);
            /*mItemNameTextView.setText("青菜炒肉");
            mItemImageView.setImageResource(R.drawable.food_test_1);
            itemView.setOnClickListener(this);
            mItemMatirialTextView.setText("青菜、肉、青菜、肉、青菜、肉、青菜、肉、青菜、肉");
        */
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",s.getObjId());
            editor.apply();
            Log.d("zucaijiemian", "onClick: ");
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID",s.getObjId());
            i.putExtra("charge","1");
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
