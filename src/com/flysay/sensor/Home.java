package com.flysay.sensor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.IOException;

public class Home extends Fragment {
    public final static String EXTRA_ID = "EXTRA_ID";
    public TextView roomCurrentTemperature;
    public TextView roomCurrentHumidity;

    public static Home newInstance(int pos) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ID, pos);

        Home fragment = new Home();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceStats) {
        super.onCreate(saveInstanceStats);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.home, viewGroup, false);
        roomCurrentTemperature = (TextView) v.findViewById(R.id.roomCurrentTemperature);
        roomCurrentHumidity = (TextView) v.findViewById(R.id.roomCurrentHumidity);

        new HomeSensorTask().execute("http://www.chenof.com:88/room");
        return v;
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
