package com.scorpiomiku.cookbook.basket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.cookbook.R;
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
    private List<String> mList = new ArrayList<>();
    private BasketAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.basket_swipe_menu_recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 10; i++) {
            mList.add("1");
        }
        mAdapter = new BasketAdapter(mList);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
        mSwipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mSwipeMenuRecyclerView.setNestedScrollingEnabled(false);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickLinstener);
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

        private void bindView() {
            mFoodNameTextView.setText("糖醋里脊");
            mFoodImageView.setImageResource(R.drawable.test_food);
            mMaterialsTextView.setText("猪肉里脊\n" +
                    "白糖两勺\n" +
                    "大葱\n" +
                    "麻辣酱\n");
        }
    }

    /*---------------------------Adapter-------------------------*/
    private class BasketAdapter extends SwipeMenuAdapter<BasketHolder> {

        private List<String> mList;

        public BasketAdapter(List<String> list) {
            super();
            mList = list;
        }


        @Override
        public void onBindViewHolder(BasketHolder holder, int position) {
            holder.bindView();
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
    }

    /*--------------------------------------MenuCreator----------------------*/
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.collection_item_width);

            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem closeItem = new SwipeMenuItem(BasketActivity.this)
                    .setBackgroundDrawable(R.drawable.delete_color)
                    .setImage(R.drawable.delete_24dp)
                    .setText("删除")
                    .setTextColor(R.color.colorWhite)
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
                mList.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

}
