package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import com.scorpiomiku.cookbook.bean.Way_ShiCai;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

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
    private int t=0;
    private String neirong;
    private Boolean panduan;
    private Intent i;

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
        Bmob.initialize(this, "3bfd53d40a453ea66ce653ab658582d1");
        mImageView.setImageBitmap(BitmapFactory.decodeFile(mPicturePath));
        mFoodNameTextView.setText(mPictureResult);
        initYuanliao();

        Log.d(TAG, "onCreate: " + mPicturePath + "    " + mPictureResult);
    }
    public void initYuanliao(){
        JSONObject jas = new JSONObject();
        try {
            jas.put("caiming",mPictureResult );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        ace1.callEndpoint("getYuanliao", jas, new CloudCodeListener() {
            public static final String TAG = "thing";
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result = object.toString();
                    Log.e(TAG, "thiiis is return string:" + result);
                    /*{"results":[{
                    "cishu":0,"createdAt":"2017-07-04 20:03:31",
                    "objectId":"47295b4608",
                    "shiDuan":"中","updatedAt":"2017-07-04 20:03:31",
                    "zuoFa":"炒","zuoFaMing":"酱肉丁",
                    "zuoFaTu":{"__type":"File","filename":"酱肉丁.gif","group":"","url":"http://bmob-cdn-12102.b0.upaiyun.com/2017/07/04/40aae1d7aa67496985dba4499167ced9.gif"}}]}
                    * */

                    JsonParser parser = new JsonParser();  //创建JSON解析器
                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                    for (int i = 0; i < array.size(); i++) {
                        Log.d("显示网络文件：", "菜谱名获取菜谱" + "测试点1");
                        final JsonObject subObject = array.get(i).getAsJsonObject();
                        //int cishu = subObject.get("cishu").getAsInt();
                        Log.d("显示网络文件：", "菜谱名获取菜谱" + "测试点2");
                        final String name=subObject.get("name").getAsString();
                        String jieshao = subObject.get("jieshao").getAsString();
                        String yingyang = subObject.get("yingyang").getAsString();
                        initView(jieshao,yingyang);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String str = name;  /*为传入的要搜索的食材名*/
                                BmobQuery<Way_ShiCai> eq1 = new BmobQuery<Way_ShiCai>();
                                eq1.addWhereEqualTo("yongLiao1", str);
                                Log.d("显示食材", "搜索测试点1");
                                BmobQuery<Way_ShiCai> eq2 = new BmobQuery<Way_ShiCai>();
                                eq2.addWhereEqualTo("yongLiao2", str);
                                BmobQuery<Way_ShiCai> eq3 = new BmobQuery<Way_ShiCai>();
                                eq3.addWhereEqualTo("yongLiao3", str);
                                BmobQuery<Way_ShiCai> eq4 = new BmobQuery<Way_ShiCai>();
                                eq4.addWhereEqualTo("yongLiao4", str);
                                BmobQuery<Way_ShiCai> eq5 = new BmobQuery<Way_ShiCai>();
                                eq5.addWhereEqualTo("yongLiao5", str);
                                BmobQuery<Way_ShiCai> eq6 = new BmobQuery<Way_ShiCai>();
                                eq6.addWhereEqualTo("yongLiao6", str);
                                BmobQuery<Way_ShiCai> eq7 = new BmobQuery<Way_ShiCai>();
                                eq7.addWhereEqualTo("yongLiao7", str);
                                BmobQuery<Way_ShiCai> eq8 = new BmobQuery<Way_ShiCai>();
                                eq8.addWhereEqualTo("yongLiao8", str);
                                BmobQuery<Way_ShiCai> eq9 = new BmobQuery<Way_ShiCai>();
                                eq9.addWhereEqualTo("yongLiao9", str);
                                List<BmobQuery<Way_ShiCai>> queries = new ArrayList<BmobQuery<Way_ShiCai>>();
                                queries.add(eq1);
                                queries.add(eq2);
                                queries.add(eq3);
                                queries.add(eq4);
                                queries.add(eq5);
                                queries.add(eq6);
                                queries.add(eq7);
                                queries.add(eq8);
                                queries.add(eq9);
                                BmobQuery<Way_ShiCai> mainQuery = new BmobQuery<Way_ShiCai>();
                                mainQuery.or(queries);
                                mainQuery.findObjects(new FindListener<Way_ShiCai>() {
                                    @Override
                                    public void done(List<Way_ShiCai> object, BmobException e) {
                                        if(e==null){
                                            //toast("查询结果有"+object.size()+"个");
                                            for (Way_ShiCai identification : object) {
                                                t=t+1;
                                                final String LinShi_s = identification.getIdentification();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        initWay(LinShi_s);
                                                    }
                                                }).start();
                                            }
                                        }else{
                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }
                        }).start();

                    }
                }
                else {
                    Log.e(TAG, " " + e.getMessage());
                }
            }
        });
    }

    private void initView(String jieshao,String yingyang) {
        mImageView.setImageBitmap(BitmapFactory.decodeFile(mPicturePath));
        mFoodNameTextView.setText(mPictureResult);
        mDescribeTextView.setText(jieshao);
        mNutritionTextView.setText(yingyang+"\n");
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
                                                        //mContextView.setAdapter(new MenuAdapter(mCblecList));
                                                        mRecyclerView.setAdapter(new Adapter(mCblecList));
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
            Log.d("三餐界面1", "bindView: shicai"+s.getShiCaiMing1()+s.getShiCaiMing2()+s.getShiCaiMing3()+s.getShiCaiMing4()+s.getShiCaiMing5()+s.getShiCaiMing6()+s.getShiCaiMing7()+s.getShiCaiMing8()+s.getShiCaiMing9());
            mFoodMatirialTextView.setText(s.getShiCaiMing1()+s.getShiCaiMing2()+s.getShiCaiMing3()+s.getShiCaiMing4()+s.getShiCaiMing5()+s.getShiCaiMing6()+s.getShiCaiMing7()+s.getShiCaiMing8()+s.getShiCaiMing9());
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
