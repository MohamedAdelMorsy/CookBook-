package com.scorpiomiku.cookbook.combination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.scorpiomiku.cookbook.menuactivity.DefaultFragment;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.recommend.RecommendDefultFragment;

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

public class ZuCaiJieMian extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    //private List<String> mList = new ArrayList<>();
    private List<CBLEC> testList = new ArrayList<>();
    private int AllNU;
    private int t=0;
    private String id1;
    private String id2;
    private String id3;
    private String id4;
    private Boolean panduan;
    //private String ZuoFaTu;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_menu_item_click_fragment);
        mRecyclerView = (RecyclerView)findViewById(R.id.recommend_menu_item_click_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Adapter(testList));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        Intent intent = getIntent();
        id1 = intent.getStringExtra("CZID1");
        id2 = intent.getStringExtra("CZID2");
        id3 = intent.getStringExtra("CZID3");
        id4 = intent.getStringExtra("CZID4");
        if(id1!=null){
            urlget(id1);
        }if(id2!=null){
            urlget(id2);
        }
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
            testList.add(new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false));
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setAdapter(new Adapter(testList));
                }
            });
        }
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
            Glide.with(ZuCaiJieMian.this)
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
            Log.d("zucaijiemian", "onClick: ");
            Intent i = new Intent(ZuCaiJieMian.this, MenuActivity.class);
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
            LayoutInflater layoutInflater = LayoutInflater.from(ZuCaiJieMian.this);
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
