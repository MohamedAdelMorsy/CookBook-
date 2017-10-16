package com.scorpiomiku.cookbook.combination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CaiZuEC;
import com.scorpiomiku.cookbook.bean.ZuCai;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.scorpiomiku.cookbook.recommendmenufragment.RecommendMenuItemClickFragment;
import com.yyydjk.library.HorizontalDropDownMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2017/6/4.
 */

public class CombinationFragment extends FragmentModule {

    private HorizontalDropDownMenu mHorizontalDropDownMenu;
    private TastesAdapter mTastesAdapter;
    private TimeAdapter mTimeAdapter;
    private MakeWayAdapter mMakeWayAdapter;
    private RecyclerView mContextView;
    public static String SouSuoNeiRong = "2948692387";

    private int chargeFangfa;
    private int AllNU;
    private int t=0;
    private String neirong;
    private Boolean panduan ;
    private ImageView mSearchImageView;
    private String mFoodName;
    private EditText mSearchEditView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private String mHeaders[] = {"口味", "时段", "做法"};
    private String mTastes[] = {"不限", "清淡", "辛辣", "酸甜"};
    private String mTimes[] = {"不限", "早餐", "午餐", "晚餐"};
    private String mMakeWays[] = {"不限", "清蒸", "爆炒", "凉拌"};
    private List<View> mPopupViews = new ArrayList<>();

    private List<CaiZuEC> mCblecList = new ArrayList<>();


    public static CombinationFragment newInstance() {
        return new CombinationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
       /* for (int i = 0; i < 15; i++) {
            testList.add("");
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.combination_fragment_layout, container, false);
        mHorizontalDropDownMenu = (HorizontalDropDownMenu) v.findViewById(R.id.combination_fragment_dropmenu);
        mSearchImageView = (ImageView) v.findViewById(R.id.combination_tool_bar_search_image_view);
        mSearchEditView = (EditText) v.findViewById(R.id.combination_search_edit_view);

        initView();

        mContextView = new RecyclerView(getContext());
        mContextView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mContextView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContextView.setNestedScrollingEnabled(false);

        Drawable[] mDrawables = new Drawable[]{
                getResources().getDrawable(R.drawable.combination_taste_ico, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_time_ico_24dp, getActivity().getTheme()),
                getResources().getDrawable(R.drawable.combination_makeway_ico, getActivity().getTheme())
        };
        mHorizontalDropDownMenu.setDropDownMenu(Arrays.asList(mHeaders), mPopupViews, mContextView
                , mDrawables
                , getResources().getDrawable(R.drawable.swipe_background, getActivity().getTheme()));


        Bmob.initialize(getActivity(),"3bfd53d40a453ea66ce653ab658582d1");
        String str1 = "";
        String str2 = "";
        String str3 = "";
        str1 = "TRUE";
        str2 = "TRUE";
        str3 = "TRUE";
        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
        eq2.addWhereEqualTo("ZaoCan", str1);
        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
        eq1.addWhereEqualTo("WanCan", str2);
        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
        eq3.addWhereEqualTo("WuCan", str3);
        //eq2.addWhereContains("FenLei","不限");

        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
        queries.add(eq2);
        queries.add(eq1);
        queries.add(eq3);
        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

        mainQuery.or(queries);
        //mainQuery.addWhereContains("FenLei","不限");
        mainQuery.findObjects(new FindListener<ZuCai>() {
            @Override
            public void done(List<ZuCai> object, BmobException e) {
                if(e==null){
                    Log.d("组菜界面", "测试点5"+"查询早成功");
                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                    //toast("查询结果有"+object.size()+"个");
                    AllNU=object.size();
                    for (ZuCai identification : object) {
                        t=t+1;
                        final List<String> ids = new ArrayList<String>();
                        ids.add(identification.getId1());
                        ids.add(identification.getId2());
                        ids.add(identification.getId3());
                        ids.add(identification.getId4());
                        Log.d("组菜界面", "Array添加成功");
                        final String LinShi_s = identification.getObjectId();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //initWay(LinShi_s);
                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                initWay(ids);
                            }
                        }).start();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFoodName = mSearchEditView.getText().toString();
                if (!mFoodName.equals("")) {
                    SouSuoNeiRong = mFoodName;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .replace(R.id.fragment_container, RecommendMenuItemClickFragment.newInstance())
                            .commit();
                    mSearchEditView.setText("");

                } else {
                    Toast.makeText(getActivity(), "请输入内容哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void initWay(final List<String> objIds){
        Log.d("组菜界面", "进入initway");

        Log.d("组菜界面", "测试点5"+"存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String objId = objIds.get(0);
                final String[] tp2 = new String[1];
                final String[] tp3 = new String[1];
                final String[] tp4 = new String[1];
                final JSONObject jas = new JSONObject();
                try {
                    jas.put("objectId",objIds.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final AsyncCustomEndpoints ace2 = new AsyncCustomEndpoints();
                ace2.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";

                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            Log.d(TAG, "收藏界面: result：" + result);
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("收藏界面", "测试点5" + "查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();

                                String ZuoFaTu;
                                try{
                                    JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
                                    ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
                                    final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
                                }catch (Exception e1213){
                                    ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                }
                                tp2[0] = ZuoFaTu;
                            }
                        }
                    }
                });
                try {
                    Log.d("组菜界面", "第一步测试完成");
                    if(objIds.get(3)!=null){
                        try {
                            jas.put("objectId",objIds.get(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final AsyncCustomEndpoints ace3 = new AsyncCustomEndpoints();
                        ace3.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                            public static final String TAG = "thing";

                            @Override
                            public void done(Object object, BmobException e) {
                                if (e == null) {
                                    String result = object.toString();
                                    Log.d(TAG, "组菜界面: result：" + result);
                                    JsonParser parser = new JsonParser();  //创建JSON解析器
                                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                                    Log.d("组菜界面", "测试点5" + "查询Way成功");
                                    for (int i = 0; i <= array.size(); i++) {
                                        final JsonObject subObject = array.get(i).getAsJsonObject();

                                        String ZuoFaTu;
                                        try{
                                            JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
                                            ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
                                            final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
                                        }catch (Exception e1213){
                                            ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                        }
                                        tp3[0] = ZuoFaTu;
                                    }
                                }
                            }
                        });
                    }
                }catch (Exception a){

                }
                try{
                    Log.d("组菜界面", "第二步测试完成");
                    if (objIds.get(4)!=null){
                        try {
                            jas.put("objectId",objIds.get(3));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final AsyncCustomEndpoints ace4 = new AsyncCustomEndpoints();
                        ace4.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                            public static final String TAG = "thing";

                            @Override
                            public void done(Object object, BmobException e) {
                                if (e == null) {
                                    String result = object.toString();
                                    Log.d(TAG, "组菜界面: result：" + result);
                                    JsonParser parser = new JsonParser();  //创建JSON解析器
                                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                                    Log.d("组菜界面", "测试点5" + "查询Way成功");
                                    for (int i = 0; i <= array.size(); i++) {
                                        final JsonObject subObject = array.get(i).getAsJsonObject();

                                        String ZuoFaTu;
                                        try{
                                            JsonObject ZuoFaTuyuan = subObject.get("zuoFaTuUser").getAsJsonObject();
                                            ZuoFaTu=ZuoFaTuyuan.get("url").getAsString();
                                            final String ZuoFaTuname = ZuoFaTuyuan.get("filename").getAsString();
                                        }catch (Exception e1213){
                                            ZuoFaTu = subObject.get("zuoFaTu").getAsString();
                                        }
                                        tp4[0] = ZuoFaTu;
                                    }
                                }
                            }
                        });
                    }
                }catch (Exception a){

                }

                Log.d("组菜界面", "第三步测试完成");
                final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
                try {
                    jas.put("objectId",objId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";
                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            Log.d(TAG, "组菜界面: result："+result);
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("组菜界面", "测试点5"+"查询Way成功");
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
                                        Log.d("组菜界面", "测试点5"+"开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
                                                    Log.d("组菜界面", "测试点5"+"开始查询食材部分result = "+result);
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
                                                        Log.d("组菜界面", "测试点5"+"图片加载成功");
                                                        Log.d("组菜界面", "测试点5"+"tp2[0]"+tp2[0]+"tp3[0]"+tp3[0]+"tp4[0]"+tp4[0]);
                                                        mCblecList.add(new CaiZuEC(ZuoFaMing, finalZuoFaTu,tp2[0],tp3[0],tp4[0], "这里是介绍", yongliao[0], yongliao[1], yongliao[2], yongliao[3], yongliao[4], yongliao[5], yongliao[6], yongliao[7], yongliao[8], yongliaoliang[0], yongliaoliang[1], yongliaoliang[2], yongliaoliang[3], yongliaoliang[4], yongliaoliang[5], yongliaoliang[6], yongliaoliang[7], yongliaoliang[8],Way_objectID,objIds.get(1),objIds.get(2),objIds.get(3),panduan));
                                                        mContextView.setAdapter(new MenuAdapter(mCblecList));
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
    /*-------------------------------------initView---------------------------------*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        /*----------------------------------------TastesView----------------------------------*/
        RecyclerView mTastesView = new RecyclerView(getContext());
        mTastesAdapter = new TastesAdapter(Arrays.asList(mTastes));
        mTastesView.setAdapter(mTastesAdapter);
        mTastesView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------TimeView----------------------------------*/
        RecyclerView mTimeView = new RecyclerView(getContext());
        mTimeAdapter = new TimeAdapter(Arrays.asList(mTimes));
        mTimeView.setAdapter(mTimeAdapter);
        mTimeView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*----------------------------------------MakeWayView----------------------------------*/
        RecyclerView mMakeWayView = new RecyclerView(getContext());
        mMakeWayAdapter = new MakeWayAdapter(Arrays.asList(mMakeWays));
        mMakeWayView.setAdapter(mMakeWayAdapter);
        mMakeWayView.setLayoutManager(new LinearLayoutManager(getContext()));



        /*-----------------initView-----------------*/
        mPopupViews.add(mTastesView);
        mPopupViews.add(mTimeView);
        mPopupViews.add(mMakeWayView);

        /*-------------contextView-------------*/

    }


    /*---------------------------------------------MakeWayAdapter-------------------------------------*/
    private class MakeWayAdapter extends RecyclerView.Adapter<MakeWayHolder> {

        private List<String> mStringlist;


        public MakeWayAdapter(List<String> list) {
            super();
            mStringlist = list;
        }


        @Override
        public MakeWayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combination_make_way_item, parent, false);
            return new MakeWayHolder(v);
        }


        @Override
        public void onBindViewHolder(MakeWayHolder holder, int position) {
            holder.bindView(mStringlist.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringlist.size();
        }

    }

    /*------------------------------------------------MakeWayholder------------------------------------*/
    private class MakeWayHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MakeWayHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_makeway_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            neirong=s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mMakeWays[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("做法")) {
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "清蒸";
                        str2 = "爆炒";
                        str3 = "凉拌";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("zuoFa", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("zuoFa", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("zuoFa", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("清蒸")) {
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "清蒸";
                        str2 = "11";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("zuoFa", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("zuoFa", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("zuoFa", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("爆炒")) {
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "11";
                        str2 = "爆炒";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("zuoFa", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("zuoFa", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("zuoFa", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mMakeWays[position]).equals("凉拌")) {
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "11";
                        str2 = "11";
                        str3 = "凉拌";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("zuoFa", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("zuoFa", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("zuoFa", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    /*------------------------------------------TimeAdapter-------------------------------------*/
    private class TimeAdapter extends RecyclerView.Adapter<TimeHolder> {

        private List<String> mStringList;

        public TimeAdapter(List<String> list) {
            mStringList = list;
        }

        @Override
        public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combination_time_item, parent, false);
            return new TimeHolder(v);
        }

        @Override
        public void onBindViewHolder(TimeHolder holder, int position) {
            holder.bindView(mStringList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }

    }

    /*-----------------------------------TimeHolder-------------------------*/
    private class TimeHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TimeHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_time_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            neirong= s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTimes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("时段")) {
                        mCblecList.clear();
                        Log.d("组菜界面", "onClick: mTimes"+mTimes[position]);
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "TRUE";
                        str2 = "TRUE";
                        str3 = "TRUE";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("ZaoCan", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("WanCan", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("WuCan", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("早餐")) {
                        mCblecList.clear();
                        Log.d("组菜界面", "onClick: mTimes"+mTimes[position]);
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "TRUE";
                        str2 = "11";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("ZaoCan", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("WanCan", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("WuCan", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("午餐")) {
                        mCblecList.clear();
                        Log.d("组菜界面", "onClick: mTimes"+mTimes[position]);
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "11";
                        str2 = "11";
                        str3 = "TRUE";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("ZaoCan", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("WanCan", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("WuCan", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }if ((position == 0 ? mHeaders[0] : mTimes[position]).equals("晚餐")) {
                        mCblecList.clear();
                        Log.d("组菜界面", "onClick: mTimes"+mTimes[position]);
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "11";
                        str2 = "TRUE";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("ZaoCan", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("WanCan", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("WuCan", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }

                }
            });
        }
    }


    /*--------------------------------------------TasterAdapter----------------------------------*/
    private class TastesAdapter extends RecyclerView.Adapter<TasteHolder> {

        private List<String> mStringlist;

        public TastesAdapter(List<String> list) {
            super();
            mStringlist = list;
        }

        @Override
        public TasteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combination_tastes_item, parent, false);
            return new TasteHolder(v);
        }

        @Override
        public void onBindViewHolder(TasteHolder holder, int position) {
            holder.bindView(mStringlist.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mStringlist.size();
        }

    }

    /*------------------------------------------Tasteholder------------------------*/
    private class TasteHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public TasteHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.combination_taste_item_test_view);
        }

        private void bindView(String s, final int position) {
            mTextView.setText(s);
            neirong = s;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHorizontalDropDownMenu.setTabText(position == 0 ? mHeaders[0] : mTastes[position]);
                    mHorizontalDropDownMenu.closeMenu();
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("口味")){
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "清淡";
                        str2 = "酸甜";
                        str3 = "辛辣";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("kouWei", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("kouWei", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("kouWei", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("清淡")){
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "清淡";
                        str2 = "清淡";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("kouWei", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("kouWei", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("kouWei", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("辛辣")){
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "辛辣";
                        str2 = "辛辣";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("kouWei", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("kouWei", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("kouWei", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    if ((position == 0 ? mHeaders[0] : mTastes[position]).equals("酸甜")){
                        mCblecList.clear();
                        String str1 = "";
                        String str2 = "";
                        String str3 = "";
                        str1 = "酸甜";
                        str2 = "酸甜";
                        str3 = "11";
                        BmobQuery<ZuCai> eq2 = new BmobQuery<ZuCai>();
                        eq2.addWhereEqualTo("kouWei", str1);
                        BmobQuery<ZuCai> eq1 = new BmobQuery<ZuCai>();
                        eq1.addWhereEqualTo("kouWei", str2);
                        BmobQuery<ZuCai> eq3 = new BmobQuery<ZuCai>();
                        eq3.addWhereEqualTo("kouWei", str3);
                        //eq2.addWhereContains("FenLei","不限");

                        List<BmobQuery<ZuCai>> queries = new ArrayList<BmobQuery<ZuCai>>();
                        queries.add(eq2);
                        queries.add(eq1);
                        queries.add(eq3);
                        BmobQuery<ZuCai> mainQuery = new BmobQuery<ZuCai>();

                        mainQuery.or(queries);
                        //mainQuery.addWhereContains("FenLei","不限");
                        mainQuery.findObjects(new FindListener<ZuCai>() {
                            @Override
                            public void done(List<ZuCai> object, BmobException e) {
                                if(e==null){
                                    Log.d("组菜界面", "测试点5"+"查询早成功");
                                    Log.d("组菜界面", "测试点5"+"查询结果有"+object.size()+"个");
                                    //toast("查询结果有"+object.size()+"个");
                                    AllNU=object.size();
                                    for (ZuCai identification : object) {
                                        t=t+1;
                                        final List<String> ids = new ArrayList<String>();
                                        ids.add(identification.getId1());
                                        ids.add(identification.getId2());
                                        ids.add(identification.getId3());
                                        ids.add(identification.getId4());
                                        Log.d("组菜界面", "Array添加成功");
                                        final String LinShi_s = identification.getObjectId();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //initWay(LinShi_s);
                                                //mRecyclerView.setAdapter(new Adapter(wayList));
                                                initWay(ids);
                                            }
                                        }).start();
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }

                }
            });

        }
    }


    /*---------------------------------------MenuAdapter----------------------------*/
    private class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        private List<CaiZuEC> mList;

        public MenuAdapter(List<CaiZuEC> l) {
            super();
            mList = l;
        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.combinstion_menu_recycler_view_item_change, parent, false);
            return new MenuHolder(v);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    /*----------------------------------------MenuHolder----------------------------*/
    private class MenuHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView1;
        private ImageView mImageView2;
        //private ImageView mImageView3;
        //private ImageView mImageView4;
        private TextView mNameTextView;
        private TextView mMatirialTextView;
        private CaiZuEC a213;
        public MenuHolder(View itemView) {
            super(itemView);
            mImageView1 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_1);
            mImageView2 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_2);
            //mImageView3 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_3);
            //mImageView4 = (ImageView) itemView.findViewById(R.id.combination_item_image_view_4);
            mNameTextView = (TextView) itemView.findViewById(R.id.combination_item_food_name_text_name);
            mMatirialTextView = (TextView) itemView.findViewById(R.id.combination_item_food_matirial_text_shicai);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ZuCaiJieMian.class);
                    Log.d("Way_objectID", "onClick: "+a213.getObjId());
                    i.putExtra("CZID1",a213.getObjId1());
                    i.putExtra("CZID2",a213.getObjId2());
                    i.putExtra("CZID3",a213.getObjId3());
                    i.putExtra("CZID4",a213.getObjId4());
                    startActivity(i);
                }
            });
        }
        private void bindView(CaiZuEC mcblec){
            Log.d("组菜界面", "bindView:准备添加 ");
            a213=mcblec;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test);
            Glide.with(getActivity())
                    .load(mcblec.getImageurl1())
                    .apply(options)
                    .into(mImageView1);
            Glide.with(getActivity())
                        .load(mcblec.getImageurl2())
                        .apply(options)
                        .into(mImageView2);
            /*if(mcblec.getImageurl3()!=null){
                Glide.with(getActivity())
                        .load(mcblec.getImageurl3())
                        .apply(options)
                        .into(mImageView3);
            }else{
            }
            if(mcblec.getImageurl4()!=null){
                Glide.with(getActivity())
                        .load(mcblec.getImageurl4())
                        .apply(options)
                        .into(mImageView4);
            }else{
            }*/

            //mImageView.setImageResource(R.drawable.food_test_1);
            mNameTextView.setText(mcblec.getName());
            mMatirialTextView.setText(mcblec.getShiCaiMing1()+mcblec.getShiCaiMing2()+mcblec.getShiCaiMing3()+mcblec.getShiCaiMing4()+mcblec.getShiCaiMing5()+mcblec.getShiCaiMing6()+mcblec.getShiCaiMing7()+mcblec.getShiCaiMing8()+mcblec.getShiCaiMing9());
        }

    }
}
