package com.example.womensafety;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.adapter.ContactListAdapter;
import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private MaterialButton addAppBtn;
    private Database database;
    private ArrayList<Contact> contactArrayList;
    private final ArrayList<Contact> selectedContactArrayList = new ArrayList<>();
    private ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_contact_list);
        database = new Database(this);
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        recyclerView = findViewById(R.id.contactRecyclerView);
        addAppBtn = findViewById(R.id.addAppBtn);
        addAppBtn.setEnabled(false);
        addAppBtn.setOnClickListener(v -> addApps());

        contactArrayList = database.getAppList();
        contactListAdapter = new ContactListAdapter(this, contactArrayList, new OnAddAppButtonListener() {
            @Override
            public void isAnyCheckboxChecked(boolean isChecked) {
            }

            @Override
            public void isAnyCheckboxChecked(boolean isLast, boolean isChecked) {
                addAppBtn.setEnabled(isChecked);
                if (isLast) {
                    addAppBtn.setEnabled(true);
                }
            }
        });
        recyclerView.setAdapter(contactListAdapter);
        readDualSimContacts();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addApps() {
        int[] position = contactListAdapter.getCheckedItemsPosition();
        selectedContactArrayList.clear();
        database.delFullSelectedAppList();
        for (int i = 0; i < position.length; i++) {
            if (position[i] != -1) {
                selectedContactArrayList.add(contactArrayList.get(i));
                //  Log.e("selected list items", arrayAppList.get(i).name);
            }
        }
        database.saveSelectedAppList(selectedContactArrayList);
        Intent intent = new Intent();
        intent.putExtra("keyan", selectedContactArrayList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactListAdapter.filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private final ArrayList<Contact> contacts = new ArrayList<>();
    private void readDualSimContacts() {
        ContentResolver contentResolver = getContentResolver();
        Uri simUri = Uri.parse("content://icc/adn");

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simCount = telephonyManager.getPhoneCount();

        for (int simSlotIndex = 0; simSlotIndex < simCount; simSlotIndex++) {
            Cursor cursor = contentResolver.query(simUri, null, "sim_index=?", new String[]{String.valueOf(simSlotIndex)}, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex("number"));

                    contacts.add(new Contact(name, phoneNumber));
                    // Use the name and phoneNumber as needed
                }
                cursor.close();
            }
        }

        contactArrayList = contacts;
        contactListAdapter.updateList(contactArrayList);
        database.delFullAppList();
        database.saveAppList(contactArrayList);
        contactListAdapter.sortList();
    }
}
