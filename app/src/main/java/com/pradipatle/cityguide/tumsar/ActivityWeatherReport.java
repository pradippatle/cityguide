package com.pradipatle.cityguide.tumsar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pradipatle.cityguide.tumsar.Common.CommonMethods;
import com.pradipatle.cityguide.tumsar.model.WeatherModel;

import org.json.JSONException;

/**
 * Created by Aeon admin on 14-Jul-16.
 */
public class ActivityWeatherReport extends AppCompatActivity {


    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView hum;
    private ImageView imgView, actionbar_left_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String city = "Tumsar,IN";
        actionbar_left_icon =  (ImageView) findViewById(R.id.actionbar_left_icon);
        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        imgView = (ImageView) findViewById(R.id.condIcon);
        CommonMethods.showDialog(ActivityWeatherReport.this,"Loading\nPlease wait....",false);
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(city);
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
        actionbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private class JSONWeatherTask extends AsyncTask<String, Void, WeatherModel> {

        @Override
        protected WeatherModel doInBackground(String... params) {
            WeatherModel weather = new WeatherModel();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(WeatherModel weather) {
            super.onPostExecute(weather);
            CommonMethods.dismissDialog();
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

         //   cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "  (" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + ""+(char) 0x00B0 +" C");
            hum.setText("Humidity : " + weather.currentCondition.getHumidity() + "%");
            press.setText("Pressure : " + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("Wind : " + weather.wind.getSpeed() + " mps");

        }







    }
}