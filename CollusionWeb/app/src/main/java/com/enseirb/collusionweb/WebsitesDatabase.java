package com.enseirb.collusionweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WebsitesDatabase {
    private final static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private MySQLiteDatabase mySQLiteDatabase;

    public WebsitesDatabase(Context context) {
        mySQLiteDatabase = new MySQLiteDatabase(context, "websites", null, DB_VERSION);
    }

    public void open() {
        db = mySQLiteDatabase.getWritableDatabase();
        if (db == null) {
            System.out.println("Database does not exist");
        }
    }

    public void close() {
        db.close();
    }

    public long insertWebsite(Website website) {
        ContentValues values = new ContentValues();
        values.put("url", website.getUrl());
        values.put("rating", website.getRating());
        return db.insert("websites", null, values);
    }

    public float getRatingByUrl(String url) {
        Cursor cursor = db.rawQuery("SELECT * FROM websites WHERE url LIKE ?", new String[] {url});
        if (cursor.getCount() == 0) {
            return -1;
        }
        cursor.moveToFirst();
        float rating = cursor.getFloat(cursor.getColumnIndex("rating"));
        cursor.close();
        return rating;
    }

    public void updateRatingByUrl(String url, float rating) {
        db.execSQL("UPDATE websites SET rating=? WHERE url LIKE ?;", new String[] {String.valueOf(rating), url});
    }

    public Cursor getAllOrderedByRating() {
        Cursor cursor = db.rawQuery("SELECT * FROM websites ORDER BY rating DESC, url ASC", null) ;
        return cursor;
    }
}
