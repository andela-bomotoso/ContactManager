package com.example.bukola_omotoso.contactmanager.fragments;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.input.InputManager;
import android.icu.util.Calendar;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bukola_omotoso.contactmanager.MainActivity;
import com.example.bukola_omotoso.contactmanager.R;
import com.example.bukola_omotoso.contactmanager.data.DatabaseDescription;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.R.attr.startYear;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditAddFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, DatePickerDialog.OnDateSetListener {

    private final Calendar calendar = Calendar.getInstance();
    private static final int CONTACT_LOADER = 0;
    private EditAddFragmentListener editAddFragmentListener;
    private Uri contactUri;
    private boolean newContact = true;


    private TextInputLayout fNameTextInputLayout;
    private TextInputLayout lNameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout birthdayTextInputLayout;
    private TextInputLayout addressTextInputLayout;
    private TextInputLayout zipTextInputLayout;

    private ImageButton birthdayIcon;
    private FloatingActionButton saveContactFAB;

    private CoordinatorLayout coordinatorLayout;



    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        editAddFragmentListener = (EditAddFragmentListener) context;
    }

    @Override
    public void onDetach()  {
        super.onDetach();
        editAddFragmentListener = null;
    }


    public EditAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    public interface EditAddFragmentListener    {
        void  onEditAddCompleted(Uri contactUri);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_add, container,false);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateBirthday();
            }
        };

        fNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.fNameTextInput);
        lNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.lNameTextInput);

        phoneTextInputLayout = (TextInputLayout)view.findViewById(R.id.phoneTextInput);
        birthdayTextInputLayout = (TextInputLayout)view.findViewById(R.id.birthdayTextInput);
        addressTextInputLayout = (TextInputLayout)view.findViewById(R.id.addressTextInput);
        zipTextInputLayout = (TextInputLayout) view.findViewById(R.id.zipTextInput);

        birthdayIcon = (ImageButton) view.findViewById(R.id.birthdayIcon);

        birthdayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        saveContactFAB = (FloatingActionButton) view.findViewById(R.id.saveFloatingActionButton);

        saveContactFAB.setOnClickListener(saveContactButtonClick);
        //updateSaveButtonFAB();
        coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.fragmentContainer);

        Bundle arguments = getArguments();

        if (arguments != null)  {
            newContact = false;
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);
        }

        if(contactUri != null)  {
            getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        }

        return view;
    }

    private void updateBirthday() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthdayTextInputLayout.getEditText().setText(sdf.format(calendar.getTime()));
    }

    private final View.OnClickListener saveContactButtonClick =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(),0);
                    saveContact();
                }
            };

            private void saveContact()  {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseDescription.Contact.COLUMN_FIRSTNAME,
                        fNameTextInputLayout.getEditText().getText().toString());
                contentValues.put(DatabaseDescription.Contact.COLUMN_LASTNAME,
                        lNameTextInputLayout.getEditText().getText().toString());
                contentValues.put(DatabaseDescription.Contact.COLUMN_PHONE,
                        phoneTextInputLayout.getEditText().getText().toString());
                contentValues.put(DatabaseDescription.Contact.COLUMN_BIRTHDAY,
                        birthdayTextInputLayout.getEditText().getText().toString());
                contentValues.put(DatabaseDescription.Contact.COLUMN_ADDRESS,
                        addressTextInputLayout.getEditText().getText().toString());
                contentValues.put(DatabaseDescription.Contact.COLUMN_ZIP,
                        zipTextInputLayout.getEditText().getText().toString());
                if(newContact) {
                    if (!fNameTextInputLayout.getEditText().getText().toString().isEmpty() || ! lNameTextInputLayout.getEditText().getText().toString().isEmpty()) {
                        Uri newContactUri = getActivity().getContentResolver().insert(DatabaseDescription.Contact.CONTENT_URI, contentValues);

                        if (newContactUri != null) {
                            Snackbar.make(coordinatorLayout, R.string.contact_added, Snackbar.LENGTH_LONG).show();
                            editAddFragmentListener.onEditAddCompleted(newContactUri);
                        } else {
                            Snackbar.make(coordinatorLayout, R.string.contact_not_added, Snackbar.LENGTH_LONG).show();
                        }
                    }   else    {
                        Snackbar.make(coordinatorLayout, R.string.invalid_contact, Snackbar.LENGTH_LONG).show();
                    }
                } else    {
                    int updatedRows = getActivity().getContentResolver().update(contactUri, contentValues, null, null);

                    if(updatedRows > 0) {
                        editAddFragmentListener.onEditAddCompleted(contactUri);
                        Snackbar.make(coordinatorLayout, R.string.contact_updated,Snackbar.LENGTH_LONG).show();
                    }   else    {
                        Snackbar.make(coordinatorLayout, R.string.contact_not_updated, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONTACT_LOADER:
                return  new CursorLoader(getActivity(), contactUri, null,
                        null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            int fnameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_FIRSTNAME);
            int lnameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_LASTNAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int birthdayIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_BIRTHDAY);
            int addressIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ADDRESS);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            fNameTextInputLayout.getEditText().setText(data.getString(fnameIndex));
            lNameTextInputLayout.getEditText().setText(data.getString(lnameIndex));
            phoneTextInputLayout.getEditText().setText(data.getString(phoneIndex));
            birthdayTextInputLayout.getEditText().setText(data.getString(birthdayIndex));
            addressTextInputLayout.getEditText().setText(data.getString(addressIndex));
            zipTextInputLayout.getEditText().setText(data.getString(zipIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
