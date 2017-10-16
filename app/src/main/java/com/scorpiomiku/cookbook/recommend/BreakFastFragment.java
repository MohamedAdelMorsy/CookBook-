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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/6/3.
 */

public class BreakFastFragment extends FragmentModule {

    private RecyclerView mRecyclerView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int AllNU;
    private int t = 0 ;
    private String ZuoFaTu;
    private Boolean panduan;
    private List<Way> wayList = new ArrayList<>();
    public static BreakFastFragment newInstance() {
        return new BreakFastFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_breakfast_fragment_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recommend_breakfast_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setNestedScrollingEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        Log.d("早餐界面", "测试点51"+"导入id成功");
        String str1 = "TRUE";
        String str2 = "FALSE";
        String str3 = "FALSE";

        /*if(t<1){
             str = "午餐";
        }else {
             str = neirong;  //为传入的要搜索的食材名
        }/*/

        BmobQuery<Way> eq2 = new BmobQuery<Way>();
        eq2.addWhereEqualTo("ZaoCan", str1);
        BmobQuery<Way> eq1 = new BmobQuery<Way>();
        eq1.addWhereEqualTo("WanCan", str2);
        BmobQuery<Way> eq3 = new BmobQuery<Way>();
        eq3.addWhereEqualTo("WuCan", str3);

        //eq2.addWhereContains("FenLei","不限");

        List<BmobQuery<Way>> queries = new ArrayList<BmobQuery<Way>>();
        queries.add(eq2);
        queries.add(eq1);
        queries.add(eq3);
        BmobQuery<Way> mainQuery = new BmobQuery<Way>();

        mainQuery.or(queries);
        //mainQuery.addWhereContains("FenLei","不限");
        mainQuery.findObjects(new FindListener<Way>() {
            @Override
            public void done(List<Way> object, BmobException e) {
                if(e==null){
                    Log.d("早餐界面1", "测试点5"+"查询早成功");
                    Log.d("早餐界面1", "测试点5"+"查询结果有"+object.size()+"个");
                    //toast("查询结果有"+object.size()+"个");
                    AllNU=object.size();
                    for (Way identification : object) {
                        t=t+1;
                        final String LinShi_s = identification.getObjectId();
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
        /*
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("1");
        }
        */
        //mRecyclerView.setAdapter(new Adapter(list));
        return v;
    }
    private void initWay(String objId){
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId",objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("早餐界面", "测试点5"+"存储objectId成功");
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
                            Log.d("早餐界面", "测试点5"+"查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                try{
                                    panduan = subObject.get("FromUser").getAsBoolean();
                                }catch (Exception a){
                                    panduan = false;
                                }
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
                                Way way = new Way();
                                way.setObjid(Way_objectID);
                                way.setZuoFaMing(ZuoFaMing);
                                way.setZuoFaTu(ZuoFaTu);
                                wayList.add(way);
                                mRecyclerView.setAdapter(new Adapter(wayList));
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
        private TextView mTextView;
        private View mWholeView;
        private Way ss;
        public holder(View itemView) {
            super(itemView);
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
            //mImageView.setImageResource(R.drawable.food_test_1);
            //mTextView.setText("干炒青菜");
            mTextView.setText(s.getZuoFaMing());
            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",ss.getObjid());
            editor.apply();
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID",ss.getObjid());
            startActivity(i);
        }
    }


    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<holder> {
        List<Way> mStringList;

        public Adapter(List<Way> list) {
            mStringList = list;
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.recommend_breakfast_recycler_view_item, parent, false);
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
