package com.app.weatherapp;

import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    @SerializedName("main")
    String condition;
    String description;
    String icon;

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

}
