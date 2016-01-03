package com.flysay.sensor;

import java.util.ArrayList;

public abstract class Sensor {
    private static ArrayList<Sensor> sensors = new ArrayList<>();

    public abstract String getCurrentTemperatureUrl();

    public abstract String getTemperatureListUrl();

    public abstract String getSensorName();

    public static void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public static int getSize() {
        return sensors.size();
    }

    public static Sensor get(int pos) {
        return sensors.get(pos);
    }
}
