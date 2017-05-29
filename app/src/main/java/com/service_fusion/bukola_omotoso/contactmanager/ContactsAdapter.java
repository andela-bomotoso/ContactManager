package com.example.bukola_omotoso.contactmanager;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bukola_omotoso.contactmanager.data.DatabaseDescription;

import org.w3c.dom.Text;

/**
 * Created by bukola_omotoso on 24/05/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private final ContactClickListener clickListener;
    private Cursor cursor = null;

    public ContactsAdapter(ContactClickListener clickListener)  {
        this.clickListener = clickListener;
    }

    public interface ContactClickListener   {
        void onClick(Uri contactUri );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private long rowID;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(DatabaseDescription.Contact.buildContactUri(rowID));
                }
            });
        }

        public void setRowID(long rowID)    {
            this.rowID = rowID;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Log.d("cursor", cursor.toString());
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_FIRSTNAME))+" " + cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_LASTNAME)));
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor)   {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public int getCursorCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }
}
