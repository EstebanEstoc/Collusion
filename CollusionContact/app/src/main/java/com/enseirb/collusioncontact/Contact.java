package com.enseirb.collusioncontact;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class Contact {
    private final String name;
    private final Bitmap thumbnail;
    private final ArrayList<PhoneNumber> numbers;

    public Contact(String name, Bitmap thumbnail, ArrayList<PhoneNumber> phones) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.numbers = phones;
    }

    public String getName() {
        return name;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public ArrayList<PhoneNumber> getNumbers() {
        return numbers;
    }
}
