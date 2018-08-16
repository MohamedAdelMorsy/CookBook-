package com.scorpiomiku.cookbook.takephoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.scorpiomiku.cookbook.recommend.BreakFastFragment.APPKEY;

public class MorePhotoAct extends AppCompatActivity {
    private int allshicai = 0;
    private int okshicai = 0;
    private List<CBLEC> testList = new ArrayList<>();
    private int AllNU;
    private int t = 0;
    private Boolean panduan;
    private RecyclerView mRecyclerView;
    //private String ZuoFaTu;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<String> moijid1 = new ArrayList<>();
    private List<String> moijid2 = new ArrayList<>();
    private List<String> moijid3 = new ArrayList<>();
    private List<String> moijidfinal = new ArrayList<>();
    private List<String> moreResults = new ArrayList<>();
    private String jieshao;
    private String yingyang;

    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //TODO
                case 1:
                    //TODO
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_menu_item_click_fragment);
        mRecyclerView = (RecyclerView) findViewById(R.id.recommend_menu_item_click_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Adapter(testList));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        moreResults = MoreCameraActivity.moreResults;
//        moreResults.clear();;
//        moreResults.add("黄瓜");
//        moreResults.add("胡萝卜");
        if (moreResults.size() == 1) {
            try {
                urlget2("http://apis.juhe.cn/cook/query?key=" + APPKEY + "&menu=" + URLEncoder.encode(moreResults.get(0), "utf-8") + "&rn=20" + "&pn=" + 0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else {
            try {
                initYuanliao(allshicai);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


    public void initYuanliao(int pn) throws UnsupportedEncodingException {
        Log.d("bug在这", "12233321");
        Log.d("test", "initYuanliao: " + moreResults.size());
        if (moreResults == null) {
            toast("结果为空");
        }
        try {
            if (moreResults.get(0) == null) {
                toast("结果为空");
            } else {
                String url = "http://apis.juhe.cn/cook/query?key=" + APPKEY + "&menu=" + URLEncoder.encode(moreResults.get(0), "utf-8") + "&rn=20" + "&pn=" + pn;
                urlget(url);
                Log.d("bug在这", "moreResults.get(0)：" + moreResults.get(0));
            }
        } catch (Exception e) {
            Log.d("test", "initYuanliao: " + e.toString());
            toast("结果为空");
        }

    }

    private void jiexiurljiansuo(String data) throws UnsupportedEncodingException {
        String ZuoFaTu = null;
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < array.size(); i++) {
            Log.d("bug在这", "***********array.size(): " + array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            String shicai = subObject.get("burden").getAsString();
            allshicai = allshicai + 1;
            for (int k = 1; k < moreResults.size(); k++) {

                Log.d("jiexiurljiansuo", "jiexiurljiansuo: +" + moreResults.size() + "   k =" + k);
                if (shicai.indexOf(moreResults.get(k)) == -1) {
                    Log.d("jiexiurljiansuo", "jiexiurljiansuo: break");
                    break;
                } else if (k == moreResults.size() - 1) {
                    okshicai = okshicai + 1;
                    try {
                        ZuoFaTu = subObject.get("albums").getAsString();
                    } catch (Exception e1213) {
                    }
                    //int cishu = subObject.get("cishu").getAsInt();
                    final String ZuoFaMing = subObject.get("title").getAsString();
                    // final CBLEC cblec = new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false);
                    testList.add(new CBLEC(ZuoFaMing, ZuoFaTu, subObject.get("ingredients").getAsString() + subObject.get("burden").getAsString(), subObject.get("id").getAsString(), false));
                    Log.d("bug在这", "GetSorce: ID：" + subObject.get("id").getAsString());
                    Log.d("bug在这", "GetSorce: imtro：" + subObject.get("imtro").getAsString());
                    Log.d("bug在这", "GetSorce: tags：" + subObject.get("tags").getAsString());

                    if (i == 0) {
                        jieshao = subObject.get("imtro").getAsString();
                        yingyang = subObject.get("tags").getAsString();

                    }

                    Log.d("bug在这", "GetSorce: ：mRecyclerView");
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(new Adapter(testList));
                            Log.d("标签界面", "全部结束");

                        }
                    });

                }
            }



            //mContextView.setAdapter(new MenuAdapter(mCblecList));

        }
        if (okshicai < 10 && allshicai < 100) {
            try {
                initYuanliao(allshicai);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
//            Looper.prepare();
//            toast("没有找到合适的搭配");
            urlget2("http://apis.juhe.cn/cook/query?key=" + APPKEY + "&menu=" + URLEncoder.encode(moreResults.get(0), "utf-8") + "&rn=20" + "&pn=" + 0);

        }


    }

    private void urlget(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient = new OkHttpClient();
                //服务器返回的地址
                Request request = null;
                try {
                    request = new Request.Builder()
                            .url(url).build();
                } catch (Exception e) {

                }

                String date = null;
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    date = response.body().string();
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    jiexiurljiansuo(date);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void urlget2(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient = new OkHttpClient();
                //服务器返回的地址
                Request request = null;
                try {
                    request = new Request.Builder()
                            .url(url).build();
                } catch (Exception e) {

                }

                String date = null;
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    date = response.body().string();
                    //把数据传入解析josn数据方法
                    // jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GetSorce(date);
                Message msg = new Message();
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void GetSorce(String data) {
        String ZuoFaTu = null;
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject ssobject = (JsonObject) parser.parse(data);  //创建JsonObject对象//将json数据转为为boolean型的数据
        // ********************************************************************************
        JsonObject nei1 = ssobject.get("result").getAsJsonObject();
        final JsonArray array = nei1.get("data").getAsJsonArray();    //得到为json的数组
        for (int i = 0; i < array.size(); i++) {
            Log.d("bug在这", "***********array.size(): " + array.size());
            final int finalI = i;

            final JsonObject subObject = array.get(finalI).getAsJsonObject();
            try {
                ZuoFaTu = subObject.get("albums").getAsString();
            } catch (Exception e1213) {
            }
            //int cishu = subObject.get("cishu").getAsInt();
            final String ZuoFaMing = subObject.get("title").getAsString();
            // final CBLEC cblec = new CBLEC(ZuoFaMing,ZuoFaTu,subObject.get("ingredients").getAsString()+subObject.get("burden").getAsString(),subObject.get("id").getAsString(),false);
            testList.add(new CBLEC(ZuoFaMing, ZuoFaTu, subObject.get("ingredients").getAsString() + subObject.get("burden").getAsString(), subObject.get("id").getAsString(), false));
            Log.d("bug在这", "GetSorce: ID：" + subObject.get("id").getAsString());
            Log.d("bug在这", "GetSorce: imtro：" + subObject.get("imtro").getAsString());
            Log.d("bug在这", "GetSorce: tags：" + subObject.get("tags").getAsString());

            if (i == 0) {
                jieshao = subObject.get("imtro").getAsString();
                yingyang = subObject.get("tags").getAsString();

            }

            Log.d("bug在这", "GetSorce: ：mRecyclerView");
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setAdapter(new Adapter(testList));
                    Log.d("标签界面", "全部结束");

                }
            });

            //mContextView.setAdapter(new MenuAdapter(mCblecList));

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
            macblec = s;
            //mImageView.setImageResource(R.drawable.test_food);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(MorePhotoAct.this)
                    .load(s.getImageurl())
                    .apply(options)
                    .into(mImageView);
            mFoodNameTextView.setText(s.getName());

            mFoodMatirialTextView.setText(s.getShiCaiMing());

            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID", macblec.getObjId());
            editor.apply();
            Log.d("zucaijiemian", "onClick: ");
            Intent i = new Intent(MorePhotoAct.this, MenuActivity.class);
            i.putExtra("Way_objectID", macblec.getObjId());
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
            LayoutInflater layoutInflater = LayoutInflater.from(MorePhotoAct.this);
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
