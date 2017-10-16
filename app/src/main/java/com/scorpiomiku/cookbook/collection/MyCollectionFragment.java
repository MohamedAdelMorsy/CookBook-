package com.scorpiomiku.cookbook.collection;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.bean.Collection;
import com.scorpiomiku.cookbook.combination.Way;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.module.FragmentModule;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/6/14.
 */

public class MyCollectionFragment extends FragmentModule {

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private List<CBLEC> mCblecList = new ArrayList<>();
    private SwipeMenuAdapter mAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String CommentId;
    //private String ZuoFaTu;
    private Boolean panduan;
    void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public static MyCollectionFragment newInstance() {
        return new MyCollectionFragment();
    }

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

                                                        Log.d("收藏界面", "测试点5"+"图片加载成功");
                                                        mCblecList.add(new CBLEC(ZuoFaMing, finalZuoFaTu, "这里是介绍", yongliao[0], yongliao[1], yongliao[2], yongliao[3], yongliao[4], yongliao[5], yongliao[6], yongliao[7], yongliao[8], yongliaoliang[0], yongliaoliang[1], yongliaoliang[2], yongliaoliang[3], yongliaoliang[4], yongliaoliang[5], yongliaoliang[6], yongliaoliang[7], yongliaoliang[8],Way_objectID,panduan));
                                                        //*/
                                                        Log.d("收藏界面", "测试点5"+"放入对象成功");
                                                        mSwipeMenuRecyclerView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                mAdapter = new Adapter(mCblecList);
                                                                Log.d("收藏界面", "测试点5"+"ada创建成功" );
                                                                mAdapter.notifyDataSetChanged();
                                                                mSwipeMenuRecyclerView.setAdapter(mAdapter);
                                                                //recyclerView.refreshComplete();
                                                                //System.out.println("Runnable thread id " + Thread.currentThread().getId());
                                                                mAdapter.notifyDataSetChanged();
                                                                //adapter.notifyDataSetChanged();
                                                                //recyclerView.notifyAll();
                                                            }
                                                        });





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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout
                .colleciont_mycollections_swipe_recyclerview, container, false);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) v.findViewById(R.id
                .collection_my_collection_swipe_recyclerview);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        Bmob.initialize(getActivity(), "3bfd53d40a453ea66ce653ab658582d1");
        mSwipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mSwipeMenuRecyclerView.setNestedScrollingEnabled(false);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickLinstener);
        //开始获取收藏数据

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("user_objId", pref.getString("user_objId",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        ace.callEndpoint("get_commentByUserobjid", jsonObj, new CloudCodeListener() {
            public static final String TAG = "thing";
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result =object.toString();
                    if (object.toString().equals("{\"results\":[]}")){
                        toast("收藏为空");
                    }
                    JsonParser parser = new JsonParser();  //创建JSON解析器
                    JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                    JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject subObject = array.get(i).getAsJsonObject();
                        CommentId = subObject.get("objectId").getAsString();
                        //str_1= subObject.get("username").getAsString();
                        //editor.putString("url",subObject.get("email").getAsString());
                        BmobQuery<Way> www = new BmobQuery<>();
                        Collection comment =new Collection();
                        comment.setObjectId(CommentId);
                        www.addWhereRelatedTo("comment", new BmobPointer(comment));
                        www.findObjects(new FindListener<Way>() {
                            @Override
                            public void done(List<Way> object,BmobException e) {
                                if(e==null){
                                    Log.i("收藏界面"+"bmob","查询个数："+object.size());
                                    for (int i=0;i<object.size();i++){
                                        final Way way = object.get(i);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                initWay(way.getObjectId());
                                            }
                                        }).start();
                                    }

                                }else{
                                    Log.i("bmob","失败："+e.getMessage());
                                    toast("bmob"+"失败："+e.getMessage());
                                }
                            }
                        });
                    }
                } else {

                }//
            }
        });
        return v;
    }

    /*---------------------------------Holder------------------------------*/
    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mItemImageView;
        private TextView mItemNameTextView;
        private TextView mItemMatirialTextView;

        private CBLEC macblec;
        public ItemHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id
                    .collection_item_image_view);
            mItemNameTextView = (TextView) itemView.findViewById(R.id
                    .collection_item_food_name_text_view);
            mItemMatirialTextView = (TextView) itemView.findViewById(R.id
                    .collection_item_food_matirial_text_view);
        }

        //此处添加数据
        private void bindView(CBLEC mcblec) {
            macblec = mcblec;
            Log.d("收藏界面", "bindView: 开始加载图片");
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.test_food);
            Glide.with(getActivity())
                    .load(mcblec.getImageurl())
                    .apply(options)
                    .into(mItemImageView);
            Log.d("收藏界面", "bindView: 加载图片完成");
            mItemNameTextView.setText(mcblec.getName());
            mItemMatirialTextView.setText(mcblec.getShiCaiMing1()+mcblec.getShiCaiMing2()+mcblec.getShiCaiMing3()+mcblec.getShiCaiMing4()+mcblec.getShiCaiMing5()+mcblec.getShiCaiMing6()+mcblec.getShiCaiMing7()+mcblec.getShiCaiMing8()+mcblec.getShiCaiMing9());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editor.putString("LS_Way_objectID",macblec.getObjId());
            editor.apply();
            Intent i = new Intent(getActivity(), MenuActivity.class);
            i.putExtra("Way_objectID",macblec.getObjId());
            macblec.getObjId();
            startActivity(i);
        }
    }


    /*--------------------------------Adapter-----------------------------*/
    private class Adapter extends SwipeMenuAdapter<ItemHolder> {
        private List<CBLEC> mList;

        public Adapter(List<CBLEC> list) {
            super();
            mList = list;
        }

        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.collection_recycler_view_item, parent, false);
            return v;
        }

        @Override
        public ItemHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            return new ItemHolder(realContentView);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    /*--------------------------------------MenuCreator----------------------*/
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.collection_item_width);

            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem closeItem = new SwipeMenuItem(getContext())
                    .setBackgroundDrawable(R.drawable.delete_color)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(R.color.colorRed)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem);

        }
    };

    /*--------------------------------------------menuItemClickLitener----------------------------*/
    private OnSwipeMenuItemClickListener menuItemClickLinstener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();
            if (menuPosition == 0) {
                Log.d("收藏界面", "onItemClick: adapterPosition="+adapterPosition+"menuPosition="+menuPosition+"direction="+direction);
                closeable.smoothCloseMenu();
                Log.d("收藏界面", "onItemClick: 开始");
                CBLEC mbl = mCblecList.get(adapterPosition);
                Log.d("收藏界面", "onItemClick: mbl完成");
                Log.d("收藏界面", "onItemClick: adapterPosition="+adapterPosition);
                String str=mbl.getObjId();
                //final String str = ((TextView) view.findViewById(R.id.cblec_Name)).getTag().toString();
                Log.d("收藏界面", "onItemClick: str" +
                        "获取完成");
                Collection comment_2 = new Collection();
                Way ada = new Way();
                ada.setObjectId(str);
                BmobRelation relation = new BmobRelation();
                relation.remove(ada);
                comment_2.setObjectId(CommentId);
                comment_2.setComment(relation);
                Log.d("收藏界面", "onItemClick: 数据填入完成");
                comment_2.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","关联关系删除成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage());
                        }
                    }

                });
                //mStringList.remove(adapterPosition);
                mCblecList.remove(adapterPosition);
                //报错语句
                mAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

}
