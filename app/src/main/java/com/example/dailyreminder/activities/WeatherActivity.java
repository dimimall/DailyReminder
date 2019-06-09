package com.example.dailyreminder.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dailyreminder.R;
import com.example.dailyreminder.Utils.WeatherHttpClient;
import com.example.dailyreminder.Utils.utils;
import com.example.dailyreminder.models.Weather;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    TextView txtLocation, txtTemperature, txtWindSpeed, txtHumidity, txtPressure, txtWeatherDescription;
    ImageView weatherIcon,search;

    final int REQUEST_CODE = 1234;
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final String API_KEY = "538b1f109f84a4fb26067c4032b83044";

    final long MIN_TIME = 5000;

    final float MIN_DISTANCE = 1000;

    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    // Create Instance of LocationManager and LocationListener
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    String cityName = "Thessaloniki,Gr";

    utils myutils = new utils();

    double lat = 0.0,lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.main_action_bar, null);
        getSupportActionBar().setCustomView(customView);


    }

    public void init(){
        txtLocation = findViewById(R.id.txtLocation);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtPressure = findViewById(R.id.txtPressure);
        txtWeatherDescription = findViewById(R.id.txtWeatherDescription);
        weatherIcon = findViewById(R.id.weatherIcon);
    }

    @Override
    public void onResume()
    {
        super.onResume();

//        if (cityName != null)
//            getNewCityWeather(cityName);
//        else
            getCurrentLocationWeather();

        search = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imageView);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myutils.checkInternetConenction(WeatherActivity.this))
                {
                    Intent intent = new Intent(WeatherActivity.this,MapsActivity.class);
                    intent.putExtra("latitude",lat);
                    intent.putExtra("longitude",lon);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(WeatherActivity.this, "No internet connection! Try to connect to a working internet and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getNewCityWeather(String cityName) {

        RequestParams requestParams = new RequestParams();

        requestParams.put("q", cityName);
        requestParams.put("appid", API_KEY);

        apiCall(requestParams);
    }

    private void getCurrentLocationWeather() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                RequestParams requestParams = new RequestParams();

                requestParams.put("lat", latitude);
                requestParams.put("lon", longitude);
                requestParams.put("appid", API_KEY);

                apiCall(requestParams);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

            return;
        }

        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);

        if (mLocationManager.isProviderEnabled(LOCATION_PROVIDER))
        {
            Location location = mLocationManager.getLastKnownLocation(LOCATION_PROVIDER);
            if (location != null)
            {
                lat = location.getLatitude();
                lon = location.getLongitude();

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                RequestParams requestParams = new RequestParams();

                requestParams.put("lat", latitude);
                requestParams.put("lon", longitude);
                requestParams.put("appid", API_KEY);

                apiCall(requestParams);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void apiCall(RequestParams requestParams) {

        if (myutils.checkInternetConenction(getApplicationContext())) {

            AsyncHttpClient client = new AsyncHttpClient();

            client.get(WEATHER_URL, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Weather weather = Weather.fromJson(response);
                    updateWeatherDetails(weather);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    Toast.makeText(WeatherActivity.this, "Error occurred while making request! "+throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(this, "No internet connection! Try to connect to a working internet and try again", Toast.LENGTH_LONG).show();
        }
    }

    private void updateWeatherDetails(Weather weather) {

        txtTemperature.setText(weather.getTemperature());
        txtWindSpeed.setText(weather.getWind());
        txtHumidity.setText(weather.getHumidity());
        txtPressure.setText(weather.getPressure());
        txtWeatherDescription.setText(weather.getDescription());
        txtLocation.setText(weather.getCity() + ", " + weather.getCountry());

//        int resourceIdentifier = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
//        weatherIcon.setImageResource(resourceIdentifier);
        Glide.with(WeatherActivity.this).load("https://openweathermap.org/img/w/"+weather.getIconName()+".png").into(weatherIcon);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
