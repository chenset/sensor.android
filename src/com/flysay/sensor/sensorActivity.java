package com.flysay.sensor;

import android.support.v4.app.Fragment;

public class sensorActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new SensorFragment();
    }
}
