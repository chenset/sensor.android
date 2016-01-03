package com.flysay.sensor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class SensorPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        Sensor nasSensor = new NasSensor();
        Sensor routeSensor = new RouteSensor();
        Sensor.addSensor(nasSensor);
        Sensor.addSensor(routeSensor);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return Sensor.getSize();
            }

            @Override
            public Fragment getItem(int pos) {
                return SensorFragment.newInstance(pos);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Sensor sensor = Sensor.get(i);
                setTitle(sensor.getSensorName() + " Sensor");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        mViewPager.setCurrentItem(0);
        Sensor sensor = Sensor.get(0);
        setTitle(sensor.getSensorName() + " Sensor");
    }

}
