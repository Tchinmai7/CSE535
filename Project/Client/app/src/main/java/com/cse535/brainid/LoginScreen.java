package  com.cse535.brainid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class LoginScreen extends Activity implements AdapterView.OnItemSelectedListener {

    private List<String> fileList = new ArrayList<String>();
    String choice,classChoice = "Naive Bayes";
    String dbFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
           // "/sdcard/Android/data/";
    EditText user_name;
    File root,selected_File;
    TextView cloudLatencyTV;
    TextView fogLatencyTV;
    Spinner classifiername, fileName;
    public int accuracy = 0;
    public static long cloudLatency, fogLatency;
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

        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        cloudLatencyTV = findViewById(R.id.cloud_latency);
        fogLatencyTV = findViewById(R.id.fog_latency);

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
                res = "";
                final ProgressDialog dialog = new ProgressDialog(LoginScreen.this);
                final long startTimer;

                if (fileName.getSelectedItem() == null) {
                    new AlertDialog.Builder(LoginScreen.this)
                            .setTitle("Error!")
                            .setMessage("Select a file")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }  else {
                    cloudLatency = measureLatency(Constants.cloudServer, true);
                    fogLatency = measureLatency(Constants.fogServer, false);
                    AuthenticationHistory ah = Constants.getAhObject(realm);
                    realm.beginTransaction();
                    ah.addCloudLatency(cloudLatency);
                    ah.addFogLatency(fogLatency);
                    ah.addAuthAttempt();
                    boolean choice_of_server = useCloudServer();
                    String server_url;
                    if (choice_of_server) {
                        server_url = Constants.cloudServer;
                        ah.addNumCloud();
                        choice = "Cloud";
                    } else {
                        server_url = Constants.fogServer;
                        ah.addNumFog();
                        choice = "Fog";
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
                        params.put("classifier", classChoice);
                        params.put("username", user_name.getText().toString());
                        params.put("signalFile", selected_File);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    client_logs.post(server_url + "/login", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                AuthenticationHistory ah = Constants.getAhObject(realm);
                                realm.beginTransaction();

                                // accuracy = Integer.parseInt(new String(responseBody));
                                accuracy = 80;
                                ah.addAccuracy(accuracy);
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                long timer = System.currentTimeMillis() - startTimer;
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
                                i.putExtra("executionTime", Long.toString(timer));
                                i.putExtra("InitBattery", batLevel);
                                if (cloudLatency > fogLatency) {
                                    i.putExtra("networkDelay",fogLatency);
                                }
                                else {
                                    i.putExtra("networkDelay",cloudLatency);
                                }
                                realm.copyToRealm(ah);
                                realm.commitTransaction();
                                startActivity(i);
                            }
                            else
                                Toast.makeText(LoginScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(LoginScreen.this, "Error with Request", Toast.LENGTH_SHORT).show();
                        }
                    });

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

    private boolean useCloudServer() {
        return cloudLatency <= fogLatency;
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
    private long measureLatency(String url, final boolean isCloud) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        final long startTime = System.currentTimeMillis();
        final long[] latency = new long[1];
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
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
                } else {
                    msg = "Fog Latency: " + msg;
                    fogLatencyTV.setText(msg);
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
