package  com.cse535.brainid;

import android.app.Activity;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;


public class EnterScreen extends Activity {

    String Classifier, FileName, server, executionTime;
    double accuracy;
    TextView result1, result2, result3, result4, result5, result6, result7, result8, result9;

    private void chartBarGraph(AuthenticationHistory ah) {
        BarChart chart = (BarChart) findViewById(R.id.classifer_bar);
        RealmList<String> classifierChoices = ah.getClassifiersUsed();
        HashMap<String, Integer> classiferCount = new HashMap<>();
        for (String s:classifierChoices) {
            classiferCount.put(s, classiferCount.getOrDefault(s, 0) + 1);
        }
        ArrayList<BarEntry> Barentry = new ArrayList<>();
        HashSet<String> labels = new HashSet<>();
        float pos = 0f;

        for (Map.Entry<String, Integer> e: classiferCount.entrySet()) {
            BarEntry be = new BarEntry(pos, e.getValue());
            pos += 2f;
            Barentry.add(be);
            labels.add(e.getKey());
        }

        BarDataSet dataSet = new BarDataSet(Barentry, "Classifiers");
        BarData data = new BarData(dataSet);
        chart.setData(data);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
    }
    private void chartLine(List<Double> values, LineChart chart, String title, TextView id) {
        if (values.size() == 0) {
            chart.setVisibility(View.GONE);
            id.setVisibility(View.GONE);
        }
        List<Entry> entries = new ArrayList<Entry>();
        Integer i = 0;
        for (Double v: values) {
           entries.add(new Entry(i.floatValue(),  v.floatValue()));
           i ++;
        }
        LineDataSet dataSet = new LineDataSet(entries, title);
        LineData lineData = new LineData(dataSet);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.setData(lineData);
        chart.invalidate();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_screen);

        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        result1 = findViewById(R.id.tvResult);
        result2 = findViewById(R.id.tvResult1);
        result3 = findViewById(R.id.tvResult3);
        result4 = findViewById(R.id.tvResult4);
        result5 = findViewById(R.id.tvResult5);
        result6 = findViewById(R.id.tvResult6);
        result7 = findViewById(R.id.tvResult7);
        result8 = findViewById(R.id.tvResult8);
        result9 = findViewById(R.id.tvResult9);

        Intent calledActivity = getIntent();
        Classifier = calledActivity.getExtras().getString("classifier");
        FileName =  calledActivity.getExtras().getString("filename");
        accuracy = calledActivity.getExtras().getDouble("accuracy");
        server = calledActivity.getExtras().getString("server");
        executionTime = calledActivity.getExtras().getString("executionTime");
        int batteryDiff = calledActivity.getExtras().getInt("InitBattery") - batLevel;

        result1.setText("Classifier: " + Classifier );
        result2.setText("Accuracy: " + accuracy);
        result4.setText("FileName: " + FileName);
        result5.setText("Execution Time: " + executionTime+" ms");
        result6.setText("Server: " + server);
        result7.setText("Battery Used: " + batteryDiff);
        result9.setText("Authentication Result: " + calledActivity.getStringExtra("Result"));
        Realm realm = Realm.getDefaultInstance();
        AuthenticationHistory ah = Constants.getAhObject(realm);
        chartBarGraph(ah);
        chartLine(ah.getAccuracies(), (LineChart) findViewById(R.id.accuracies_line), "Accuracies", (TextView)findViewById(R.id.tv_accuracy));
        chartLine(ah.getCloudExecutionTimes(), (LineChart)findViewById(R.id.cloud_exec_time), "Cloud Exection Time", (TextView) findViewById(R.id.tv_cloud_exec_time));
        chartLine(ah.getFogExecutionTimes(), (LineChart)findViewById(R.id.fog_exec_time), "Fog Execution Time", (TextView)findViewById(R.id.tv_fog_exec_time));
        chartLine(ah.getFogLatencies(), (LineChart)findViewById(R.id.fog_latency_line), "Fog Latencies", (TextView) findViewById(R.id.tv_fog_latency));
        chartLine(ah.getCloudLatencies(), (LineChart)findViewById(R.id.cloud_latency_line), "Cloud Latencies", (TextView)findViewById(R.id.tv_cloud_latency));
    }
}
