package com.enseirb.collusionweb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayWebsitesActivity extends AppCompatActivity {
    private static WebsitesDatabase websites_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_websites);
        websites_db = new WebsitesDatabase(this);
        TextView text = findViewById(R.id.websites_list_text);

        websites_db.open();
        Cursor cursor = websites_db.getAllOrderedByRating();

        if (cursor.getCount() == 0) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
        }

        ListView websites_view = findViewById(R.id.websites_listview);
        WebsiteCursorAdapter adapter = new WebsiteCursorAdapter(
                this, R.layout.website_layout, cursor, 0 );
        websites_view.setAdapter(adapter);

        websites_db.close();
    }
}