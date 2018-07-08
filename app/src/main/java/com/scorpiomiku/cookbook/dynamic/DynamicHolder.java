package com.scorpiomiku.cookbook.dynamic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.CBLEC;
import com.scorpiomiku.cookbook.bean.Collection;
import com.scorpiomiku.cookbook.bean.Person;
import com.scorpiomiku.cookbook.bean.Post;
import com.scorpiomiku.cookbook.combination.Way;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DynamicHolder extends RecyclerView.ViewHolder {
    private ImageView mAccountImageView;
    private TextView mAccountTextView;
    private TextView mTimeTextView;
    private TextView mDescribtionTextView;
    private ImageView mPhotoImageView;
    private ImageView mPraiseImageView;
    private ImageView mCollectionImageView;
    private Context mContext;
    private CBLEC cblec1;
    public DynamicHolder(View itemView) {
        super(itemView);
        mAccountImageView = (ImageView) itemView.findViewById(R.id.dynamic_account_iamge_view_all);
        mAccountTextView = (TextView) itemView.findViewById(R.id.dynamic_account_text_view_all);
        mTimeTextView = (TextView) itemView.findViewById(R.id.dynamic_time_text_view_all);
        mDescribtionTextView = (TextView) itemView.findViewById(R.id.dynamic_description_text_view_all);
        mPhotoImageView = (ImageView) itemView.findViewById(R.id.dynamic_photo_iamge_view_all);
        mPraiseImageView = (ImageView) itemView.findViewById(R.id.dynamic_praise_image_view_all);
        mCollectionImageView = (ImageView) itemView.findViewById(R.id.dynamic_collection_iamge_view_all);
    }

    public void bindView(final CBLEC cblec, Context s) {
        mContext = s;
        cblec1 = cblec;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher);
        Glide.with(mContext)
                .load(cblec.getImageurl())
                .apply(options)
                .into(mPhotoImageView);
        mAccountImageView.setImageResource(R.drawable.backtest);
        mAccountTextView.setText(cblec.getName());
        mTimeTextView.setText("当前"+cblec1.getGeshu()+"人点赞" );
        mDescribtionTextView.setText("cblec.getIntroduce()");
        //mPhotoImageView.setImageResource(R.drawable.food_test_1);
        mPraiseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post p = new Post();
                p.setGeshu(cblec.getGeshu()+1);
                p.update(cblec1.getPostId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage());
                        }
                    }

                });
                mPraiseImageView.setImageResource(R.drawable.dynamic_clickedpraise);

            }
        });
        mCollectionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Person user = BmobUser.getCurrentUser(Person.class);
                mCollectionImageView.setImageResource(R.drawable.dynamic_collected);
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("user_objId",user.getObjectId() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                Log.d("收藏部分", "ace设置完成");

                ace.callEndpoint("get_commentByUserobjid", jsonObj, new CloudCodeListener() {

                    @Override

                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            Log.d("收藏部分e == null", "done: "+object.toString());
                            if (object.toString().equals("{\"results\":[]}")){
                                //即表是空的
                                Log.d("收藏部分e == null", "done: "+object.toString()+"str2是空的");
                                Collection comment = new Collection();
                                BmobRelation relation = new BmobRelation();
                                comment.setObjectId(user.getObjectId());
                                Way way = new Way();
                                way.setObjectId(cblec.getObjId());
                                relation.add(way);
                                comment.setUserObjID(user.getObjectId());
                                comment.setComment(relation);
                                comment.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId, BmobException e) {
                                        if(e==null){
                                            //toast("创建数据成功：" + objectId);
                                        }else{
                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }else{
                                String result =object.toString();
                                JsonParser parser = new JsonParser();  //创建JSON解析器
                                JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                                JsonArray array = ssobject.get("results").getAsJsonArray();
                                //得到为json的数组
                                for (int i = 0; i < array.size(); i++) {
                                    JsonObject subObject = array.get(i).getAsJsonObject();
                                    String CommentId = subObject.get("objectId").getAsString();
                                    Log.d("主函数", "检查点"+"数据加载完成");
                                    Collection comment = new Collection();
                                    BmobRelation relation = new BmobRelation();
                                    comment.setObjectId(CommentId);
                                    Way way = new Way();
                                    way.setObjectId(cblec.getObjId());
                                    relation.add(way);
                                    comment.setComment(relation);
                                    comment.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                //toast("成功");
                                                Log.i("bmob","多对多关联添加成功");
                                            }else{
                                                Log.i("bmob","失败："+e.getMessage());
                                                //toast("失败："+e.getMessage());
                                            }
                                        }

                                    });
                                }
                            }
                        } else {

                        }//
                    }
                });
            }
        });
    }
}
