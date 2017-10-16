package com.scorpiomiku.cookbook.sql;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/7/15.
 */

public class BasketDataHelper extends SQLiteOpenHelper {
    private static final String TAG = "BasketDataHelper";

    public static final String CREAT_BOOK = "create table Basket (" +
            "id integer primary key autoincrement, " +
            "material text, " +
            "foodname text)";

    private Context mContext;

    public BasketDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_BOOK);
        Log.d(TAG, "basketSQL create successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
