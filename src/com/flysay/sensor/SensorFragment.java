package com.flysay.sensor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class SensorFragment extends Fragment {
    private LineChart mLineChart;
    private RelativeLayout rl;
    private TextView nasTemp;
    public final static String TAG = "TTTTTTTTTTTTTTTTTT";

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.main, viewGroup, false);

        mLineChart = (LineChart) v.findViewById(R.id.spread_line_chart);

        rl = (RelativeLayout) v.findViewById(R.id.main);
//        rl.setBackgroundColor(Color.BLUE);

        nasTemp = (TextView) v.findViewById(R.id.nasCurrentTemperature);

        new ChangeMainTemperatureTask().execute("http://www.chenof.com:88/nas");
        new ChangeLineChartTask().execute("http://www.chenof.com:88/nas/temperatures");
        return v;
    }

    private class ChangeMainTemperatureTask extends AsyncTask<String, Void, String> {
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
            Log.d(TAG, html);

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(html);
            } catch (Exception e) {
                throw new RuntimeException();
            }

            try {
                nasTemp.setText(jsonObject.getString("CPU"));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    private class ChangeLineChartTask extends AsyncTask<String, Void, String> {
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
            System.out.println(html);
            JSONArray jsonArr;
            try {
                jsonArr = new JSONArray(html);
            } catch (JSONException e) {
                throw new RuntimeException();
            }

            JSONObject jsonObj;

            ArrayList<Double> yData = new ArrayList<>();
            ArrayList<String> xData = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    jsonObj = jsonArr.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException();
                }

                try {
                    yData.add(jsonObj.getDouble("CPU"));
                    System.out.println(jsonObj.getString("CPU"));
                    System.out.println(jsonObj.getInt("add_time"));

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    String d = format.format(new Date(jsonObj.getLong("add_time") * 1000));
//                    Date date = format.parse(d);

                    System.out.println("Format To String(Date):" + d);

                    xData.add(d);
//                    System.out.println("Format To Date:" + date);

                } catch (Exception e) {
                    throw new RuntimeException();
                }

                LineData mLineData = getLineData(yData, xData);
                showChart(mLineChart, mLineData, Color.alpha(0));

            }

        }
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.alpha(0)); // 表格的的颜色，在这里是是给颜色设置一个透明度
        // enable touch gestures
        XAxis xAxis = lineChart.getXAxis();
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        xAxis.setEnabled(false);

        lineChart.fitScreen();

        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.BLUE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

//        lineChart.animateX(0); // 立即执行的动画,x轴
        lineChart.animateY(1000);
    }

    /**
     * 生成一个数据
     * @return LineData
     */
    private LineData getLineData(ArrayList<Double> yData, ArrayList<String> xValues) {

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            yValues.add(new Entry(new Float(yData.get(i)), i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setCircleColorHole(Color.BLACK);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.WHITE);
//        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色

//        lineDataSet.setDrawCubic(true);
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setFillAlpha(255);
//        lineDataSet.setDrawCircles(false);
//        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);


        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        return new LineData(xValues, lineDataSets);
    }
}
