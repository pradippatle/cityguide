package com.pradipatle.cityguide.tumsar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Aeon-02 on 9/1/2017.
 */

public class AcivityViewMore_Webview extends Activity {
    private WebView WebViewMediaReview;
    String URLcode;
    private ProgressBar progressbar = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmore);
        TextView appbarTitle = (TextView)findViewById(R.id.id_appbartitle);

        Bundle b = getIntent().getExtras();
       if(b.getString("Action").equalsIgnoreCase("BusTimings")){
           URLcode = "http://bhandara.nic.in/public_html/tumsar%20timetable.htm";
           appbarTitle.setText("ST Bus Time Table");
       }else {
           URLcode = "http://bhandara.nic.in/Tourism/index.html";
           appbarTitle.setText("BHANDARA TOURISM");
       }
        progressbar = (ProgressBar)findViewById(R.id.progressBar11);
        progressbar.setMax(100);
        ImageView actionbar_left_icon =  (ImageView) findViewById(R.id.actionbar_left_icon);
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Retrieve the value
        progressbar.setVisibility(View.VISIBLE);
        WebViewMediaReview =(WebView)findViewById(R.id.webViewGallery);
        WebViewMediaReview.getSettings().setLoadsImagesAutomatically(true);
        WebViewMediaReview.getSettings().setJavaScriptEnabled(true);
        WebViewMediaReview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebViewMediaReview.loadUrl(URLcode);
        WebViewMediaReview.setWebViewClient(new WebViewClient());
        WebViewMediaReview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(progress);
                if (progress == 100) {
                    progressbar.setVisibility(View.GONE); // Make the bar disappear after URL is loaded
                }
            }
        });
    }
}


