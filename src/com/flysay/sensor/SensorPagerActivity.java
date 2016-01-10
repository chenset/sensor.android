package com.flysay.sensor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SensorPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        Sensor nasSensor = new NasSensor();
        Sensor piSensor = new PiSensor();
        Sensor routeSensor = new RouteSensor();
        Sensor.addSensor(nasSensor);
        Sensor.addSensor(piSensor);
        Sensor.addSensor(routeSensor);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return Sensor.getSize() + 1;
            }

            @Override
            public Fragment getItem(int pos) {
                if (pos == 0) {
                    return Home.newInstance(pos);
                } else {
                    return SensorFragment.newInstance(pos - 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int pos) {
                if (pos == 0) {
                    setTitle("Room Sensor");
                } else {
                    Sensor sensor = Sensor.get(pos - 1);
                    setTitle(sensor.getSensorName() + " Sensor");
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        mViewPager.setCurrentItem(0);
        setTitle("Room Sensor");
////        Sensor sensor = Sensor.get(0);
//        setTitle(sensor.getSensorName() + " Sensor");
    }

}
