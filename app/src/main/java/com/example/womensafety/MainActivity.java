package com.example.womensafety;

import static com.example.womensafety.Utils.isWhatsAppInstalled;
import static com.example.womensafety.Utils.setDataToLineChart;
import static com.example.womensafety.Utils.setOsmdroidMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.womensafety.activity.HelpLineActivity;
import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {
    private Database database;
    FloatingActionButton fabSOS;
    SharedPreferences preferences;
    ExpandableCardView sendLocationExCardView, smsAlertExCardView;
    MaterialCheckBox whatsappCheckBox;
    TextInputLayout inputLayout;
    TextInputEditText inputEditText;
    private LinearLayout errorMapViewLayout;

    LineChart lineChart;
    MaterialButton refreshBtn;

    private static final String KEY_PREF_WHATSAPP = "whatsapp";
    private static final String KEY_PREF_LOCATION = "location";
    private static final String KEY_PREF_SMS = "sms";

    CardView cardAddContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //https://osmdroid.github.io/osmdroid/Important-notes-on-using-osmdroid-in-your-app.html
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        setSupportActionBar(findViewById(R.id.toolBar));
        lineChart = findViewById(R.id.lineChart);
        fabSOS = findViewById(R.id.fabSOS);

        cardAddContact = findViewById(R.id.cardAddContact);
        sendLocationExCardView = findViewById(R.id.sendLocationExCardView);
        smsAlertExCardView = findViewById(R.id.smsAlertExCardView);
        inputLayout = findViewById(R.id.inputLayout);
        inputEditText = findViewById(R.id.inputEditText);
        whatsappCheckBox = findViewById(R.id.whatsappCheckBox);
        refreshBtn = findViewById(R.id.refreshBtn);
        mapView = findViewById(R.id.mapView);
        errorMapViewLayout = findViewById(R.id.errorMapViewLayout);

        TextView todaysTipTxt = findViewById(R.id.todaysShortTipTxt);

        String[] safety_tips = getResources().getStringArray(R.array.safety_tips);
        todaysTipTxt.setText(safety_tips[0]);

        cardAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddContactActivity.class));
            }
        });
        findViewById(R.id.cardShowRegisteredContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowRegisteredContact().show(getSupportFragmentManager(), "");
            }
        });
        fabSOS.setOnClickListener(clickListener);
        database = new Database(this);
        sendLocationExCardView.setChecked(preferences.getBoolean(KEY_PREF_LOCATION, false));
        sendLocationExCardView.setOnCheckChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setLocationToMap();
            } else {
                removeMapsLink(inputEditText);
            }
            preferences.edit().putBoolean(KEY_PREF_LOCATION, isChecked).apply();
        });

        whatsappCheckBox.setChecked(preferences.getBoolean(KEY_PREF_WHATSAPP, false));
        whatsappCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isWhatsAppInstalled(this)) {
                if (isChecked) {
                    preferences.edit().putBoolean(KEY_PREF_WHATSAPP, true).apply();
                }
            } else {
                Toast.makeText(this, "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
                whatsappCheckBox.setChecked(false);
                preferences.edit().putBoolean("whatsapp", false).apply();
            }
        });

        smsAlertExCardView.setChecked(preferences.getBoolean(KEY_PREF_SMS, false));
        smsAlertExCardView.setOnCheckChangeListener((buttonView, isChecked) -> {
            inputLayout.setEnabled(isChecked);
            preferences.edit().putBoolean(KEY_PREF_SMS, isChecked).apply();
        });

        findViewById(R.id.helpLineCardView).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HelpLineActivity.class));
        });
        setDataToLineChart(this, lineChart);
        refreshBtn.setOnClickListener(v -> setLocationToMap());
        setLocationToMap();
    }

    private void removeMapsLink(TextInputEditText editText) {
        String currentText = editText.getText().toString();
        String newText = currentText.replaceAll("https://www.google.com/maps/search/\\?api=1&query=\\S+", "");
        editText.setText(newText);
    }

    String mapLink;
    MapView mapView;
    String defaultSmsAlert = "Emergency alert! I am in danger. Please send help to my location immediately.";
    String fullMSG = "";

    public void setLocationToMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            //mapView.removeAllViews();
            errorMapViewLayout.setVisibility(View.GONE);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            setOsmdroidMap(this, mapView, latitude, longitude);
            mapLink = generateMapLink(latitude, longitude);
            fullMSG = defaultSmsAlert + "\n" + mapLink;
            inputEditText.setText(fullMSG);
        } else {
            errorMapViewLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fabSOS) {
                sendSOS();
            }
        }
    };

    public String generateMapLink(double lat, double lon) {
        return "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
    }

    public void sendSOS() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_send_sos, null);
        RippleBackground rippleBackground = view.findViewById(R.id.ripple_bg);
        rippleBackground.startRippleAnimation();
        TextView countdownTextView = view.findViewById(R.id.countTxt);
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        dialog.show();

        new CountDownTimer(6000, 1000) { // 5000 milliseconds (5 seconds) countdown with 1000 milliseconds (1 second) interval
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText("" + millisUntilFinished / 1000); // update the TextView with the remaining time in seconds
            }

            public void onFinish() {
                countdownTextView.setText("0"); // set the final countdown value to 0
                // rippleBackground.stopRippleAnimation();
                if (preferences.getBoolean(KEY_PREF_WHATSAPP, false) && preferences.getBoolean("sms", false)) {
                    sendSMS(fullMSG);
                    postTextToWhatsAppStatus(fullMSG);
                } else if (preferences.getBoolean(KEY_PREF_SMS, false)) {
                    sendSMS(fullMSG);
                } else if (preferences.getBoolean(KEY_PREF_WHATSAPP, false)) {
                    postTextToWhatsAppStatus(fullMSG);
                }
                dialog.dismiss();
            }
        }.start(); // start the countdown timer
    }

    private void sendSMS(String message) {
        for (Contact contact : database.getContactList()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getPh_no(), null, message, null, null);
        }
    }

    private void postTextToWhatsAppStatus(String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.whatsapp");
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}