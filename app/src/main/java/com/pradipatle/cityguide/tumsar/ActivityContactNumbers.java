package com.pradipatle.cityguide.tumsar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.pradipatle.cityguide.tumsar.Adapter.contactListAdapter;
import com.pradipatle.cityguide.tumsar.model.ModelContacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Aeon admin on 07-Oct-16.
 */
public class ActivityContactNumbers extends Activity{
    private String jsonResponse = null;
    private ArrayList<ModelContacts> contactsArrayList;
    private contactListAdapter contactsAdapter;
    private ListView contactsListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);
        contactsListview = (ListView)findViewById(R.id.listviewContacts);
        contactsArrayList = new ArrayList<>();
        loadJSONFromAsset();

        ImageView actionbar_left_icon =  (ImageView) findViewById(R.id.actionbar_left_icon);
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private String loadJSONFromAsset() {
        try {

            InputStream is = getAssets().open("contact_list.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            jsonResponse = new String(buffer, "UTF-8");
            Log.i("Log", "JSON RESPONSE " + jsonResponse);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(jsonResponse);
                JSONArray dataArray = jsonObj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    ModelContacts mr = new ModelContacts();
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String number = jsonObject.getString("number");
                    mr.mobile = number;
                    mr.name = name;
                    contactsArrayList.add(mr);
                }

                if (contactsArrayList.size() > 0) {
                    contactsAdapter = new contactListAdapter(contactsArrayList, ActivityContactNumbers.this);
                    contactsListview.setAdapter(contactsAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonResponse;

    }
}
