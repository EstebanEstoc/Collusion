package com.enseirb.collusioncontact;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.service.controls.templates.ControlButton;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
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
    private ArrayList<Contact> filteredContacts;

    public ContactAdapter(@NonNull Context currentContext, ArrayList<Contact> myContacts) {
        super(currentContext, CONTACT_LAYOUT, myContacts);
        context = currentContext;
        contacts = myContacts;
        filteredContacts = myContacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(CONTACT_LAYOUT, parent, false);
        }
        TextView displayName = convertView.findViewById(R.id.display_name);
        ImageView thumbnail = convertView.findViewById(R.id.contact_thumbnail);
        Bitmap image = getItem(position).getThumbnail();
        if (image != null) {
            thumbnail.setImageBitmap(image);
        }
        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(context, phoneNumbers);
        displayName.setText(getItem(position).getName());
        ListView phoneNumbersListView = convertView.findViewById(R.id.numbertable);
        phoneNumbersListView.setAdapter(phoneNumberAdapter);
        phoneNumbers.addAll(getItem(position).getNumbers());
        phoneNumberAdapter.notifyDataSetChanged();
        return convertView;
    }

    @Nullable
    @Override
    public Contact getItem(int position) {
        return filteredContacts.get(position);
    }

    @Override
    public int getCount() {
        return filteredContacts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = contacts;
                    results.count = contacts.size();
                } else {
                    ArrayList<Contact> filterResultsData = new ArrayList<Contact>();

                    for (Contact data : contacts) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if (data.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredContacts = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}