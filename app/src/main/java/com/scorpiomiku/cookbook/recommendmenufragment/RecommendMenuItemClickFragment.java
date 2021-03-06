package com.scorpiomiku.cookbook.recommendmenufragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.scorpiomiku.cookbook.bean.Way_ShiCai;
import com.scorpiomiku.cookbook.combination.CombinationFragment;
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.combination.ZuCaiJieMian;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.APPKEY;

/**
 * Created by Administrator on 2017/7/14.
 */

public class RecommendMenuItemClickFragment extends FragmentModule {
    private RecyclerView mRecyclerView;
    //private List<String> mList = new ArrayList<>();
    private List<CBLEC> testList = new ArrayList<>();
    private int pn;
    private int t=0;
    private int id = 0;
    private Boolean panduan;
    //private String ZuoFaTu;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static RecommendMenuItemClickFragment newInstance() {
        return new RecommendMenuItemClickFragment();
    }
    String s = CombinationFragment.SouSuoNeiRong;
    String changeby = MenuHolder.menusme;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void urlget(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {


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
            testList.add(new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false));
            Log.d("搜索界面", "GetSorce: ID："+subObject.get("id").getAsString());

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setAdapter(new Adapter(testList));
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_menu_item_click_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_item_click_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter(testList));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = pref.edit();
        if (s == "2948692387") {
            CombinationFragment.SouSuoNeiRong = "2948692387";
            s = changeby;
            Log.d("标签界面", "onCreate: MenuHolder.menusme" + s);
            List<BmobQuery<Way>> queries = new ArrayList<BmobQuery<Way>>();
            if (s == "酸" || s == "甜" || s == "辣" || s == "酸辣" || s == "酸甜" || s == "微苦") {
                if(s=="酸"){
                    id = 52;
                }
                if(s=="甜"){
                    id = 53;
                }
                if(s=="辣"){
                    id= 54;
                }
                if(s=="酸甜"){
                    id= 334;
                }
                if(s=="苦"){
                    id =331;
                }
                if(s==""){

                }
                String url = "http://apis.juhe.cn/cook/index?key="+APPKEY+"&cid="+id;
                urlget(url);
            }
         else if (s == "素菜" || s == "荤菜" || s == "甜品" || s == "汤粥类" || s == "水果类" || s == "腌制类" || s == "面食" || s == "水产" || s == "豆类") {
            if(s=="素菜"){
                id = 61;
            }if(s=="荤菜"){
                id = 182;
            }if(s=="甜品"){
                id=337;
            }if(s=="汤粥类"){
                id = 82;
            }if(s=="水果类"){
                id = 219;
            }if(s=="腌制类"){
                id = 308;
            }if(s=="面食"){
                id = 7;

            }if(s=="水产"){
                id = 201;
            }if(s=="豆类"){

            }
            String url = "http://apis.juhe.cn/cook/index?key="+APPKEY+"&cid="+id;
            urlget(url);
         }
         else if (s == "家常" || s == "聚会" || s == "宿舍" || s == "约会" || s == "宵夜" || s == "" || s == "春节") {
            if(s=="家常"){
                id = 1;
            }if(s=="聚会"){
                id = 241;
            }if(s=="宿舍"){
                id = 243;
            }if(s=="约会"){
                id =244;
            }if(s=="宵夜"){
                id = 242;
            }if (s=="春节"){
                id =261;
            }
            String url = "http://apis.juhe.cn/cook/index?key="+APPKEY+"&cid="+id;
            urlget(url);
         }
        }else{
            CombinationFragment.SouSuoNeiRong="2948692387";
            String str = s;  /*为传入的要搜索的食材名*/
            String url = null;
            try {
                url = "http://apis.juhe.cn/cook/query?key="+APPKEY+ "&menu="+URLEncoder.encode(s, "utf-8")+"&rn=20"+"&pn="+pn;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            urlget(url);
        }

      //  mRecyclerView.setAdapter(new Adapter(testList));
        return v;
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mFoodNameTextView;
        private TextView mFoodMatirialTextView;
        private View mWholeView;
        private CBLEC macblec;
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

        private void bindView(CBLEC s) {
            macblec= s;
            //mImageView.setImageResource(R.drawable.test_food);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(getActivity())
                    .load(s.getImageurl())
                    .apply(options)
                    .into(mImageView);
            mFoodNameTextView.setText(s.getName());

            mFoodMatirialTextView.setText(s.getShiCaiMing());

            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",macblec.getObjId());
            editor.apply();
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID",macblec.getObjId());
            startActivity(i);
        }
    }

    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<holder> {
        List<CBLEC> mStringList;

        public Adapter(List<CBLEC> list) {
            mStringList = list;
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.recommend_default_recycler_view_item, parent, false);
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            holder.bindView(mStringList.get(position));
        }


        @Override
        public int getItemCount() {
            return mStringList.size();
        }
    }
}
