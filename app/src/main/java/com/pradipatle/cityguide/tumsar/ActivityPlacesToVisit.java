package com.pradipatle.cityguide.tumsar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * Created by Aeon admin on 14-Jul-16.
 */
public class ActivityPlacesToVisit extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_visit);
        ImageView actionbar_left_icon =  (ImageView) findViewById(R.id.actionbar_left_icon);
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7240646173515438/5028992105");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView viewMorePlaces = (TextView)findViewById(R.id.viewMorePlaces);
        viewMorePlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPlacesToVisit.this, AcivityViewMore_Webview.class);
                intent.putExtra("Action","MorePlaces");
                startActivity(intent);
            }
        });

    }
}
