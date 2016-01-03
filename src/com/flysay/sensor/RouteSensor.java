package com.flysay.sensor;

public class RouteSensor extends Sensor {
    public String currentTemperatureUrl = "http://www.chenof.com:88/route";
    public String TemperatureListUrl = "http://www.chenof.com:88/route/temperatures";

    public String getSensorName() {
        return "Route";
    }

    public String getTemperatureListUrl() {
        return TemperatureListUrl;
    }

    public String getCurrentTemperatureUrl() {
        return currentTemperatureUrl;
    }
}
