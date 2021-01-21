package com.enseirb.collusioncontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView contactListView;
    ArrayList<Contact> contacts;
    ArrayList<PhoneNumber> numbers;
    String [] appPermissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
    };
    ContactAdapter contactAdapter;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactListView = findViewById(R.id.contactlstview);
        contacts = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(this, contacts);
        contactListView.setAdapter(contactAdapter);
        if (checkAndRequestPermissions()){
            getContacts();
        }
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

    public boolean checkAndRequestPermissions(){
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String perm : appPermissions){
            if (ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(perm);
            }
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
