package com.example.bukola_omotoso.contactmanager.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.bukola_omotoso.contactmanager.R;

public class ContactContentProvider extends ContentProvider {

    private ContactDatabaseHelper contactDatabaseHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_CONTACT =  1;
    private static final int CONTACTS = 2;

    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);

        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }

    public ContactContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;
        switch (uriMatcher.match(uri))  {
            case ONE_CONTACT:
                String id = uri.getLastPathSegment();
                numberOfRowsDeleted = contactDatabaseHelper.getWritableDatabase().delete(
                        DatabaseDescription.Contact.TABLE_NAME, DatabaseDescription.Contact._ID+"="+id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri) + uri);
        }
        if(numberOfRowsDeleted != 0)    {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        // Implement this to handle requests to delete one or more rows.
        return  numberOfRowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Uri newContactUri = null;
        switch (uriMatcher.match(uri))  {
            case  CONTACTS:
            long rowId = contactDatabaseHelper.getWritableDatabase().insert(
                    DatabaseDescription.Contact.TABLE_NAME, null, values);

                if(rowId > 0)   {
                    newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);
                    getContext().getContentResolver().notifyChange(uri, null);
                }   else    {
                    throw new UnsupportedOperationException(getContext().getString(
                            R.string.insert_failed)+uri
                    );
                }
                break;
            default:
                throw  new UnsupportedOperationException(getContext().getString(R.string.invalid_insert_uri) + uri);

        }
        return  newContactUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        contactDatabaseHelper = new ContactDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri))  {
            case ONE_CONTACT:
                queryBuilder.appendWhere(DatabaseDescription.Contact._ID+"="
                +uri.getLastPathSegment());
                break;
            case CONTACTS:
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        Cursor cursor = queryBuilder.query(contactDatabaseHelper.getReadableDatabase(),
                projection,selection,selectionArgs,null,null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numberOfRowsUpdated;
        switch (uriMatcher.match(uri))  {
            case ONE_CONTACT:
                String id =  uri.getLastPathSegment();
                numberOfRowsUpdated = contactDatabaseHelper.getWritableDatabase().update(
                        DatabaseDescription.Contact.TABLE_NAME, values, DatabaseDescription.Contact._ID+"=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri)+uri);
        }
        // TODO: Implement this to handle requests to update one or more rows.
        if(numberOfRowsUpdated != 0)    {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numberOfRowsUpdated;
    }
}
