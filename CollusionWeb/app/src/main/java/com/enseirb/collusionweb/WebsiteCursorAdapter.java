package com.enseirb.collusionweb;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RatingBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class WebsiteCursorAdapter extends ResourceCursorAdapter {

    public WebsiteCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView url_tv = view.findViewById(R.id.list_display_url);
        RatingBar rating_rb = view.findViewById(R.id.list_display_rating);

        url_tv.setText(cursor.getString(cursor.getColumnIndex("url")));
        rating_rb.setRating(cursor.getFloat(cursor.getColumnIndex("rating")));
    }
}