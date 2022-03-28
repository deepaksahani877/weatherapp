package com.app.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Weather {
    @SerializedName("weather")
    ArrayList<WeatherCondition> weatherCondition;
    Main main;
    Wind wind;
    Sys sys;
    String name;



    public ArrayList<WeatherCondition> getWeatherCondition() {
        return weatherCondition;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public String getName() {
        return name;
    }




}
