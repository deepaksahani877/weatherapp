package com.app.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("/data/2.5/weather?appid=3d9f7cc8462987c55a080b88ac971165&units=metric")
    Call<Weather> getWeather(@Query("lat") String latitude, @Query(value = "lon") String longitude);
}
