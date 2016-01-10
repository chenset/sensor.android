package com.flysay.sensor;

public class PiSensor extends Sensor {
    public String currentTemperatureUrl = "http://www.chenof.com:88/pi";
    public String TemperatureListUrl = "http://www.chenof.com:88/pi/temperatures";

    public String getSensorName() {
        return "Pi";
    }

    public String getTemperatureListUrl() {
        return TemperatureListUrl;
    }

    public String getCurrentTemperatureUrl() {
        return currentTemperatureUrl;
    }
}
