package com.example.amine.learn2sign;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.amine.learn2sign.LoginActivity.INTENT_EMAIL;
import static com.example.amine.learn2sign.LoginActivity.INTENT_ID;
import static com.example.amine.learn2sign.LoginActivity.INTENT_SERVER_ADDRESS;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rg_practice_learn)
    RadioGroup rg_practice_learn;

    @BindView(R.id.rb_learn)
    RadioButton rb_learn;

    @BindView(R.id.rb_practice)
    RadioButton rb_practice;

    @BindView(R.id.sp_words)
    Spinner sp_words;

    @BindView(R.id.sp_ip_address)
    Spinner sp_ip_address;

    @BindView(R.id.vv_video_learn)
    VideoView vv_video_learn;

    @BindView(R.id.vv_record)
    VideoView vv_record;

    @BindView(R.id.bt_record)
    Button bt_record;

    @BindView(R.id.bt_proceed)
    Button btProceed;

    @BindView(R.id.bt_cancel)
    Button bt_cancel;

    @BindView(R.id.ll_after_record)
    LinearLayout ll_after_record;

    @BindView(R.id.tv_word_to_practice)
    TextView tv_word_to_practice;

    String path;
    String returnedURI;
    String oldText = "";
    String chosenWord = "";
    String[] spinnerWordsArray;
    SharedPreferences sharedPreferences;
    long timeStarted = 0;
    long timeStartedReturn = 0;
    Activity mainActivity;
    boolean practiceMode = false;

    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind xml to activity
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        spinnerWordsArray = getResources().getStringArray(R.array.spinner_words);
        rb_learn.setChecked(true);
        bt_cancel.setVisibility(View.GONE);
        btProceed.setVisibility(View.GONE);
        sharedPreferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("Number_Accepted", 0) < 3) {
            rb_practice.setVisibility(View.GONE);
        } else {
            rb_practice.setVisibility(View.VISIBLE);
        }

        rg_practice_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_learn.getId()) {
                    Toast.makeText(getApplicationContext(),"Learn",Toast.LENGTH_SHORT).show();
                    vv_video_learn.setVisibility(View.VISIBLE);
                    tv_word_to_practice.setVisibility(View.GONE);
                    sp_words.setVisibility(View.VISIBLE);
                    vv_video_learn.start();
                    vv_record.setVisibility(View.GONE);
                    sp_words.setEnabled(true);
                    practiceMode = false;
                    timeStarted = System.currentTimeMillis();
                } else if (checkedId == rb_practice.getId()) {
                    Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT).show();
                    vv_video_learn.setVisibility(View.GONE);
                    sp_words.setVisibility(View.GONE);
                    tv_word_to_practice.setVisibility(View.VISIBLE);
                    chosenWord = spinnerWordsArray[new Random().nextInt(spinnerWordsArray.length)];
                    tv_word_to_practice.setText(chosenWord);
                    practiceMode = true;
                    timeStarted = System.currentTimeMillis();
                }
            }
        });

        sp_words.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = sp_words.getSelectedItem().toString();
                if(!oldText.equals(text)) {
                    path = "";
                    timeStarted = System.currentTimeMillis();
                    play_video(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_ip_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sharedPreferences.edit().putString(INTENT_SERVER_ADDRESS, sp_ip_address.getSelectedItem().toString()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null)
                {
                    mediaPlayer.start();
                }

             }
        };

        vv_record.setOnCompletionListener(onCompletionListener);
        vv_video_learn.setOnCompletionListener(onCompletionListener);
        vv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vv_record.start();
            }
        });
        vv_video_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vv_video_learn.isPlaying()) {
                    vv_video_learn.start();
                }
            }
        });
        timeStarted = System.currentTimeMillis();
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if(intent.hasExtra(INTENT_EMAIL) && intent.hasExtra(INTENT_ID)) {
            Toast.makeText(this,"User : " + intent.getStringExtra(INTENT_EMAIL),Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Already Logged In",Toast.LENGTH_SHORT).show();

        }
        Constants.email = getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE)
                .getString(INTENT_EMAIL,"tchinmai7@asu.edu");
        Constants.userId = getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE)
                .getString(INTENT_ID,"983837234");
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {

        if (vv_video_learn.getVisibility() == View.VISIBLE) {
            vv_video_learn.start();
        }
        if (vv_record.getVisibility() == View.VISIBLE) {
            vv_record.start();
        }
        timeStarted = System.currentTimeMillis();
        super.onResume();

    }

    public void play_video(String text) {
        oldText = text;
        path = Constants.getFilePath(text, getPackageName());
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vv_video_learn.setVideoURI(uri);
            vv_video_learn.start();
        }
    }

    @OnClick(R.id.bt_record)
    public void record_video() {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            // Permission has already been granted
             File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

             if (!f.exists()) {
                 f.mkdirs();
             }

             timeStarted = System.currentTimeMillis() - timeStarted;

             Intent t = new Intent(this,VideoActivity.class);
             if (!practiceMode) {
                 t.putExtra(INTENT_WORD, sp_words.getSelectedItem().toString());
             } else {
                 t.putExtra(INTENT_WORD, chosenWord);
             }
             t.putExtra(INTENT_TIME_WATCHED, timeStarted);
             startActivityForResult(t, Constants.REQUEST_VIDEO_CAPTURE);
        }
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

    @OnClick(R.id.bt_proceed)
    public void sendToServer() {
        if (!practiceMode) {
            Toast.makeText(this,"Send to Server",Toast.LENGTH_SHORT).show();
            Intent t = new Intent(this, UploadActivity.class);
            startActivityForResult(t, Constants.REQUEST_CODE_UPLOAD);
        } else {
            // Its in Practice Mode, so now we need to goto practice activity, and show both
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("Sign", chosenWord);
            intent.putExtra("UserVideo", returnedURI);
            startActivityForResult(intent, Constants.REQUEST_SHOW_VIDEO);
        }
    }

    @OnClick(R.id.bt_cancel)
    public void cancel() {
        vv_record.setVisibility(View.GONE);
        if(rb_learn.isSelected()) {
            vv_video_learn.setVisibility(View.VISIBLE);
        }
        bt_record.setVisibility(View.VISIBLE);
        btProceed.setVisibility(View.GONE);
        bt_cancel.setVisibility(View.GONE);

        sp_words.setEnabled(true);

        rb_learn.setEnabled(true);
        //rb_practice.setEnabled(true);
        timeStarted = System.currentTimeMillis();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.e("OnActivityresult",requestCode+" "+resultCode);
        if (requestCode == Constants.REQUEST_CODE_UPLOAD) {
            //from video activity
            vv_record.setVisibility(View.GONE);
            rb_learn.setChecked(true);
            bt_cancel.setVisibility(View.GONE);
            btProceed.setVisibility(View.GONE);
            bt_record.setVisibility(View.VISIBLE);
            sp_words.setEnabled(true);
            rb_learn.setEnabled(true);
            //rb_practice.setEnabled(true);
            sp_ip_address.setEnabled(true);
        }

        if (requestCode == Constants.REQUEST_VIDEO_CAPTURE && resultCode == Constants.RETURN_VIDEO_ACTIVITY_SUCCESS) {
            if (intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                returnedURI = intent.getStringExtra(INTENT_URI);
                timeStartedReturn = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
                vv_record.setVisibility(View.VISIBLE);
                bt_record.setVisibility(View.GONE);
                btProceed.setVisibility(View.VISIBLE);
                bt_cancel.setVisibility(View.VISIBLE);
                //rb_practice.setEnabled(false);
                vv_record.setVideoURI(Uri.parse(returnedURI));
                int try_number = sharedPreferences.getInt("record_"+sp_words.getSelectedItem().toString(),0);
                try_number++;
                String toAdd  = sp_words.getSelectedItem().toString()+"_"+try_number+"_"+ timeStartedReturn + "";
                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("RECORDED",new HashSet<String>());
                set.add(toAdd);
                sharedPreferences.edit().putStringSet("RECORDED",set).apply();
                sharedPreferences.edit().putInt("record_"+sp_words.getSelectedItem().toString(), try_number).apply();
                vv_record.start();
                vv_video_learn.start();
            }

        }

        if (requestCode == Constants.REQUEST_VIDEO_CAPTURE && resultCode == Constants.RETURN_VIDEO_ACTIVITY_ABORT )
        {
            if (intent != null) {
                //create folder
                if(intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                    returnedURI = intent.getStringExtra(INTENT_URI);
                    timeStartedReturn = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
                    File f = new File(returnedURI);
                    f.delete();
                    timeStarted = System.currentTimeMillis();
                    vv_video_learn.start();
                }
            }
        }
        if (requestCode == Constants.REQUEST_SHOW_VIDEO && resultCode == Constants.VIDEO_REJECTED) {
            // Okay, so user rejected. now show him a different video
            vv_record.setVisibility(View.GONE);
            bt_record.setVisibility(View.VISIBLE);
            bt_cancel.setVisibility(View.GONE);
            btProceed.setVisibility(View.GONE);
            chosenWord = spinnerWordsArray[new Random().nextInt(spinnerWordsArray.length)];
            tv_word_to_practice.setText(chosenWord);
        }

    }

    //Menu Item for logging out
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mainActivity = this;
                    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("ALERT");
                    alertDialog.setMessage("Logging out will delete all the data!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().clear().apply();
                                    File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");
                                    if (f.isDirectory())
                                    {
                                        String[] children = f.list();
                                        for (int i = 0; i < children.length; i++)
                                        {
                                            new File(f, children[i]).delete();
                                        }
                                    }
                                    startActivity(new Intent(mainActivity,LoginActivity.class));
                                    mainActivity.finish();

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                    return true;
            case R.id.menu_upload_server:
                sharedPreferences.edit().putInt(getString(R.string.gotoupload), sharedPreferences.getInt(getString(R.string.gotoupload),0)+1).apply();
                Intent t = new Intent(this,UploadActivity.class);
                startActivityForResult(t,2000);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SaveFile extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            try {
                fileOutputStream = new FileOutputStream(strings[0]);
                fileInputStream = (FileInputStream) getContentResolver().openInputStream(Uri.parse(strings[1]));
                Log.d("msg", fileInputStream.available() + " ");
                byte[] buffer = new byte[1024];
                while (fileInputStream.available() > 0) {

                    fileInputStream.read(buffer);
                    fileOutputStream.write(buffer);
                    publishProgress(fileInputStream.available()+"");
                }

                fileInputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Video Saved Successfully",Toast.LENGTH_SHORT).show();
        }
    }
}
