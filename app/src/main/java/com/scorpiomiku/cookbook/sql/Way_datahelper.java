package com.scorpiomiku.cookbook.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a on 2017/10/6.
 */

public class Way_datahelper extends SQLiteOpenHelper {

    public static final String Way_objectID = "create table Way_objectID (" +
            "id integer primary key autoincrement, "
            + "Way_objectID text)";
    private Context mContext;
    public Way_datahelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Way_objectID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
