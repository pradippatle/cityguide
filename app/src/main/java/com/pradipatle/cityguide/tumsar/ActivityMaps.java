package com.pradipatle.cityguide.tumsar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    double latitude;
    double longitude;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private int PROXIMITY_RADIUS = 25000;
    private LinearLayout menuBanks, menuHospitals, menuSchools, menuRestaurants, otherMenu;
    public static String searchParam;
    StringBuilder currAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ImageView actionbar_left_icon = (ImageView) findViewById(R.id.actionbar_left_icon);
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menuBanks = (LinearLayout) findViewById(R.id.menuBanks);
        menuHospitals = (LinearLayout) findViewById(R.id.id_Hospitals_menu);
        menuSchools = (LinearLayout) findViewById(R.id.id_schools_menu);
        menuRestaurants = (LinearLayout) findViewById(R.id.id_restaaurant_menu);
        otherMenu = (LinearLayout) findViewById(R.id.id_other_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            finish();
        } else {

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        menuBanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParam = "bank";
                mMap.clear();
                String url = getUrl(latitude, longitude, "bank|atm");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                GetNearbyResultsData getNearbyResultData = new GetNearbyResultsData();
                getNearbyResultData.execute(DataTransfer);
                Toast.makeText(ActivityMaps.this, "Please wait...\nLoading results for Nearby Banks!", Toast.LENGTH_LONG).show();
            }
        });

        menuHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParam = "hospital";
                mMap.clear();
                String url = getUrl(latitude, longitude, "hospital|pharmacy|physiotherapist|dentist|doctor");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                GetNearbyResultsData getNearbyResultData = new GetNearbyResultsData();
                getNearbyResultData.execute(DataTransfer);
                Toast.makeText(ActivityMaps.this, "Please wait...\nLoading results for Nearby Hospitals!", Toast.LENGTH_LONG).show();
            }
        });

        menuSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParam = "school";
                mMap.clear();
                String url = getUrl(latitude, longitude, "school|library|book_store");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                GetNearbyResultsData getNearbyResultData = new GetNearbyResultsData();
                getNearbyResultData.execute(DataTransfer);
                Toast.makeText(ActivityMaps.this, "Please wait...\nLoading results for Nearby Schools!", Toast.LENGTH_LONG).show();
            }
        });

        menuRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParam = "restaurant";
                mMap.clear();
                String url = getUrl(latitude, longitude, "restaurant|meal_delivery|meal_takeaway");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                GetNearbyResultsData getNearbyResultData = new GetNearbyResultsData();
                getNearbyResultData.execute(DataTransfer);
                Toast.makeText(ActivityMaps.this, "Please wait...\nLoading results for Nearby Restaurants!", Toast.LENGTH_LONG).show();
            }
        });

        otherMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParam = "electrician|insurance_agency|jewelry_store|bus_station|gas_station|train_station|hindu_temple|shoe_store|electronics_store";
                mMap.clear();
                String url = getUrl(latitude, longitude, searchParam);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                GetNearbyResultsData getNearbyResultData = new GetNearbyResultsData();
                getNearbyResultData.execute(DataTransfer);
                Toast.makeText(ActivityMaps.this, "Please wait...\nLoading results for Nearby Restaurants!", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAG3K2UPK1-jIxJDO78jNSKdqVqLil9DFc");
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            currAddress = new StringBuilder();

            if (addresses.size() > 0) {
                if (addresses.get(0).getFeatureName() != null && !addresses.get(0).getFeatureName().equals("null"))
                    currAddress.append(addresses.get(0).getFeatureName());

                if (addresses.get(0).getAddressLine(0) != null && !addresses.get(0).getAddressLine(0).equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getAddressLine(0));
                }

                if (addresses.get(0).getSubLocality() != null && !addresses.get(0).getSubLocality().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getSubLocality());
                }

                if (addresses.get(0).getLocality() != null && !addresses.get(0).getLocality().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getLocality());
                }

                if (addresses.get(0).getSubAdminArea() != null && !addresses.get(0).getSubAdminArea().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getSubAdminArea());
                }

                if (addresses.get(0).getAdminArea() != null && !addresses.get(0).getAdminArea().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getAdminArea());
                }
                if (addresses.get(0).getCountryName() != null && !addresses.get(0).getCountryName().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getCountryName());
                }
                if (addresses.get(0).getPostalCode() != null && !addresses.get(0).getPostalCode().equals("null")) {
                    currAddress.append(" ");
                    currAddress.append(addresses.get(0).getPostalCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are Here!");
        markerOptions.snippet(currAddress.toString());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        TextView addressText = (TextView)findViewById(R.id.addressText);
        addressText.setVisibility(View.VISIBLE);
        addressText.setText(currAddress.toString());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(ActivityMaps.this, "Your Current Location", Toast.LENGTH_LONG).show();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
