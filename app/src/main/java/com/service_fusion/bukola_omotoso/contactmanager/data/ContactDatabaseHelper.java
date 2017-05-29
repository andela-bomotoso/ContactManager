package com.service_fusion.bukola_omotoso.contactmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bukola_omotoso on 22/05/2017.
 */

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactManager.db";
    private static final int DATABASE_VERSION = 1;

    public ContactDatabaseHelper(Context context)   {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_CONTACTS_TABLE =
                "CREATE TABLE "+ com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.TABLE_NAME+ "("+
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact._ID + " integer primary key, " +
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_FIRSTNAME + " TEXT, " +
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_LASTNAME + " TEXT, "+
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_PHONE + " TEXT, "+
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_BIRTHDAY+ " TEXT, "+
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_ADDRESS+" TEXT, "+
                        com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription.Contact.COLUMN_ZIP + " TEXT);";
                sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
