package com.cse535.brainid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {
    int PICKFILE_RESULT_CODE = 1;
    String filesrc = "";
    Button filePickerButton;
    TextView selectedFiles;
    EditText userNameet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        filePickerButton = findViewById(R.id.registerFilePicker);
        Button registerButton = findViewById(R.id.registerSubmit);
        selectedFiles = findViewById(R.id.filesSelected);
        userNameet = findViewById(R.id.registerUserName);
        filePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filesrc.equals("")) {
                    // dialog
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Error!")
                            .setMessage("Please Select a EEG File.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    String username = "";
                    username = userNameet.getText().toString();
                    if (username.equals("")) {
                        // dialog
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Error!")
                                .setMessage("Please Enter Username")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else {
                        final File f = new File(filesrc);
                        Log.e("REGISTER", filesrc);
                        AsyncHttpClient client_logs = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        try {
                            params.put("signal_file", f);
                            params.put("username", username);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        client_logs.post(Constants.cloudServer + "/register", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                if (statusCode == 200) {
                                    String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName();
                                    File root = new File(rootPath);
                                    Log.e("Register", rootPath);
                                    if (!root.exists()) {
                                       boolean res = root.mkdirs();
                                       Log.e("Register", "Result of Mkdir is " + res);
                                    }
                                    String filename = rootPath + "/" +f.getName();
                                    copy(f, new File(filename));
                                    new AlertDialog.Builder(RegisterActivity.this)
                                            .setTitle("Success!")
                                            .setMessage("You can now Login")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent in = new Intent(RegisterActivity.this, LoginScreen.class);
                                                    startActivity(in);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                                else
                                    Toast.makeText(RegisterActivity.this, "EEG File could not be uploaded. Try Again", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(RegisterActivity.this, "EEG File could not be uploaded", Toast.LENGTH_SHORT).show();

                            }
                        });
                        client_logs.post(Constants.fogServer + "/upload_log_file.php", params, new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                if (statusCode == 200) {
                                   // Toast.makeText(RegisterActivity.this, "Success, You can login now.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(RegisterActivity.this, "EEG File could not be uploaded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(RegisterActivity.this, "EEG File could not be uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
    private void copy(File source, File destination){
        try (FileChannel in = new FileInputStream(source).getChannel(); FileChannel out = new FileOutputStream(destination).getChannel()) {
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            Log.d("Register", e.toString());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Uri content_describer = data.getData();
            if (content_describer != null) {
                filesrc = FileUtils.getRealPath(RegisterActivity.this, content_describer);
                Log.e("REGISTER", filesrc);
                selectedFiles.setText(filesrc);
            }
        }
    }
}
