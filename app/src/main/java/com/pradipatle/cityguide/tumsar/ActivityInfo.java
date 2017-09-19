package com.pradipatle.cityguide.tumsar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Aeon admin on 14-Jul-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ActivityInfo extends AppCompatActivity {
    private ImageView actionbar_left_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        actionbar_left_icon =  (ImageView) findViewById(R.id.actionbar_left_icon);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7240646173515438/5028992105");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
