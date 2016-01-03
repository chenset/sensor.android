package com.flysay.sensor;

public class NasSensor extends Sensor {
    public String currentTemperatureUrl = "http://www.chenof.com:88/nas";
    public String TemperatureListUrl = "http://www.chenof.com:88/nas/temperatures";

    public String getSensorName() {
        return "Nas";
    }

    public String getTemperatureListUrl() {
        return TemperatureListUrl;
    }

    public String getCurrentTemperatureUrl() {
        return currentTemperatureUrl;
    }
}
