package com.scorpiomiku.cookbook.basket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
import com.scorpiomiku.cookbook.bean.Basket;
import com.scorpiomiku.cookbook.sql.BasketDataHelper;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private List<Basket> mList = new ArrayList<>();
    private BasketAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;
    private BasketDataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.basket_swipe_menu_recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.basket_floating_action_button);
        dbHelper = new BasketDataHelper(this, "BasketStore.db", null, 1);
        initData();
        mAdapter = new BasketAdapter(mList);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
        mSwipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mSwipeMenuRecyclerView.setNestedScrollingEnabled(false);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickLinstener);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BasketActivity.this, CreateNewBasketActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnName = data.getStringExtra("name_return");
                    String returnMaterial = data.getStringExtra("material_return");
                    mAdapter.addItem(0,new Basket(returnName, returnMaterial));
                    Log.d("Test", "onActivityResult: " +
                            "ok");
                }
                break;
            default:
        }
    }

    /*-----------------------------Holder-------------------------*/

    private class BasketHolder extends RecyclerView.ViewHolder {

        private ImageView mFoodImageView;
        private TextView mFoodNameTextView;
        private TextView mMaterialsTextView;

        public BasketHolder(View itemView) {
            super(itemView);
            mFoodImageView = (ImageView) itemView.findViewById(R.id.basket_item_iamge_view);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id.basket_item_food_name_text_view);
            mMaterialsTextView = (TextView) itemView.findViewById(R.id.basket_item_food_materials_text_view);
        }

        private void bindView(Basket b) {
            mFoodNameTextView.setText(b.getName());
            mFoodImageView.setImageResource(R.drawable.test_food);
            mMaterialsTextView.setText(b.getMaterial());
        }
    }

    /*---------------------------Adapter-------------------------*/
    private class BasketAdapter extends SwipeMenuAdapter<BasketHolder> {

        private List<Basket> mList;

        public BasketAdapter(List<Basket> list) {
            super();
            mList = list;
        }


        @Override
        public void onBindViewHolder(BasketHolder holder, int position) {
            holder.bindView(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(BasketActivity.this);
            View v = layoutInflater.inflate(R.layout.basket_recycler_view_item, parent, false);
            return v;
        }

        @Override
        public BasketHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            return new BasketHolder(realContentView);
        }

        private void addItem(int position, Basket b) {
            mList.add(position,b);
            Log.d("Test", "addItem: "+b.getName());
            notifyItemInserted(position);
        }
    }

    /*--------------------------------------MenuCreator----------------------*/
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.collection_item_width);

            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem closeItem = new SwipeMenuItem(BasketActivity.this)
                    .setBackgroundDrawable(R.color.colorRed)
                    .setText("删除")
                    .setTextColor(R.color.white)
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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Basket b = mList.get(adapterPosition);
                String deletName = b.getName();
                String deletMaterial = b.getMaterial();
                db.delete("Basket", "material == ?", new String[]{deletMaterial});
                mList.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

    /*-------------------------initData----------------------*/
    private void initData() {
        /*for (int i = 0; i < 2; i++) {
            mList.add(new Basket("糖醋里脊", "猪肉里脊\n" + "白糖两勺\n" + "大葱\n" + "麻辣酱\n"));
        }*/
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Basket", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Basket b = new Basket(cursor.getString(cursor.getColumnIndex("foodname")),
                        cursor.getString(cursor.getColumnIndex("material")));
                mList.add(b);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
