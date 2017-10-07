package com.scorpiomiku.cookbook.classifierresultactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.FoodMaterials;
import com.scorpiomiku.cookbook.mainactivity.ContainActivity;
import com.scorpiomiku.cookbook.menuactivity.MenuActivity;
import com.scorpiomiku.cookbook.recommend.RecommendDefultFragment;
import com.scorpiomiku.cookbook.tensorflow.Classifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

public class ClassifierResultActivity extends AppCompatActivity {

    private ImageView mTestImageView;


    private List<String> list = new ArrayList<>();

    private Adapter mAdapter = new Adapter(list);
    boolean isLoading;
    private Handler handler = new Handler();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private String mPicturePath;
    private String mFoodName;
    private RecyclerView mClassifierRecyclerView;

    private int[] test = new int[1];
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mImageView;
    private TextView mDescribeTextView;
    private TextView mNutritionTextView;
    private TextView mFoodNameTextView;

    private String mFragmentSendMessage;

    private Intent i;

    private int AllNU;
    private int t;


    private static final String TAG = "ClassifierResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier_result);
        i = getIntent();
        mFragmentSendMessage = i.getStringExtra("FragmentSendMessage");

        mClassifierRecyclerView = (RecyclerView) findViewById(R.id.classifier_result_recyclerview);
        mImageView = (ImageView) findViewById(R.id.classifier_result_imageview);
        mDescribeTextView = (TextView) findViewById(R.id.classifier_result_describe_textview);
        mNutritionTextView = (TextView) findViewById(R.id.nutrition_know_text_view);
        mFoodNameTextView = (TextView) findViewById(R.id.classifier_result_food_name);

        mLinearLayoutManager = new LinearLayoutManager(this);


        initData();
        setRefresh();
       
        /*CameraActivity  RecommendFragment  CombinationFragment*/
        chooseInit();
        //Log.d(TAG, "onCreate: " + mPicturePath + "    " + mFoodName);
    }

    /*---------------------------RecommendFragment-----------------------------*/
    private void recommendInitView() {
        mFoodName = i.getStringExtra("foodname");
        initUrlView();
    }

    /*-----------------------------TakePhotoAndClassifier---------------------------*/
    private void cameraInitView() {
        mPicturePath = i.getStringExtra("picturePath");
        mFoodName = i.getStringExtra("foodname");
        test = i.getIntArrayExtra("pictureResult");
        Log.d(TAG, "cameraInitView: " + test);
        initUrlView();
    }

    /*---------------------------whichFragmentSendMassege------------------------*/
    private void chooseInit() {
        if (mFragmentSendMessage.equals("RecommendFragment")) {
            Log.d(TAG, "chooseInit: RecommendFragment");
            recommendInitView();
        }
        if (mFragmentSendMessage.equals("CameraActivity")) {
            Log.d(TAG, "chooseInit: CameraActivity");
            cameraInitView();
        }
        if (mFragmentSendMessage.equals("CombinationFragment")) {
            Log.d(TAG, "chooseInit: CombinationFragment");
        }
    }
    /*---------------------------initUrlView--------------------------*/

    private void initUrlView() {
        mImageView.setImageResource(R.drawable.changyutest);
        mFoodNameTextView.setText(mFoodName);
        mDescribeTextView.setText("   鲳鱼属于鲈形目，鲳科。体短而高，极侧扁，略呈菱形。");
        mNutritionTextView.setText("   鲳鱼含有多种营养。100克鱼肉含蛋白质15.6克，脂肪6.6克，碳水化合物0.2克" +
                "，钙19毫克，磷240毫克，铁0.3毫克。含有丰富的不饱和脂肪酸，" +
                "有降低胆固醇的功效；含有丰富的微量元素硒和镁，对冠状动脉硬化等心血管疾病有预防作用，" +
                "并能延缓机体衰老，预防癌症的发生。\n");
    }

    /*-------------------------------------holder------------------------------*/
    private class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mFoodNameTextView;
        private TextView mFoodMatirialTextView;
        private View mWholeView;


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

        private void bindView(int i, String s1, String s2) {
            if (i == 0) {
                mImageView.setImageResource(R.drawable.changyucai1);
            }
            if (i == 1) {
                mImageView.setImageResource(R.drawable.changyucai2);
            }
            if (i != 0 && i != 1) {
                mImageView.setImageResource(R.drawable.test_food);
            }
            mFoodNameTextView.setText(s1);
            mFoodMatirialTextView.setText(s2);
            mWholeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(ClassifierResultActivity.this, MenuActivity.class);
            startActivity(i);
        }
    }

    /*-----------------------------------------adapter--------------------------*/
    private class Adapter extends RecyclerView.Adapter<holder> {
        List<String> mStringList;

        List<String> Names = new ArrayList<>();
        List<String> Metirals = new ArrayList<>();

        public Adapter(List<String> list) {
            mStringList = list;
            Names.add("清蒸鲳鱼");
            Names.add("一品鲳鱼");
            Names.add("红烧鲳鱼");
            Metirals.add("胡萝卜半根、花生油2大勺、食盐适量、姜1块、料酒2勺、大葱1棵、胡椒粉1勺、蒸鱼豉油2大勺");
            Metirals.add("酱油1勺、蒜适量、料酒1勺、香油适量、淀粉适量、白糖1勺、咖喱粉少许、胡椒粉少许");
            Metirals.add("红尖椒1个、色拉油适量、食盐适量、姜1小块、蒜1瓣、料酒15毫升、生抽15毫升、老抽5毫升、淀粉10克、小葱1根");
        }

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(ClassifierResultActivity.this).inflate(R.layout
                        .recommend_default_recycler_view_item, parent, false);
                return new holder(v);
            } else if (viewType == TYPE_FOOTER) {
                View v = LayoutInflater.from(ClassifierResultActivity.this).inflate(R.layout
                        .item_foot, parent, false);
                return new holder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {

            if (position + 1 == getItemCount()) {

            } else {
                holder.bindView(position, Names.get(position), Metirals.get(position));
            }
        }


        @Override
        public int getItemCount() {
            return mStringList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    private void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);
    }

    private void getData() {
        for (int i = 0; i < 6; i++) {
            list.add("1");
        }
        mAdapter.notifyDataSetChanged();
        //RecommendFragment.mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
    }

    private void setRefresh() {
        /*RecommendFragment.mSwipeRefreshLayout.setColorSchemeResources(R.color.toolbar_and_menu_color);
        RecommendFragment.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                RecommendFragment.mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        RecommendFragment.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: clear");
                        list.clear();
                        getData();
                    }
                }, 2000);
            }
        });*/

        mClassifierRecyclerView.setLayoutManager(mLinearLayoutManager);
        mClassifierRecyclerView.setNestedScrollingEnabled(false);
        mClassifierRecyclerView.setAdapter(mAdapter);
        mClassifierRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "在滑动");
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                Log.d("test", "onScrolled:最后一个可见的位子 " + lastVisibleItemPosition);
                Log.d("test", "onScrolled: adapter" + mAdapter.getItemCount());
                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    Log.d("test", "loading executed在加载新的");

                    /*boolean isRefreshing = RecommendFragment.mSwipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                        return;
                    }*/
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ClassifierResultActivity.this, ContainActivity.class);
        startActivity(i);
    }
}
