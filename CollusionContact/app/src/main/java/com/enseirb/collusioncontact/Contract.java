package com.enseirb.collusioncontact;

import android.net.Uri;

public final class Contract {
    public static final String AUTHORITY = "com.enseirb.collusioncontact";
    public static final String CONTENT_PATH = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    static final int ALL_ITEMS = -2;
    static final String CONTACT_ID = "id";
    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd."+AUTHORITY+"."+CONTENT_PATH;
    static final String MULTIPLE_RECORD_MIME_TYPE = "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+CONTENT_PATH;
    private Contract() {}
}
