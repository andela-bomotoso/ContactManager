package com.example.bukola_omotoso.contactmanager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.bukola_omotoso.contactmanager.fragments.ContactDetailsFragment;
import com.example.bukola_omotoso.contactmanager.fragments.ContactFragment;
import com.example.bukola_omotoso.contactmanager.fragments.EditAddFragment;

public class MainActivity extends AppCompatActivity implements ContactFragment.ContactFragmentListener,EditAddFragment.EditAddFragmentListener, ContactDetailsFragment.ContactDetailsFragmentListener{
    public static final String CONTACT_URI = "contact_uri";
    private ContactFragment contactFragment;

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sharedPreferences = getSharedPreferences("")
        contactFragment = new ContactFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactFragment);
            transaction.commit();
    }

    @Override
    public void onEditAddCompleted(Uri contactUri) {
        getSupportFragmentManager().popBackStack();
        contactFragment.updateContactList();
    }

    @Override
    public void onContactSelected(Uri contactUri) {
        displayContact(contactUri, R.id.fragmentContainer);
    }

    @Override
    public void onContactAdded() {
        displayEditAddFragment(R.id.fragmentContainer, null);
    }

    private void displayContact(Uri contactUri, int viewID) {
        ContactDetailsFragment detailsFragment = new ContactDetailsFragment();
        Bundle arguments =  new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailsFragment.setArguments(arguments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayEditAddFragment(int viewID, Uri contactUri) {
        EditAddFragment editAddFragment = new EditAddFragment();

        if(contactUri != null)  {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            editAddFragment.setArguments(arguments);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, editAddFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onContactDeleted() {
        getSupportFragmentManager().popBackStack();
        contactFragment.updateContactList();

    }

    @Override
    public void onEditContact(Uri contactUri) {
        displayEditAddFragment(R.id.fragmentContainer, contactUri);
    }
}
