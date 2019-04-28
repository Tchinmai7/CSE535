package  com.cse535.brainid;

import android.app.Activity;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.TextView;


public class EnterScreen extends Activity {

    String Classifier, FileName, accuracy, server, executionTime;
    TextView result1, result2, result3, result4, result5, result6, result7, result8;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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


        result1.setText("Classifier: " + Classifier );
        result2.setText("Accuracy: " + accuracy);
        result4.setText("FileName: " + FileName);
        result5.setText("Execution Time: " + executionTime+" ms");
        result6.setText("Server: " + server);
        result7.setText("Battery Level: " + batLevel);


}}
