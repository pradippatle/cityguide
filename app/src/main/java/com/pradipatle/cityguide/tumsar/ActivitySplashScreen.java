package com.pradipatle.cityguide.tumsar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
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

    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
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
        configSplash.setPathSplashStrokeColor(R.color.colorPrimaryDark); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1500);
        configSplash.setPathSplashFillColor(R.color.colorAccent); //path object filling color

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
}

    @Override
    public void animationsFinished() {

    }

}