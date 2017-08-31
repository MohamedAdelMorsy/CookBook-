package com.scorpiomiku.cookbook.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/31.
 */

public class UserDataHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String TAG = "UserDataHelper";
    public static final String CREAT_USER = "create table Basket (" +
            "id integer primary key autoincrement, " +
            "email text, " +
            "passwords text)";

    public UserDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
