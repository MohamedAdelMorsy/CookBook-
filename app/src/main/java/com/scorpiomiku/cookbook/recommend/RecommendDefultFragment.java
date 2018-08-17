package com.scorpiomiku.cookbook.recommend;

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
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.tensorflow.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.URL;

/**
 * Created by Administrator on 2017/6/5.
 */

public class RecommendDefultFragment extends FragmentModule {
    public static RecommendDefultFragment newInstance() {
        return new RecommendDefultFragment();
    }

    private RecyclerView mRecyclerView;

    private List<CBLEC> mCblecList = new ArrayList<>();
    //    public static final String APPKEY = "0d6cb9431d04bba78300b0227867d48c";// 你的appkey
    public static final String APPKEY = Utils.APPKEY;// 你的appkey
    public static final int cid = 39;
    private int tpyecid = 0;
    public int pn = 0;
    private String date = null;
    private String ZuoFaTu;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_default_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_default_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        urlget();

        return v;
    }

    private void urlget() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;
                if (tpyecid == 0) {
                    url = URL + APPKEY + "&cid=" + cid + "&rn=20";
                } else {
                    url = URL + APPKEY + "&cid=" + cid + "&pn=" + pn + "&rn=20";
                    tpyecid = 0;
                }


                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient = new OkHttpClient();
                //服务器返回的地址
                Request request = new Request.Builder()
                        .url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    date = response.body().string();
                    Log.d("数据显示：", "run: " + date);
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GetSorce();

            }
        }).start();
    }

    private void GetSorce() {
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(date);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < array.size(); i++) {
            Log.d("数据显示", "***********array.size(): " + array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            try {
                ZuoFaTu = subObject.get("albums").getAsString();
            } catch (Exception e1213) {
            }

            //int cishu = subObject.get("cishu").getAsInt();
            final String ZuoFaMing = subObject.get("title").getAsString();
            // final CBLEC cblec = new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false);
            mCblecList.add(new CBLEC(ZuoFaMing, ZuoFaTu, subObject.get("ingredients").getAsString() + subObject.get("burden").getAsString(), subObject.get("id").getAsString(), false));
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setAdapter(new RecommendDefultFragment.Adapter(mCblecList));
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
        private CBLEC s1;

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

        private void bindviewfoot(final int pd) {
            mWholeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tpyecid = 1;
                    pn = pd;
                    urlget();
                    //Toast.makeText(getActivity(),"HEllo",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void bindView(CBLEC s) {
            s1 = s;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(getActivity())
                    .load(s.getImageurl())
                    .thumbnail(0.5f)
                    .apply(options)
                    .into(mImageView);
            //mImageView.setImageResource(R.drawable.test_food);
            Log.d("三餐界面1", "bindView: url" + s.getImageurl());
            //mFoodNameTextView.setText("豌豆炒肉");
            mFoodNameTextView.setText(s.getName());
            Log.d("三餐界面1", "bindView: objid" + s.getObjId());
            Log.d("三餐界面1", "bindView: name" + s.getName());
            Log.d("三餐界面1", "bindView: shicai" + s.getShiCaiMing());
            mFoodMatirialTextView.setText(s.getShiCaiMing());
            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID", s1.getObjId());
            editor.apply();
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID", s1.getObjId());
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
            View v;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == 1056) {
                v = layoutInflater.inflate(R.layout.item_foot, parent, false);
            } else {
                v = layoutInflater.inflate(R.layout.recommend_default_recycler_view_item, parent, false);
            }
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            if (position == mStringList.size() - 1) {
                holder.bindviewfoot(position);
            } else {
                holder.bindView(mStringList.get(position));
            }
        }


        @Override
        public int getItemCount() {
            return mStringList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mStringList.size() - 1) {
                return 1056;
            } else {
                return super.getItemViewType(position);
            }
        }
    }

}
