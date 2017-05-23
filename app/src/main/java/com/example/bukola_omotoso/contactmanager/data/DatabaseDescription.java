package com.example.bukola_omotoso.contactmanager.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bukola_omotoso on 22/05/2017.
 */

public class DatabaseDescription {
    public static final String AUTHORITY = "com.servicefusion.contactmanager.data";
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://"+AUTHORITY);

    public static final class Contact implements BaseColumns    {
        public static final String TABLE_NAME = "contacts";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static  final String COLUMN_FIRSTNAME = "first_name";
        public static  final String COLUMN_LASTNAME = "last_name";
        public static  final String COLUMN_PHONE = "phone";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_ZIP = "zip_code";

        public static Uri buildContactUri(long id)  {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}
