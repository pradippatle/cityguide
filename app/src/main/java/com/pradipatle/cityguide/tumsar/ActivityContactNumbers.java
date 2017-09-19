package com.pradipatle.cityguide.tumsar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.pradipatle.cityguide.tumsar.Adapter.SarpanchListAdapter;
import com.pradipatle.cityguide.tumsar.model.SarpanchTahsildarModel;

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
    private ArrayList<SarpanchTahsildarModel> sarpanchTahsildarList;
    private SarpanchListAdapter sarpanchTahsildarAdapter;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);
        listview= (ListView)findViewById(R.id.listviewSarpanch);
        sarpanchTahsildarList = new ArrayList<>();
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

            InputStream is = getAssets().open("sarpanch_list.json");

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
                    SarpanchTahsildarModel mr = new SarpanchTahsildarModel();
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    String title = jsonObject.getString("village");
                    String description = jsonObject.getString("name");
                    String weblink = jsonObject.getString("mobile");

                    mr.village = title;
                    mr.name = description;
                    mr.mobile = weblink;
                    sarpanchTahsildarList.add(mr);
                }

                if (sarpanchTahsildarList.size() > 0) {
                    sarpanchTahsildarAdapter = new SarpanchListAdapter(sarpanchTahsildarList, ActivityContactNumbers.this);
                    listview.setAdapter(sarpanchTahsildarAdapter);
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
