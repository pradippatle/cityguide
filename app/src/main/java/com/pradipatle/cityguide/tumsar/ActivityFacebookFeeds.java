package com.pradipatle.cityguide.tumsar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pradipatle.cityguide.tumsar.Adapter.FbFeedsAdapter;
import com.pradipatle.cityguide.tumsar.Common.CommonMethods;
import com.pradipatle.cityguide.tumsar.model.FbFeedsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aeon-02 on 9/11/2017.
 */

public class ActivityFacebookFeeds extends Activity {
    private ImageView actionbar_icon;
    private ListView FeedList;
    private ArrayList<FbFeedsModel> FeedsArrayList;
    private FbFeedsAdapter FbFeedsAdapter;
    private TextView Title_bar;
    private String FbPageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_feeds_activity);

        Bundle b = getIntent().getExtras();
        String SourcePage =b.getString("page");
        Title_bar = (TextView)findViewById(R.id.id_title_bar);
        if(SourcePage!=null && SourcePage.equalsIgnoreCase("Schemes")){
            Title_bar.setText("Government Schemes");
            FbPageId = "/1924848391114798/feed";
        }else if(SourcePage!=null && SourcePage.equalsIgnoreCase("Help")){
            Title_bar.setText("Farmers Help");
            FbPageId = "/1694125200658653/feed";
        }else {
            Title_bar.setText("News & Events");
            FbPageId = "/129306537713760/feed";
        }
        FeedList = (ListView) findViewById(R.id.fb_feeds_listview);
        FeedsArrayList = new ArrayList<>();
        actionbar_icon = (ImageView) findViewById(R.id.actionbar_left_icon);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7240646173515438/5028992105");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        actionbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CommonMethods.showDialog(ActivityFacebookFeeds.this,"Loading\nPlease wait....",false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                FbPageId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if(response != null) {
                            Log.i("Facebook", "Fb Post Response : " + response);
                            CommonMethods.dismissDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response.getRawResponse());
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                if (dataArray.length() > 0) {

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        FbFeedsModel feedModel = new FbFeedsModel();
                                        JSONObject dataObj = dataArray.getJSONObject(i);
                                        String FbFeed = dataObj.getString("message");
                                        String FeedTime = dataObj.getString("created_time");
                                        Log.i("FbFeed", "Response " + FbFeed);

                                        feedModel.created_time = FeedTime;
                                        feedModel.feeds = FbFeed;
                                        FeedsArrayList.add(feedModel);
                                    }

                                    if (FeedsArrayList.size() > 0) {
                                        FbFeedsAdapter = new FbFeedsAdapter(FeedsArrayList, ActivityFacebookFeeds.this);
                                        FeedList.setAdapter(FbFeedsAdapter);
                                    }
                                } else {
                                    TextView nodata = (TextView) findViewById(R.id.nodata_id);
                                    FeedList.setVisibility(View.GONE);
                                    nodata.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAsync();
    }
}
