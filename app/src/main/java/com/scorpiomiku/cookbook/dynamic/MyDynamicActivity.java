package com.scorpiomiku.cookbook.dynamic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scorpiomiku.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class MyDynamicActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_dynamic_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 10; i++) {
            mList.add("1");
        }
        DynamicAdapter dynamicAdapter = new DynamicAdapter(mList,this);
        mRecyclerView.setAdapter(dynamicAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
