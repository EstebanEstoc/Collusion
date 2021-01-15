package com.enseirb.collusioncontact;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Locale;


public class ContactAdapter extends ArrayAdapter<Contact> {
    private static final int CONTACT_LAYOUT = R.layout.contact_layout;
    private final Context context;
    private final ArrayList<Contact> contacts;

    public ContactAdapter(@NonNull Context currentContext, ArrayList<Contact> myContacts) {
        super(currentContext, CONTACT_LAYOUT, myContacts);
        context = currentContext;
        contacts = myContacts;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(CONTACT_LAYOUT, parent, false);
        }
        TextView displayName = convertView.findViewById(R.id.display_name);
        ImageView thumbnail = convertView.findViewById(R.id.contact_thumbnail);
        Bitmap image = contacts.get(position).getThumbnail();
        if (image != null){
            thumbnail.setImageBitmap(image);
        }
        displayName.setText(contacts.get(position).getName());
        TableLayout tableLayout = convertView.findViewById(R.id.numbertable);
        for (PhoneNumber number : contacts.get(position).getNumbers()) {
            TableRow row = new TableRow(context);
            View numberView = ((Activity) context).getLayoutInflater().inflate(R.layout.number_layout,row, false);
            TextView tetype = numberView.findViewById(R.id.type);
            TextView tenumber= numberView.findViewById(R.id.number);
            String stType = number.getTypeString();
            tetype.setText(stType);
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                tenumber.setText(PhoneNumberUtils.formatNumber(number.getNumber(), "FR"));
            }else {
                tenumber.setText(number.getNumber());
            }
            row.addView(numberView);
            tableLayout.addView(row);
        }
        return convertView;
    }

}