package com.app.weatherapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {

    static private WeatherApiClient instance = null;
    static private WeatherApi api = null;

    public WeatherApiClient() {
        if (instance == null) {
            Retrofit client = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = client.create(WeatherApi.class);
        }
    }

    static public WeatherApiClient getInstance() {
        if (instance == null) {
            instance = new WeatherApiClient();
        }
        return instance;
    }

    WeatherApi getApi() {
        return api;
    }
}
