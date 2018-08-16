package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import com.scorpiomiku.cookbook.bean.Way_ShiCai;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuItemClickFragment;

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

public class ClassifierResultActivity extends AppCompatActivity {
    public static String mPicturePath;
    public static String mPictureResult;
    private List<CBLEC> mCblecList = new ArrayList<>();
    private ImageView mImageView;
    private TextView mDescribeTextView;
    private TextView mNutritionTextView;
    private TextView mFoodNameTextView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RecyclerView mRecyclerView;
    private int AllNU;
    private int chargeFangfa;
    private int pn=20;

    private Boolean panduan;
    private Intent i;
    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    private String jieshao;
    private String yingyang;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.arg1) {
                case 0:
                    //TODO
                case 1:
                    initView(jieshao,yingyang);
                    //TODO
            }

        }
    };
    private static final String TAG = "ClassifierResultActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classifier_result);
        mRecyclerView = (RecyclerView)findViewById(R.id.classifier_result_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mImageView = (ImageView) findViewById(R.id.classifier_result_imageview);
        mDescribeTextView = (TextView) findViewById(R.id.classifier_result_describe_textview);
        mNutritionTextView = (TextView) findViewById(R.id.nutrition_know_text_view);
        mFoodNameTextView = (TextView) findViewById(R.id.classifier_result_food_name);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        mImageView.setImageBitmap(BitmapFactory.decodeFile(mPicturePath));
        mFoodNameTextView.setText(mPictureResult);

        try {
            initYuanliao();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "onCreate: " + mPicturePath + "    " + mPictureResult);

    }
    public void initYuanliao() throws UnsupportedEncodingException {
        Log.d("bug在这","1111111");
        Log.d("bug在这","mPictureResult："+mPictureResult);
        if(mPictureResult==null||mPictureResult.equals(" ")||mPictureResult.equals("")){
            toast("请正确拍摄哦");
        }else {
            String url = "http://apis.juhe.cn/cook/query?key=" + APPKEY + "&menu=" + URLEncoder.encode(mPictureResult, "utf-8") + "&rn=20" + "&pn=" + pn;
            urlget(url);
        }
    }
    private void urlget(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //url = URL + "?keyword=" + URLEncoder.encode(keyword, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request = null;
                try {
                    request=new Request.Builder()
                            .url(url).build();
                }catch (Exception e){

                }
               
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
                Message msg = new Message();
                msg.arg1 = 1;
                handler.sendMessage(msg);
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
            Log.d("bug在这", "***********array.size(): "+array.size());
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
            Log.d("bug在这", "GetSorce: ID："+subObject.get("id").getAsString());
            Log.d("bug在这", "GetSorce: imtro："+subObject.get("imtro").getAsString());
            Log.d("bug在这", "GetSorce: tags："+subObject.get("tags").getAsString());

            if(i==0){
                 jieshao = subObject.get("imtro").getAsString();
                 yingyang = subObject.get("tags").getAsString();

            }

            Log.d("bug在这", "GetSorce: ：mRecyclerView");
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setAdapter(new Adapter(mCblecList));
                }
            });

            //mContextView.setAdapter(new MenuAdapter(mCblecList));

        }
    }

    private void initView(String jieshao,String yingyang) {
        Log.d("bug在这", "GetSorce: ：mFoodNameTextView");
        mDescribeTextView.setText(jieshao);
        Log.d("bug在这", "GetSorce: ：mDescribeTextView");
        mNutritionTextView.setText(yingyang+"\n");
        Log.d("bug在这", "GetSorce: ：mNutritionTextView");
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mFoodNameTextView;
        private TextView mFoodMatirialTextView;
        private View mWholeView ;
        private CBLEC s1;
        public holder(View itemView) {
            super(itemView);
            mWholeView = itemView ;
            mImageView = (ImageView) itemView.findViewById(R.id
                    .recommend_default_item_image_button);
            mFoodMatirialTextView = (TextView) itemView.findViewById(R.id
                    .recommend_default_item_food_matirial_text_view);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id
                    .recommend_default_item_food_name_text_view);
        }

        private void bindView(CBLEC s) {
            s1= s;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(ClassifierResultActivity.this)
                    .load(s.getImageurl())
                    .thumbnail( 0.5f )
                    .apply(options)
                    .into(mImageView);
            //mImageView.setImageResource(R.drawable.test_food);
            Log.d("三餐界面1", "bindView: url"+s.getImageurl());
            //mFoodNameTextView.setText("豌豆炒肉");
            mFoodNameTextView.setText(s.getName());
            Log.d("三餐界面1", "bindView: objid"+s.getObjId());
            Log.d("三餐界面1", "bindView: name"+s.getName());
          //  Log.d("三餐界面1", "bindView: shicai"+s.getShiCaiMing1()+s.getShiCaiMing2()+s.getShiCaiMing3()+s.getShiCaiMing4()+s.getShiCaiMing5()+s.getShiCaiMing6()+s.getShiCaiMing7()+s.getShiCaiMing8()+s.getShiCaiMing9());
            mFoodMatirialTextView.setText(s.getShiCaiMing());
            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",s1.getObjId());
            editor.apply();
            Intent i = new Intent(ClassifierResultActivity.this, MenuActivity.class);
            i.putExtra("Way_objectID",s1.getObjId());
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
            LayoutInflater layoutInflater = LayoutInflater.from(ClassifierResultActivity.this);
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
