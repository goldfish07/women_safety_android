package com.example.womensafety.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.womensafety.MainActivity;
import com.example.womensafety.R;
import com.example.womensafety.SplashActivity;
import com.google.android.material.button.MaterialButton;

public class PermissionActivity extends AppCompatActivity {

    MaterialButton locationPermissionBtn, contactPermissionBtn, smsPermissionBtn;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        locationPermissionBtn = findViewById(R.id.locationPermissionBtn);
        contactPermissionBtn = findViewById(R.id.contactPermissionBtn);
        smsPermissionBtn = findViewById(R.id.smsPermissionBtn);
        locationPermissionBtn.setOnClickListener(clickListener);
        contactPermissionBtn.setOnClickListener(clickListener);
        smsPermissionBtn.setOnClickListener(clickListener);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {

        }

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View v) {
            if (v.getId() == R.id.locationPermissionBtn) {
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            } else if (v.getId() == R.id.contactPermissionBtn) {
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else if (v.getId() == R.id.smsPermissionBtn) {
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    };

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSION_REQUEST_LOCATION) {
                locationPermissionBtn.setIcon(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                locationPermissionBtn.setBackgroundColor(getResources().getColor(R.color.teal_700));
                locationPermissionBtn.setTextColor(getResources().getColor(R.color.white));
                locationPermissionBtn.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                locationPermissionBtn.setText("GRANTED");
            } else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
                contactPermissionBtn.setIcon(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                contactPermissionBtn.setBackgroundColor(getResources().getColor(R.color.teal_700));
                contactPermissionBtn.setTextColor(getResources().getColor(R.color.white));
                contactPermissionBtn.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                contactPermissionBtn.setText("GRANTED");
            } else if (requestCode == PERMISSIONS_REQUEST_SEND_SMS) {
                smsPermissionBtn.setIcon(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                smsPermissionBtn.setTextColor(getResources().getColor(R.color.white));
                smsPermissionBtn.setBackgroundColor(getResources().getColor(R.color.teal_700));
                smsPermissionBtn.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                smsPermissionBtn.setText("GRANTED");
            }
        } else {
            if (requestCode == PERMISSION_REQUEST_LOCATION) {

            } else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {

            } else if (requestCode == PERMISSIONS_REQUEST_SEND_SMS) {

            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(PermissionActivity.this, MainActivity.class));
            finish();
        }
    }
}
