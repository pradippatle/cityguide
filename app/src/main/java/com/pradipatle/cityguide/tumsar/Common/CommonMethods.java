package com.pradipatle.cityguide.tumsar.Common;

import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Typeface;
import android.net.ConnectivityManager;

import android.widget.TextView;
import android.widget.Toast;


public class CommonMethods {

    private static ProgressDialog mDialog;

    public static void DisplayToastLengthLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void DisplayToastLengthShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setFontRegular(Context context, TextView textView) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
            textView.setTypeface(tf);

        } catch (Exception e) {
            e.toString();
        }
    }

    public static void setFontBold(Context context, TextView textView) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Brandon_blk.otf");
            textView.setTypeface(tf);

        } catch (Exception e) {
            e.toString();
        }
    }

    public static void setFontMedium(Context context, TextView textView) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Brandon_light.otf");
            textView.setTypeface(tf);

        } catch (Exception e) {
            e.toString();
        }
    }

    // ------------------- DIALOG -------------------//

    public static void showDialog(Context context, String message, boolean cancelable) {
        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
        } else {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage(message); // message
            mDialog.setCancelable(cancelable);
            mDialog.show();
        }

    }

    public static void dismissDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception ignored) {
        }
    }

    public static boolean isDialogShowing() {
        try {
            return mDialog != null && mDialog.isShowing();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInternetConnectionAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}