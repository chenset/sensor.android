package com.flysay.sensor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.IOException;

public class HomeFragment extends Fragment {
    public final static String EXTRA_ID = "EXTRA_ID";
    public TextView roomCurrentTemperature;
    public TextView roomCurrentHumidity;

    public static HomeFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ID, pos);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onCreate(Bundle saveInstanceStats) {
        super.onCreate(saveInstanceStats);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.home, viewGroup, false);
        roomCurrentTemperature = (TextView) v.findViewById(R.id.roomCurrentTemperature);
        roomCurrentHumidity = (TextView) v.findViewById(R.id.roomCurrentHumidity);

        refreshData();
        return v;
    }

    public void refreshData() {
        new HomeSensorTask().execute("http://www.chenof.com:88/room");
    }

    private class HomeSensorTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String html;
            try {
                html = HTTP.get(params[0]);
            } catch (IOException ioe) {
                throw new RuntimeException();
            }

            return html;
        }

        @Override
        protected void onPostExecute(String html) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(html);
            } catch (Exception e) {
                throw new RuntimeException();
            }

            try {
                roomCurrentTemperature.setText(jsonObject.getString("temperature") + "°");
                roomCurrentHumidity.setText(jsonObject.getString("humidity") + "%");
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
}
