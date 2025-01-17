
package com.enseirb.collusioncontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ListView contactListView;
    ArrayList<Contact> contacts;
    ArrayList<PhoneNumber> numbers;
    String[] appPermissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
    };
    ContactAdapter contactAdapter;
    EditText inputSearch;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactListView = findViewById(R.id.contactlstview);
        contacts = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(this, contacts);
        contactListView.setAdapter(contactAdapter);
        if (checkAndRequestPermissions()) {
            getContacts();
        }

        inputSearch = (EditText) findViewById(R.id.search_box);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                contactAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getContacts();
    }

    private void getContacts() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                ArrayList<PhoneNumber> numbers = getPhones(contactId);
                contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), uriToBitmap(this, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))), numbers));
            } while (cursor.moveToNext());
            contactAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    private ArrayList<PhoneNumber> getPhones(String contactId) {
        ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
        ContentResolver contentResolver = getContentResolver();
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        if (phones.moveToFirst()) {
            do {
                numbers.add(new PhoneNumber(phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)), phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
            } while (phones.moveToNext());
        }
        phones.close();
        return numbers;
    }

    public static Bitmap uriToBitmap(Context ctx, String uri) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.parse(uri));
            } catch (IOException e) {
                // Do nothing
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public boolean checkAndRequestPermissions() {
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(perm);
            }
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
