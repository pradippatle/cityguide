package com.pradipatle.cityguide.tumsar;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.pradipatle.cityguide.tumsar.Common.FontsOverride;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Aeon admin on 04-Oct-16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/OpenSans-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-ExtraBold.ttf");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.pradipatle.cityguide.tumsar", // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException ignored) {

        } catch (NoSuchAlgorithmException ignored) {

        }
    }
}