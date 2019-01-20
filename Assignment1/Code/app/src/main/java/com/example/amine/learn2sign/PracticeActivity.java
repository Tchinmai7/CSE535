package com.example.amine.learn2sign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PracticeActivity extends AppCompatActivity {
    @BindView(R.id.vv_user_video)
    VideoView vvUserVideo;

    @BindView(R.id.vv_original_video)
    VideoView vvOriginalVideo;

    @BindView(R.id.bt_accept_video)
    Button btAcceptVideo;

    @BindView(R.id.bt_reject_video)
    Button btRejectVideo;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        Intent callingIntent = getIntent();
        Log.e("Word", callingIntent.getStringExtra("Sign"));
        Toast.makeText(this, callingIntent.getStringExtra("Sign"), Toast.LENGTH_SHORT).show();
        String path = Constants.getFilePath(callingIntent.getStringExtra("Sign"), getPackageName());
        vvUserVideo.setOnCompletionListener(onCompletionListener);
        vvOriginalVideo.setOnCompletionListener(onCompletionListener);
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vvOriginalVideo.setVideoURI(uri);
            vvOriginalVideo.start();

        }
        filename = callingIntent.getStringExtra("UserVideo");
        vvUserVideo.setVideoURI(Uri.parse(filename));
        vvUserVideo.start();
    }

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mediaPlayer != null)
            {
                mediaPlayer.start();
            }
        }
    };

    @OnClick(R.id.bt_accept_video)
    public void acceptVideo() {
        Constants.clicksLogger.updateLog("Video - Rejected");
        Toast.makeText(this, "Accept Video", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_reject_video)
    public void rejectVideo() {
        this.setResult(Constants.VIDEO_REJECTED);
        File f = new File(filename);
        // Just to be safe
        if (f.exists()) {
            boolean ret = f.delete();
        }
        this.finish();
    }
}
