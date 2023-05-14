package com.example.womensafety.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.MainActivity;
import com.example.womensafety.R;
import com.example.womensafety.adapter.HelpLineAdapter;
import com.example.womensafety.model.HelpLine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HelpLineActivity extends MainActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new HelpLineAdapter(this, loadHelpLinesFromJSON()));
    }

    private ArrayList<HelpLine> loadHelpLinesFromJSON() {
        ArrayList<HelpLine> helpLines = new ArrayList<>();

        try {
            // read the JSON data from the helplines.json file in the assets folder
            InputStream inputStream = getAssets().open("helpline.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // parse the JSON data into an array of JSON objects
            JSONArray jsonArray = new JSONArray(new String(buffer, "UTF-8"));

            // iterate over the array of JSON objects and create HelpLine objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String icon = jsonObject.getString("icon");
                String name = jsonObject.getString("name");
                String number = jsonObject.getString("number");
                HelpLine helpLine = new HelpLine(icon, name, number);
                helpLines.add(helpLine);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return helpLines;
    }
}
