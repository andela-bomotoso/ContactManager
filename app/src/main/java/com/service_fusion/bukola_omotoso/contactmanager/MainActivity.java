package com.service_fusion.bukola_omotoso.contactmanager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.service_fusion.bukola_omotoso.contactmanager.fragments.ContactDetailsFragment;
import com.service_fusion.bukola_omotoso.contactmanager.fragments.ContactFragment;
import com.service_fusion.bukola_omotoso.contactmanager.fragments.EditAddFragment;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, ContactFragment.ContactFragmentListener, EditAddFragment.EditAddFragmentListener, ContactDetailsFragment.ContactDetailsFragmentListener {
    public static final String CONTACT_URI = "contact_uri";
    private ContactFragment contactFragment;
    private CoordinatorLayout coordinatorLayout;
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.fragmentContainer);
        prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        contactFragment = new ContactFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, contactFragment);
        transaction.commit();

        if (isFirstrun()) {
            Snackbar.make(coordinatorLayout, R.string.first_run_message, Snackbar.LENGTH_LONG).show();
            setFirstrun(false);

            getSupportFragmentManager().addOnBackStackChangedListener(this);
            shouldDisplayHomeUp();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume Entered", "RESUME MSG");
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        boolean hasFragment = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(hasFragment);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
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
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailsFragment.setArguments(arguments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayEditAddFragment(int viewID, Uri contactUri) {
        EditAddFragment editAddFragment = new EditAddFragment();

        if (contactUri != null) {
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


    public boolean isFirstrun() {
        return prefs.getBoolean("firstrun", true);
    }

    public void setFirstrun(boolean firstrun) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstrun", firstrun);
    }
}
