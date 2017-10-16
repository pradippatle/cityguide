package com.pradipatle.cityguide.tumsar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

class GetNearbyResultsData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyResultData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            UrlConnection urlConnection = new UrlConnection();
            googlePlacesData = urlConnection.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            Log.i("Map","Result : " +googlePlace.toString());
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
          //  String snipet = placeName + "\n" + vicinity;
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity);
            //  markerOptions.title(placeName + "\n" + vicinity);

            if (ActivityMaps.searchParam.equalsIgnoreCase("restaurant")) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurants_blk));
            } else if (ActivityMaps.searchParam.equalsIgnoreCase("hospital")) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital_blk));
            } else if (ActivityMaps.searchParam.equalsIgnoreCase("school")) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_black));
            } else if (ActivityMaps.searchParam.equalsIgnoreCase("bank")) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_atm_location));
            }else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.box));
            }
            mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
}
