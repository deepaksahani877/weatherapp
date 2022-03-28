package com.app.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView locationTextView;
    ImageView weatherImageView;
    TextView temperatureTextView;
    TextView weatherDescriptionTextView;
    TextView humidityTextView;
    TextView maxTemperatureTextView;
    TextView minTemperatureTextView;
    TextView windSpeedTextView;
    TextView pressureTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    String lat;
    String lon;
    ProgressBar progressBar;
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = findViewById(R.id.locationTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        maxTemperatureTextView = findViewById(R.id.maxTempTextView);
        minTemperatureTextView = findViewById(R.id.minTempTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        group = findViewById(R.id.group);
        progressBar = findViewById(R.id.progressBar);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude() + "";
                lon = location.getLongitude() + "";
                WeatherApi api = WeatherApiClient.getInstance().getApi();
                api.getWeather(lat, lon).enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                        if (response.isSuccessful()) {

                           // Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            Weather weather = response.body();
                            locationTextView.setText(weather.getSys().country + "," + weather.name);
                            temperatureTextView.setText(weather.getMain().getTemp() + " °C");
                            weatherDescriptionTextView.setText(weather.getWeatherCondition().get(0).getDescription());
                            humidityTextView.setText("Humidity:" + weather.getMain().getHumidity() + "%");
                            maxTemperatureTextView.setText("Max Temp:" + weather.getMain().getMaxTemp());
                            minTemperatureTextView.setText("Min Temp:" + weather.getMain().getMinTemp());
                            windSpeedTextView.setText("Wind Speed:" + weather.getWind().getSpeed());
                            pressureTextView.setText("Pressure:" + weather.getMain().getPressure());
                            String url = "https://openweathermap.org/img/wn/" + weather.getWeatherCondition().get(0).getIcon() + "@4x.png";
                            Glide.with(getApplicationContext()).load(url).into(weatherImageView);
                            progressBar.setVisibility(View.INVISIBLE);
                            group.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
               // startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                displayLocationSettingsRequest(getApplicationContext());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, locationListener);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissions.length > 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, locationListener);
            } else {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                       // Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 77);
                        } catch (IntentSender.SendIntentException e) {
                           // Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                       // Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}