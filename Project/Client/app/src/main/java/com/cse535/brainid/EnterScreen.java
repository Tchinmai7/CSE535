package  com.cse535.brainid;

import android.app.Activity;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;


public class EnterScreen extends Activity {

    String Classifier, FileName, accuracy, server, executionTime;
    TextView result1, result2, result3, result4, result5, result6, result7, result8;

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

        Intent calledActivity = getIntent();
        Classifier = calledActivity.getExtras().getString("classifier");
        FileName =  calledActivity.getExtras().getString("filename");
        accuracy = calledActivity.getExtras().getString("accuracy");
        server = calledActivity.getExtras().getString("server");
        executionTime = calledActivity.getExtras().getString("executionTime");
        int batteryDiff = calledActivity.getExtras().getInt("InitBattery") - batLevel;

        result1.setText("Classifier: " + Classifier );
        result2.setText("Accuracy: " + accuracy);
        result4.setText("FileName: " + FileName);
        result5.setText("Execution Time: " + executionTime+" ms");
        result6.setText("Server: " + server);
        result7.setText("Battery Used: " + batteryDiff);
        Realm realm = Realm.getDefaultInstance();
        AuthenticationHistory ah = Constants.getAhObject(realm);

        BarChart chart = (BarChart) findViewById(R.id.bar_chart);
        int numCloud = ah.getNumCloud();
        Log.e("Sss", numCloud + "");
        RealmList<String> classifierChoices = ah.getClassifiersUsed();
        HashMap<String, Integer> classiferCount = new HashMap<>();
        for (String s:classifierChoices) {
           classiferCount.put(s, classiferCount.getOrDefault(s, 0) + 1);
        }
        ArrayList<BarEntry> Barentry = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
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
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh



    }}
