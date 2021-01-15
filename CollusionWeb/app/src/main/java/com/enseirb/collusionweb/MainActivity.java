package com.enseirb.collusionweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static WebsitesDatabase websites_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        websites_db = new WebsitesDatabase(this);
    }

    private boolean isUrlStartingCorrectly(Uri url) {
        String parsed_url = url.toString();
        return parsed_url.startsWith("http://") || parsed_url.startsWith("https://");
    }

    public void goToInternet(View view) {
        // Getting the url entered by user
        EditText et_url = findViewById(R.id.url);
        Uri web_page = Uri.parse(et_url.getText().toString());

        // If url does not start with http[s]://
        if (!isUrlStartingCorrectly(web_page)) {
            // Display a warning toast
            Context context = getApplicationContext();
            CharSequence text = "URL should start with http:// or https://";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Redirecting to url on internet
            Intent intent = new Intent(Intent.ACTION_VIEW, web_page);
            startActivity(intent);
        }
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

    public void displayWebsiteRating(View view) {
        EditText et_url = findViewById(R.id.url);
        LinearLayout rating_section = findViewById(R.id.display_rating_section);
        TextView rating_text = findViewById(R.id.display_rating_text);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        websites_db.open();
        float rating = websites_db.getRatingByUrl(et_url.getText().toString());
        websites_db.close();

        rating_section.setVisibility(View.VISIBLE);

        if (rating >= 0) {
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(rating);
            rating_text.setText("You rated " + et_url.getText().toString() + " :");
        } else {
            ratingBar.setVisibility(View.GONE);
            rating_text.setText("You did not rate " + et_url.getText().toString());
        }
    }
}