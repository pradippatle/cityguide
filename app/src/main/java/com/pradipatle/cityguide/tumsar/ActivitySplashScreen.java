package com.pradipatle.cityguide.tumsar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pradipatle.cityguide.tumsar.Chat.Config;
import com.pradipatle.cityguide.tumsar.Chat.NotificationUtils;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


/**
 * Created by Aeon admin on 06-Oct-16.
 */
public class ActivitySplashScreen extends AwesomeSplash {

    private static int SPLASH_TIME_OUT = 3800;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String isLoggedIn = "isLoggedIn";
    private String loginStatus;
    private static final String TAG = ActivitySplashScreen.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.bg_gradient_end); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms

        //Customize Path
        configSplash.setOriginalHeight(150); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(150); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(2500);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.bg_gradient_end); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1500);
        configSplash.setPathSplashFillColor(R.color.bg_gradient_end); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("Tumsar - My City");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        loginStatus = sharedpreferences.getString(isLoggedIn, null);
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            if (loginStatus != null) {
                if (loginStatus.equalsIgnoreCase("yes")) {
                    Intent i = new Intent(ActivitySplashScreen.this, HomeScreenActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(ActivitySplashScreen.this, ActivitySliderScreen.class);
                    startActivity(i);
                    finish();
                }
            } else {
                Intent i = new Intent(ActivitySplashScreen.this, ActivitySliderScreen.class);
                startActivity(i);
                finish();
            }
            finish();
        }
    }, SPLASH_TIME_OUT);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                }
            }
        };
}


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

    }

    @Override
    public void animationsFinished() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}