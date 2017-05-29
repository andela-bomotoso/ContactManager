package com.example.bukola_omotoso.contactmanager.fragments;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bukola_omotoso.contactmanager.MainActivity;
import com.example.bukola_omotoso.contactmanager.R;
import com.example.bukola_omotoso.contactmanager.data.DatabaseDescription;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private  static final  int CONTACT_LOADER = 0;
    private static Uri contactUri;
    private  static ContactDetailsFragmentListener contactDetailsFragmentListener;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView birthdayTextView;
    private TextView addressTextView;
    private TextView zipTextView;

    private static CoordinatorLayout coordinatorLayout;


    public ContactDetailsFragment() {
    }

    public interface ContactDetailsFragmentListener {
        void onContactDeleted();
        void onEditContact(Uri contactUri);
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        contactDetailsFragmentListener = (ContactDetailsFragmentListener) context;
    }

    @Override
    public   void onDetach()    {
        super.onDetach();
        contactDetailsFragmentListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        if(arguments !=null)
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);

        coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.fragmentContainer);
        nameTextView = (TextView)view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView)view.findViewById(R.id.phoneTextView);
        birthdayTextView = (TextView)view.findViewById(R.id.birthdayTextView);
        addressTextView = (TextView)view.findViewById(R.id.addressTextView);
        zipTextView = (TextView)view.findViewById(R.id.zipTextView);

        getLoaderManager().initLoader(CONTACT_LOADER, null, this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)   {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())   {
            case R.id.action_edit:
                contactDetailsFragmentListener.onEditContact(contactUri);
                return true;
            case R.id.action_delete:
                deleteContact();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteContact()    {
        DialogFragment dialogFragment = DeleteDialogFragment.newInstance(R.string.confirm_title);
        dialogFragment.show(getFragmentManager(),"deleteDialog");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        switch (id) {
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(), contactUri,
                        null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst())  {
            int fNameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_FIRSTNAME);
            int lNameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_LASTNAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int birthdayIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_BIRTHDAY);
            int addressIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ADDRESS);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);
            nameTextView.setText(data.getString(fNameIndex)+" "+data.getString(lNameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            birthdayTextView.setText(data.getString(birthdayIndex));
            addressTextView.setText(data.getString(addressIndex));
            zipTextView.setText(data.getString(zipIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class DeleteDialogFragment extends DialogFragment {
        public static DeleteDialogFragment newInstance(int title)   {
            DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title",title);
            deleteDialogFragment.setArguments(args);
            return deleteDialogFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);

            builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().getContentResolver().delete(contactUri, null,null);
                    contactDetailsFragmentListener.onContactDeleted();
                    Snackbar.make(coordinatorLayout, R.string.contact_deleted,Snackbar.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create();
        }
    }
}
