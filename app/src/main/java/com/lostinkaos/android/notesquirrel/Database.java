package com.lostinkaos.android.notesquirrel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by keya on 25/7/15.
 */
public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table POINTS ( ID INTEGER PRIMARY KEY, X INTEGER NOT NULL, Y INTEGER NOT NULL )";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
