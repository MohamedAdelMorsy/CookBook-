package com.scorpiomiku.cookbook.recommend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.A_sorce;
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuItemClickFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URLEncoder;


/**
 * Created by Administrator on 2017/6/3.
 */

public class BreakFastFragment extends FragmentModule {

    private RecyclerView mRecyclerView;
    private String ZuoFaTu;
    private List<Way> wayList = new ArrayList<>();
    public static BreakFastFragment newInstance() {
        return new BreakFastFragment();
    }
    private String date = null;
    public   int pn = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static final String APPKEY = "0d6cb9431d04bba78300b0227867d48c";// 你的appkey
    public static final String URL = "http://apis.juhe.cn/cook/index?key=";
    public static final int cid = 37;
    private int tpyecid = 0;
    public static final int num = 100;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_breakfast_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_breakfast_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        urlget();



        return v;
    }

    private void urlget(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;
                if (tpyecid == 0){
                    url = URL +  APPKEY+"&cid="+cid+"&rn=20";
                }else {
                    url = URL +  APPKEY+"&cid="+cid+"&pn="+pn+"&rn=20";
                    tpyecid = 0;
                }


                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url(url).build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    date=response.body().string();
                    Log.d("数据显示：", "run: "+date);
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GetSorce();

            }
        }).start();
    }
    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView;
        private View mWholeView;
        private Way ss;
        public holder(View itemView) {
            super(itemView);
            Log.d("分类", "holder");
            mWholeView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id
                    .recommend_breakfast_item_food_image_view);
            mTextView = (TextView) itemView.findViewById(R.id
                    .recommend_breakfast_item_food_name_text_view);
        }

        private void bindView(Way s) {
            ss = s;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.food_test_1);
            Glide.with(getActivity())
                    .load(s.getZuoFaTu())
                    .apply(options)
                    .into(mImageView);

            mTextView.setText(s.getZuoFaMing());
            mWholeView.setOnClickListener(this);
        }
        //刷新实现
        private void bindviewfoot(final int pd){
            mWholeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tpyecid = 1;
                    pn = pd;
                    urlget();

                    //Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
                }
            });
        }
//
//        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",ss.getObjid());
            editor.apply();
            Log.d("数据显示", "onClick()");
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID",ss.getObjid());
            startActivity(i);
        }
    }

    private void show(String result) throws JSONException {
        Log.d("分类", "result: 测试点："+result);
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(result);
        Log.d("分类", "run: 测试点4");

        if (0 != ssobject.get("error_code").getAsInt()) {
            Log.d("分类", "show: msg："+ssobject.get("classid").getAsString());
          //  System.out.println(json.getString("msg"));
        } else {
            JsonArray nei11 = ssobject.get("result").getAsJsonArray();
            for (int i = 0; i < nei11.size(); i++) {
                Log.d("分类", "run: 测试点8");
                final JsonObject subObject = nei11.get(i).getAsJsonObject();
                String classid = subObject.get("id").getAsString();
                String name = subObject.get("type").getAsString();
                String parentid = subObject.get("tid").getAsString();
                Log.d("分类fin", "show: ID："+classid + " 名字：" + name + "   " + parentid);
                // System.out.println(classid + " " + name + " " + parentid);
//                if (obj.opt("list") != null) {
//                    JSONArray list = obj.optJSONArray("list");
//                    for (int j = 0; j < list.size(); j++) {
//                        JSONObject list_ = (JSONObject) list.opt(j);
//                        String classid_ = list_.getString("classid");
//                        String name_ = list_.getString("name");
//                        String parentid_ = list_.getString("parentid");
//                        System.out.println(classid_ + " " + name_ + " " + parentid_);
//                    }
//                }
            }


//            Log.d("分类", "run: 测试点5");
//            JsonArray nei11 = ssobject.get("result").getAsJsonArray();
//            JsonObject nei1 = nei11.get(0).getAsJsonObject();
//            //JsonObject nei1 = ssobject.get("result").getAsJsonObject();
//            Log.d("分类", "run: 测试点6");
//            final JsonArray array = nei1.get("list").getAsJsonArray();    //得到为json的数组
//            Log.d("分类", "run: 测试点7");
//            for (int i = 0; i < array.size(); i++) {
//                Log.d("分类", "run: 测试点8");
//                final JsonObject subObject = array.get(i).getAsJsonObject();
//                String classid = subObject.get("classid").getAsString();
//                String name = subObject.get("name").getAsString();
//                String parentid = subObject.get("parentid").getAsString();
//                Log.d("分类", "show: ID："+classid + " 名字：" + name + "   " + parentid);
//               // System.out.println(classid + " " + name + " " + parentid);
////                if (obj.opt("list") != null) {
////                    JSONArray list = obj.optJSONArray("list");
////                    for (int j = 0; j < list.size(); j++) {
////                        JSONObject list_ = (JSONObject) list.opt(j);
////                        String classid_ = list_.getString("classid");
////                        String name_ = list_.getString("name");
////                        String parentid_ = list_.getString("parentid");
////                        System.out.println(classid_ + " " + name_ + " " + parentid_);
////                    }
////                }
//            }
        }
    }

    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<holder> {
        List<Way> mStringList = new ArrayList<>();

        public Adapter(List<Way> list) {
            Log.d("数据显示", "Adapter");
            mStringList = list;
            Log.d("数据显示", "Adapter传值完成");
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v ;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType == 1056){
                v = layoutInflater.inflate(R.layout.item_foot,parent,false);
            }else {
                v = layoutInflater.inflate(R.layout.recommend_breakfast_recycler_view_item, parent, false);

            }
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            if(position == mStringList.size()-1){
                holder.bindviewfoot(position);
            }else{
                holder.bindView(mStringList.get(position));
            }
        }
        @Override
        public int getItemViewType(int position) {
            if(position == mStringList.size()-1){
                return 1056;
            }else{
                return super.getItemViewType(position);
            }
        }
        @Override
        public int getItemCount() {
            Log.d("数据显示", "getItemCount()");
            return mStringList.size();
        }
    }
    private void  GetSorce(){
                JsonParser parser = new JsonParser();  //创建JSON解析器
                JsonObject ssobject = (JsonObject) parser.parse(date);  //创建JsonObject对象//将json数据转为为boolean型的数据
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
                            final Way way = new Way();
                            way.setObjid(subObject.get("id").getAsString());
                            way.setZuoFaMing(ZuoFaMing);
                            way.setZuoFaTu(ZuoFaTu);
                            wayList.add(way);
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(new Adapter(wayList));
                        }
                    });
                }


    }
}
