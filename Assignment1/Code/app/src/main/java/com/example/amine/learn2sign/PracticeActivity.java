package com.example.amine.learn2sign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        Intent callingIntent = getIntent();
        String path = Constants.getFilePath(callingIntent.getStringExtra("Sign"), getPackageName());
        vvUserVideo.setOnCompletionListener(onCompletionListener);
        vvOriginalVideo.setOnCompletionListener(onCompletionListener);
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vvOriginalVideo.setVideoURI(uri);
            vvOriginalVideo.start();

        }
        vvUserVideo.setVideoURI(Uri.parse(callingIntent.getStringExtra("UserVideo")));
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
        Toast.makeText(this, "Accept Video", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.bt_reject_video)
    public void rejectVideo() {
        Toast.makeText(this, "Reject Video", Toast.LENGTH_LONG).show();
    }
}
