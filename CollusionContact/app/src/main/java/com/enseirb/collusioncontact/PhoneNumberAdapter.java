package com.enseirb.collusioncontact;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class PhoneNumberAdapter extends ArrayAdapter<PhoneNumber> {
    private static final int NUMBER_LAYOUT = R.layout.number_layout;
    private final Context context;
    private final ArrayList<PhoneNumber> phoneNumbers;

    public PhoneNumberAdapter(@NonNull Context currentContext, ArrayList<PhoneNumber> myPhoneNumbers) {
        super(currentContext, NUMBER_LAYOUT, myPhoneNumbers);
        context = currentContext;
        phoneNumbers = myPhoneNumbers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(NUMBER_LAYOUT, parent, false);
        }
        TextView tetype = convertView.findViewById(R.id.type);
        TextView tenumber = convertView.findViewById(R.id.number);
        String stType = phoneNumbers.get(position).getTypeString();
        tetype.setText(stType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tenumber.setText(PhoneNumberUtils.formatNumber(phoneNumbers.get(position).getNumber(), "FR"));
        } else {
            tenumber.setText(phoneNumbers.get(position).getNumber());
        }
        return convertView;
    }
}

