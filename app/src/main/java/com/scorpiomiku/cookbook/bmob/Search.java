package com.scorpiomiku.cookbook.bmob;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.FoodMaterials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/9/5.
 */

public class Search {
    public static int AllNU;
    public static int t;

    public static void findByMaterials(String mFoodName) {
        BmobQuery<FoodMaterials> eq1 = new BmobQuery<FoodMaterials>();
        eq1.addWhereEqualTo("yongLiao1", mFoodName);
        Log.d("显示食材", "搜索测试点1");
        BmobQuery<FoodMaterials> eq2 = new BmobQuery<FoodMaterials>();
        eq2.addWhereEqualTo("yongLiao2", mFoodName);
        BmobQuery<FoodMaterials> eq3 = new BmobQuery<FoodMaterials>();
        eq3.addWhereEqualTo("yongLiao3", mFoodName);
        BmobQuery<FoodMaterials> eq4 = new BmobQuery<FoodMaterials>();
        eq4.addWhereEqualTo("yongLiao4", mFoodName);
        BmobQuery<FoodMaterials> eq5 = new BmobQuery<FoodMaterials>();
        eq5.addWhereEqualTo("yongLiao5", mFoodName);
        BmobQuery<FoodMaterials> eq6 = new BmobQuery<FoodMaterials>();
        eq6.addWhereEqualTo("yongLiao6", mFoodName);
        BmobQuery<FoodMaterials> eq7 = new BmobQuery<FoodMaterials>();
        eq7.addWhereEqualTo("yongLiao7", mFoodName);
        BmobQuery<FoodMaterials> eq8 = new BmobQuery<FoodMaterials>();
        eq8.addWhereEqualTo("yongLiao8", mFoodName);
        BmobQuery<FoodMaterials> eq9 = new BmobQuery<FoodMaterials>();
        eq9.addWhereEqualTo("yongLiao9", mFoodName);
        List<BmobQuery<FoodMaterials>> queries = new ArrayList<BmobQuery<FoodMaterials>>();
        queries.add(eq1);
        queries.add(eq2);
        queries.add(eq3);
        queries.add(eq4);
        queries.add(eq5);
        queries.add(eq6);
        queries.add(eq7);
        queries.add(eq8);
        queries.add(eq9);
        BmobQuery<FoodMaterials> mainQuery = new BmobQuery<FoodMaterials>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<FoodMaterials>() {
            @Override
            public void done(List<FoodMaterials> object, BmobException e) {
                if (e == null) {
                    //toast("查询结果有"+object.size()+"个");
                    AllNU = object.size();
                    for (FoodMaterials identification : object) {
                        t = t + 1;
                        final String LinShi_s = identification.getIdentification();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initWay(LinShi_s);
                            }
                        }).start();
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void initWay(String objId) {
        final JSONObject jas = new JSONObject();
        final AsyncCustomEndpoints ace1 = new AsyncCustomEndpoints();
        try {
            jas.put("objectId", objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("initWay", "测试点5" + "存储objectId成功");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ace1.callEndpoint("get_way_fromShiCai", jas, new CloudCodeListener() {
                    public static final String TAG = "thing";

                    @Override
                    public void done(Object object, BmobException e) {
                        if (e == null) {
                            String result = object.toString();
                            JsonParser parser = new JsonParser();  //创建JSON解析器
                            JsonObject ssobject = (JsonObject) parser.parse(result);  //创建JsonObject对象//将json数据转为为boolean型的数据
                            JsonArray array = ssobject.get("results").getAsJsonArray();    //得到为json的数组
                            Log.d("initWay", "测试点5" + "查询Way成功");
                            for (int i = 0; i <= array.size(); i++) {
                                final JsonObject subObject = array.get(i).getAsJsonObject();
                                JsonObject ZuoFaTu = subObject.get("zuoFaTu").getAsJsonObject();
                                final String Way_objectID = subObject.get("objectId").getAsString();
                                try {
                                    int cishu = subObject.get("cishu").getAsInt();
                                } catch (Exception d) {
                                    int cishu = 0;
                                }
                                //int cishu = subObject.get("cishu").getAsInt();
                                final String ZuoFaMing = subObject.get("zuoFaMing").getAsString();
                                final String ZuoFaTuUrl = ZuoFaTu.get("url").getAsString();
                                final String ZuoFaTuMing = ZuoFaTu.get("filename").getAsString();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //identification
                                        try {
                                            jas.put("identification", Way_objectID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("initWay", "测试点5" + "开始查询食材部分");
                                        ace1.callEndpoint("get_shicai", jas, new CloudCodeListener() {
                                            public static final String TAG = "thing";

                                            @Override
                                            public void done(Object object, BmobException e) {
                                                if (e == null) {
                                                    final String result = object.toString();
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
                                                        try {
                                                            ShiCaiLiang = subObject.get("ShiCaiLiang").getAsInt();
                                                        } catch (Exception e2) {
                                                            ShiCaiLiang = 0;
                                                        }
                                                        //int ShiCaiLiang = subObject.get("ShiCaiLiang").getAsInt();
                                                        for (int k = 0; k < ShiCaiLiang; k++) {
                                                            if (k == 0) {
                                                                yongliao[0] = subObject.get("yongLiao1").getAsString();
                                                                yongliaoliang[0] = subObject.get("yongLiaoLiang1").getAsString();
                                                            } else if (k == 1) {
                                                                yongliao[1] = subObject.get("yongLiao2").getAsString();
                                                                yongliaoliang[1] = subObject.get("yongLiaoLiang2").getAsString();
                                                            } else if (k == 2) {
                                                                yongliao[2] = subObject.get("yongLiao3").getAsString();
                                                                yongliaoliang[2] = subObject.get("yongLiaoLiang3").getAsString();
                                                            } else if (k == 3) {
                                                                yongliao[3] = subObject.get("yongLiao4").getAsString();
                                                                yongliaoliang[3] = subObject.get("yongLiaoLiang4").getAsString();
                                                            } else if (k == 4) {
                                                                yongliao[4] = subObject.get("yongLiao5").getAsString();
                                                                yongliaoliang[4] = subObject.get("yongLiaoLiang5").getAsString();
                                                            } else if (k == 5) {
                                                                yongliao[5] = subObject.get("yongLiao6").getAsString();
                                                                yongliaoliang[5] = subObject.get("yongLiaoLiang6").getAsString();
                                                            } else if (k == 6) {
                                                                yongliao[6] = subObject.get("yongLiao7").getAsString();
                                                                yongliaoliang[6] = subObject.get("yongLiaoLiang7").getAsString();
                                                            } else if (k == 7) {
                                                                yongliao[7] = subObject.get("yongLiao8").getAsString();
                                                                yongliaoliang[7] = subObject.get("yongLiaoLiang8").getAsString();
                                                            } else if (k == 8) {
                                                                yongliao[8] = subObject.get("yongLiao9").getAsString();
                                                                yongliaoliang[8] = subObject.get("yongLiaoLiang9").getAsString();
                                                            } else if (k == 9) {
                                                                yongliao[9] = subObject.get("yongLiao10").getAsString();
                                                                yongliaoliang[9] = subObject.get("yongLiaoLiang10").getAsString();
                                                            }
                                                        }
                                                        Log.d("initWay", "测试点5" + "查询食材成功");
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Drawable CBLdrawable = loadImageFromNetwork(ZuoFaTuUrl, ZuoFaTuMing);
                                                                Log.d("initWay", "测试点5" + "图片加载成功");
                                                                mCookBookList.add(new CBLEC(ZuoFaMing, CBLdrawable, "这里是介绍", yongliao[0], yongliao[1], yongliao[2], yongliao[3], yongliao[4], yongliao[5], yongliao[6], yongliao[7], yongliao[8], yongliaoliang[0], yongliaoliang[1], yongliaoliang[2], yongliaoliang[3], yongliaoliang[4], yongliaoliang[5], yongliaoliang[6], yongliaoliang[7], yongliaoliang[8], Way_objectID));
                                                                //*/
                                                                Log.d("initWay", "测试点5" + "放入对象成功");
                                                                recyclerView.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        final CBLAdapter adapter = new CBLAdapter(this, mCookBookList);
                                                                        Log.d("initWay", "测试点5" + "ada创建成功");
                                                                        adapter.notifyDataSetChanged();
                                                                        recyclerView.setAdapter(adapter);
                                                                        //recyclerView.refreshComplete();
                                                                        //System.out.println("Runnable thread id " + Thread.currentThread().getId());

                                                                        adapter.setOnRecyclerViewListener(new CBLAdapter.OnRecyclerViewListener() {
                                                                            @Override
                                                                            public void onItemClick(View view, int position) {
                                                                                String str = ((TextView) view.findViewById(R.id.cblec_Name)).getTag().toString();
                                                                                //toast(str);
                                                                                //   toast(str);
                                                                                Intent intent = new Intent(CookBookListActivity.this, CookPrativeActivity.class);
                                                                                intent.putExtra("WayObjId", str);
                                                                                intent.putExtra("user_objId", userObjId);
                                                                                intent.putExtra("FangFaMing", "get_way");
                                                                                intent.putExtra("data", msg);
                                                                                intent.putExtra("MethedJudge", MethedJudge);
                                                                                startActivityForResult(intent, 100);
                                                                                //startActivity(intent);
                                                                                //另加返回值
                                                                            }

                                                                            @Override
                                                                            public boolean onItemLongClick(View view, final int position) {
                                                                                if (MethedJudge == 2) {
                                                                                    PopupMenu popupMenu;
                                                                                    final Menu menu;
                                                                                    String str = ((TextView) view.findViewById(R.id.cblec_Name)).getTag().toString();
                                                                                    popupMenu = new PopupMenu(CookBookListActivity.this, findViewById(R.id.cblec_recycler_view));
                                                                                    menu = popupMenu.getMenu();
                                                                                    // 通过代码添加菜单项
                                                                                    menu.add(Menu.NONE, Menu.FIRST + 0, 0, "删除");
                                                                                    popupMenu.show();
                                                                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                                        @Override
                                                                                        public boolean onMenuItemClick(MenuItem item) {
                                                                                            switch (item.getItemId()) {
                                                                                                case Menu.FIRST + 0:
                                                                                                    Toast.makeText(CookBookListActivity.this, "删除",
                                                                                                            Toast.LENGTH_LONG).show();
                                                                                                    mCookBookList.remove(position);
                                                                                                    adapter.notifyItemRemoved(position);
                                                                                                    adapter.notifyItemRangeChanged(0, mCookBookList.size() - position);
                                                                                                    adapter.notifyDataSetChanged();
                                                                                                    ///adapter.removeData(position);
                                                                                                    break;
                                                                                                default:
                                                                                                    break;
                                                                                            }
                                                                                            return false;
                                                                                        }
                                                                                    });
                                                                                }
                                                                                return false;
                                                                            }
                                                                        });
                                                                        adapter.notifyDataSetChanged();
                                                                        recyclerView.setAdapter(adapter);
                                                                        //adapter.notifyDataSetChanged();
                                                                        //recyclerView.notifyAll();
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

                        }
                    }
                });
            }
        }).start();
    }
    public static Drawable loadImageFromNetwork(String urladdr, String Imagename) {
        // TODO Auto-generated method stub
        Drawable drawable = null;
        try {
            //judge if has picture locate or not according to filename
            drawable = Drawable.createFromStream(new URL(urladdr).openStream(), Imagename);
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }
        return drawable;
    }
}
