package com.scorpiomiku.cookbook.recommendmenufragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;

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

/**
 * Created by Administrator on 2017/7/14.
 */

public class RecommendMenuItemClickFragment extends FragmentModule {
    private RecyclerView mRecyclerView;
    //private List<String> mList = new ArrayList<>();
    private List<CBLEC> testList = new ArrayList<>();
    private int AllNU;
    private int t=0;
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
    private void initWay(String objId){
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId",objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("标签界面", "测试点5"+"存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            Log.d(TAG, "标签界面: result："+result);
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("标签界面", "测试点5"+"查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                try{
                                    panduan = subObject.get("FromUser").getAsBoolean();
                                }catch (Exception a){
                                    panduan = false;
                                }
                                String ZuoFaTu ;
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
                                        Log.d("标签界面", "测试点5"+"开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
                                                    Log.d("标签界面", "测试点5"+"开始查询食材部分result = "+result);
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
                                                        Log.d("标签界面", "测试点5"+"图片加载成功");
                                                        testList.add(new CBLEC(ZuoFaMing, finalZuoFaTu, "这里是介绍", yongliao[0], yongliao[1], yongliao[2], yongliao[3], yongliao[4], yongliao[5], yongliao[6], yongliao[7], yongliao[8], yongliaoliang[0], yongliaoliang[1], yongliaoliang[2], yongliaoliang[3], yongliaoliang[4], yongliaoliang[5], yongliaoliang[6], yongliaoliang[7], yongliaoliang[8],Way_objectID,panduan));
                                                        //*/
                                                        Log.d("标签界面", "测试点5"+"放入对象成功");
                                                        mRecyclerView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                mRecyclerView.setAdapter(new Adapter(testList));
                                                                Log.d("标签界面", "全部结束");

                                                            }
                                                        });

                                                       /* mSwipeMenuRecyclerView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                mAdapter = new MyCollectionFragment.Adapter(mCblecList);
                                                                Log.d("收藏界面", "测试点5"+"ada创建成功" );
                                                                mAdapter.notifyDataSetChanged();
                                                                mSwipeMenuRecyclerView.setAdapter(mAdapter);
                                                                //recyclerView.refreshComplete();
                                                                //System.out.println("Runnable thread id " + Thread.currentThread().getId());
                                                                mAdapter.notifyDataSetChanged();
                                                                //adapter.notifyDataSetChanged();
                                                                //recyclerView.notifyAll();
                                                            }
                                                        });*/





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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_menu_item_click_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_menu_item_click_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter(testList));
        mRecyclerView.setNestedScrollingEnabled(false);
        Bmob.initialize(getActivity(), "3bfd53d40a453ea66ce653ab658582d1");
        if(s=="2948692387"){
            CombinationFragment.SouSuoNeiRong="2948692387";
            s = changeby;
            Log.d("标签界面", "onCreate: MenuHolder.menusme"+s);
            List<BmobQuery<Way>> queries = new ArrayList<BmobQuery<Way>>();
            if(s=="酸"||s=="甜"||s=="辣"||s=="酸辣"||s=="酸甜"||s=="微苦"){
                if(s=="酸"||s=="甜"||s=="辣"){
                    Log.d("标签界面", "进入选择");
                    String str1 = s;
                    BmobQuery<Way> eq2 = new BmobQuery<Way>();
                    eq2.addWhereEqualTo("DanBiao", str1);
                    BmobQuery<Way> eq1 = new BmobQuery<Way>();
                    eq1.addWhereEqualTo("DanBiao2", str1);
                    //eq2.addWhereContains("FenLei","不限");
                    queries.add(eq2);
                    queries.add(eq1);
                }else if(s=="酸辣"||s=="酸甜"||s=="微苦"){
                    String str1 = s;
                    BmobQuery<Way> eq2 = new BmobQuery<Way>();
                    eq2.addWhereEqualTo("ShuangBiao", str1);
                    BmobQuery<Way> eq1 = new BmobQuery<Way>();
                    eq1.addWhereEqualTo("ShuangBiao2", str1);
                    //eq2.addWhereContains("FenLei","不限");
                    queries.add(eq2);
                    queries.add(eq1);
                }
            }else if(s=="素菜"||s=="荤菜"||s=="甜品"||s=="汤粥类"||s=="水果类"||s=="腌制类"||s=="面食"||s=="水产"||s=="豆类"){
                String str1 = s;
                BmobQuery<Way> eq2 = new BmobQuery<Way>();
                eq2.addWhereEqualTo("Lei", str1);
                BmobQuery<Way> eq1 = new BmobQuery<Way>();
                eq1.addWhereEqualTo("Lei", str1);
                //eq2.addWhereContains("FenLei","不限");
                queries.add(eq2);
                queries.add(eq1);
            }else if(s=="家常"||s=="聚会"||s=="宿舍"||s=="野餐"||s=="宵夜"||s==""||s=="生日"){
                String str1 = s;
                BmobQuery<Way> eq2 = new BmobQuery<Way>();
                eq2.addWhereEqualTo("Where", str1);
                BmobQuery<Way> eq1 = new BmobQuery<Way>();
                eq1.addWhereEqualTo("Where2", str1);
                //eq2.addWhereContains("FenLei","不限");
                queries.add(eq2);
                queries.add(eq1);
            }

        /*if(t<1){
             str = "午餐";
        }else {
             str = neirong;  //为传入的要搜索的食材名
        }/*/

            BmobQuery<Way> mainQuery = new BmobQuery<Way>();
            mainQuery.or(queries);
            Log.d("标签界面", "运行获取");
            //mainQuery.addWhereContains("FenLei","不限");
            mainQuery.findObjects(new FindListener<Way>() {
                @Override
                public void done(List<Way> object, BmobException e) {
                    if(e==null){
                        Log.d("标签界面", "测试点5"+"查询早成功");
                        Log.d("标签界面", "测试点5"+"查询结果有"+object.size()+"个");
                        //toast("查询结果有"+object.size()+"个");
                        AllNU=object.size();
                        for (Way identification : object) {
                            t=t+1;
                            final String LinShi_s = identification.getObjectId();
                            Log.d("标签界面", "LinShi_s="+LinShi_s);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //initWay(LinShi_s);
                                    //mRecyclerView.setAdapter(new Adapter(wayList));
                                    initWay(LinShi_s);
                                }
                            }).start();
                        }
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }else{
            CombinationFragment.SouSuoNeiRong="2948692387";
            String str = s;  /*为传入的要搜索的食材名*/
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
                        AllNU=object.size();
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

            JSONObject jas = new JSONObject();
            try {
                jas.put("caiming",s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
            ace1.callEndpoint("get_way", jas, new CloudCodeListener() {
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
                            final String Way_objectID = subObject.get("objectId").getAsString();
                            Log.d("显示网络文件：", "菜谱名获取菜谱" + "测试点1:"+Way_objectID);
                            //int cishu = subObject.get("cishu").getAsInt();
                            Log.d("显示网络文件：", "菜谱名获取菜谱" + "测试点2");
                            String ZuoFaMing=subObject.get("zuoFaMing").getAsString();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    initWay(Way_objectID);
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

        mRecyclerView.setAdapter(new Adapter(testList));
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

            mFoodMatirialTextView.setText(s.getShiCaiMing1()+s.getShiCaiMing2()+s.getShiCaiMing3()+s.getShiCaiMing4()+s.getShiCaiMing5()+s.getShiCaiMing6()+s.getShiCaiMing7()+s.getShiCaiMing8()+s.getShiCaiMing9());

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
