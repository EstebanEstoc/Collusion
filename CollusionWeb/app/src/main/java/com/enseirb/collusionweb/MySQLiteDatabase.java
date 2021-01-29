package com.enseirb.collusionweb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteDatabase extends SQLiteOpenHelper {
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS websites(" +
                                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                    "url VARCHAR(255) NOT NULL," +
                                                    "rating FLOAT NOT NULL );";

    public MySQLiteDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
