package  com.cse535.brainid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginScreen extends Activity implements AdapterView.OnItemSelectedListener {

    private List<String> fileList = new ArrayList<String>();
    String choice,classChoice = "Naive Bayes";
    String dbFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
           // "/sdcard/Android/data/";
    EditText user_name;
    File root,selected_File;
    TextView cloudLatencyTV;
    TextView fogLatencyTV, serverChoiceTV;
    Spinner classifiername, fileName;
    public double accuracy = 0;
    public double cloudLatency, fogLatency;
    String res = "";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int batLevel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        cloudLatency = measureLatency(Constants.cloudServer, true);
        fogLatency = measureLatency(Constants.fogServer, false);
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        cloudLatencyTV = findViewById(R.id.cloud_latency);
        fogLatencyTV = findViewById(R.id.fog_latency);
        serverChoiceTV = findViewById(R.id.serverChoice);
        classifiername = findViewById(R.id.class_spinner);
        fileName = findViewById(R.id.file_spinner);

        user_name = findViewById(R.id.user_name);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(LoginScreen.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.servers));

        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(LoginScreen.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.classifiers));

        newAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        classifiername.setAdapter(newAdapter);

        classifiername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classChoice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            root = new File(dbFilePath + "/" + getPackageName());
            if (root.listFiles() != null) {
                ListDir(root);
            }
        }

        final Button btn_Login = findViewById(R.id.btnLogin);
        Button btn_Register = findViewById(R.id.btnRegister);

        final Realm realm = Realm.getDefaultInstance();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fogLatencyTV.setText("");
                cloudLatencyTV.setText("");
                serverChoiceTV.setText("");
                res = "";
                final ProgressDialog dialog = new ProgressDialog(LoginScreen.this);
                final long startTimer;
                if (user_name.getText().toString().equals("")) {
                    new AlertDialog.Builder(LoginScreen.this)
                            .setTitle("Error!")
                            .setMessage("Enter a userName")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else if (!Constants.isValidUserName(user_name.getText().toString())) {
                    new AlertDialog.Builder(LoginScreen.this)
                            .setTitle("Error!")
                            .setMessage("Invalid Username. Only S001 - S109 allowed.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    if (fileName.getSelectedItem() == null) {
                        new AlertDialog.Builder(LoginScreen.this)
                                .setTitle("Error!")
                                .setMessage("Select a file")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else {
                        AuthenticationHistory ah = Constants.getAhObject(realm);
                        realm.beginTransaction();
                        ah.addCloudLatency(cloudLatency);
                        ah.addFogLatency(fogLatency);
                        ah.addAuthAttempt();
                        boolean choice_of_server = useCloudServer(ah);

                        String server_url;
                        if (choice_of_server) {
                            Log.e("Register", "Using Cloud Server");
                            server_url = Constants.cloudServer;
                            ah.addNumCloud();
                            choice = "Cloud";
                            serverChoiceTV.setText("Choosing Cloud Server");
                        } else {
                            server_url = Constants.fogServer;
                            ah.addNumFog();
                            choice = "Fog";
                            serverChoiceTV.setText("Choosing Fog Server");

                        }
                        realm.copyToRealm(ah);
                        realm.commitTransaction();

                        startTimer = System.currentTimeMillis();
                        dialog.setTitle("Login Loader");
                        dialog.setMessage("Authenticating......");
                        dialog.show();

                        AsyncHttpClient client_logs = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        try {
                            params.put("ClassifierName", classChoice);
                            params.put("UserName", user_name.getText().toString());
                            params.put("UserSignalFile", selected_File);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        client_logs.post(server_url + "/login", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                if (statusCode == 200) {
                                    String authResult = "";
                                    AuthenticationHistory ah = Constants.getAhObject(realm);
                                    realm.beginTransaction();
                                    try {
                                        JSONObject jObject = new JSONObject(new String(responseBody));
                                        accuracy = jObject.getDouble("accuracy");
                                        authResult = jObject.getString("status");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    ah.addAccuracy(accuracy);
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    double timer = System.currentTimeMillis() - startTimer;
                                    ah.addClassifier(classifiername.getSelectedItem().toString());
                                    if ("cloud".equals(choice)) {
                                        ah.addCloudExecutionTime(timer);
                                    } else {
                                        ah.addFogExecutionTime(timer);
                                    }

                                    Intent i = new Intent(LoginScreen.this, EnterScreen.class);
                                    i.putExtra("classifier", classifiername.getSelectedItem().toString());
                                    i.putExtra("filename", fileName.getSelectedItem().toString());
                                    i.putExtra("accuracy", accuracy);
                                    i.putExtra("server", choice);
                                    i.putExtra("executionTime", Double.toString(timer));
                                    i.putExtra("InitBattery", batLevel);
                                    i.putExtra("Result", authResult);
                                    if (cloudLatency > fogLatency) {
                                        i.putExtra("networkDelay", fogLatency);
                                    } else {
                                        i.putExtra("networkDelay", cloudLatency);
                                    }
                                    realm.copyToRealm(ah);
                                    realm.commitTransaction();
                                    startActivity(i);
                                } else
                                    Toast.makeText(LoginScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("Login", new String(responseBody));
                                Toast.makeText(LoginScreen.this, "Error with Request", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginScreen.this, RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    private boolean useCloudServer(AuthenticationHistory ah) {
        if (cloudLatency <= Long.MAX_VALUE && fogLatency <= Long.MAX_VALUE) {
            // Both servers are up.
            Double cloud_avg = getAverage(ah.getCloudExecutionTimes());
            Double fog_avg = getAverage(ah.getFogExecutionTimes());
            return (cloudLatency <= fogLatency) && (cloud_avg <= fog_avg);
        }
        return cloudLatency <= fogLatency;
    }

    public static double getAverage(List<Double> list) {
        Double sum = 0.0;
        if(!list.isEmpty()) {
            for (Double val : list) {
                sum += val;
            }
            return sum / list.size();
        }
        return sum;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void ListDir(File root) {

        File[] files = root.listFiles();
        fileList.clear();

        for(File file:files){
            fileList.add(file.getName());

        }
        Spinner spinner = findViewById(R.id.file_spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> file_list = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fileList);
        file_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(file_list);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        File[] files = root.listFiles();

        for(File file:files){
            if(file.getName().toString().equals(item))
            {
                selected_File =  file;
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private double measureLatency(String url, final boolean isCloud) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        final long startTime = System.currentTimeMillis();
        final double[] latency = new double[1];
        asyncHttpClient.get(url + "/latency", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                long endTime = System.currentTimeMillis();
                String msg = "Error contacting server";
                if (statusCode == 200) {
                    latency[0] = endTime - startTime;
                    msg = latency[0] + " ms";
                } else {
                    latency[0] = Long.MAX_VALUE;
                }
                if (isCloud) {
                    msg = "Cloud Latency: " + msg;
                    cloudLatencyTV.setText(msg);
                    cloudLatency = latency[0];
                } else {
                    msg = "Fog Latency: " + msg;
                    fogLatencyTV.setText(msg);
                    fogLatency = latency[0];
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                latency[0] = Long.MAX_VALUE;
                String msg = "Error contacting server";
                if (isCloud) {
                    cloudLatencyTV.setText(msg);
                } else {
                    fogLatencyTV.setText(msg);
                }
            }
        });
        return latency[0];
    }
}
