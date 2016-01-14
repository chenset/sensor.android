package com.flysay.sensor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class SensorPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private SensorFragment currentF;
    private HomeFragment currentHomeF;

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mViewPager.getAdapter().notifyDataSetChanged();

        return super.onMenuItemSelected(featureId, item);
    }

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
                    currentHomeF = HomeFragment.newInstance(pos);
                    return currentHomeF;
                } else {
                    currentF = SensorFragment.newInstance(pos - 1);
                    return currentF;
                }
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
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
    }
}
