package com.enseirb.collusionweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity {

    private static WebsitesDatabase websites_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        websites_db = new WebsitesDatabase(this);
    }

    public void goToInternet(View view) {
        // Getting the url entered by user
        EditText et_url = findViewById(R.id.url);
        Uri web_page = Uri.parse(et_url.getText().toString());

        // Redirecting to url on internet
        Intent intent = new Intent(Intent.ACTION_VIEW, web_page);
        startActivity(intent);
    }


    public void addWebsite(View view) {
        EditText et_url = findViewById(R.id.url);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        Website website = new Website(et_url.getText().toString(), ratingBar.getRating());

        websites_db.open();
        websites_db.insertWebsite(website);
        websites_db.close();

        et_url.setText("");
        ratingBar.setRating(0);
    }

    public void printWebsiteRating(View view) {
        EditText et_url = findViewById(R.id.url);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        websites_db.open();
        float rating = websites_db.getRatingByUrl(et_url.getText().toString());
        websites_db.close();

        if (rating >= 0) {
            ratingBar.setRating(rating);
        }
    }
}