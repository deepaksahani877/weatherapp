package com.app.weatherapp;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp_min")
    String minTemp;
    @SerializedName("temp_max")
    String maxTemp;
    String pressure;
    String humidity;
    String temp;

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    String getTemp(){
        return temp;
    }


}
