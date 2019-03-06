package com.example.amine.learn2sign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PracticeResultActivity extends AppCompatActivity {
    @BindView(R.id.vv_user_video)
    VideoView vvUserVideo;

    @BindView(R.id.vv_original_video)
    VideoView vvOriginalVideo;

    @BindView(R.id.bt_accept_video)
    Button btAcceptVideo;

    @BindView(R.id.bt_reject_video)
    Button btRejectVideo;
    String filename;
    String Sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_result);
        ButterKnife.bind(this);
        Intent callingIntent = getIntent();
        Sign = callingIntent.getStringExtra("Sign");
        Log.e("Word", Sign);
        String path = Constants.getFilePath(Sign, getPackageName());
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
        Constants.clicksLogger.updateLog("Video: " + filename + " - Accepted");
        Toast.makeText(this, "Accept Video", Toast.LENGTH_SHORT).show();
        // Upload the video now
        String prodURL = "http://10.211.17.171/upload_video_performance.php";
        RequestParams params = new RequestParams();
        try {
            File f = new File(filename);
            params.put("uploaded_file", f);
            params.put("id", Constants.userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(prodURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String res = new String(responseBody);
                Log.e("RESULT", res);
                if (statusCode == 200 && "success".equals(res))
                    Toast.makeText(PracticeResultActivity.this, "Done", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PracticeResultActivity.this, "Video could not be uploaded, Try again", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(PracticeResultActivity.this, PracticeActivity.class);
                startActivity(in);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PracticeResultActivity.this, "Video could not be uploaded, Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.bt_reject_video)
    public void rejectVideo() {
        this.setResult(Constants.VIDEO_REJECTED);
        Constants.clicksLogger.updateLog("Video: " + filename + " - Rejected");
        File f = new File(filename);
        // Just to be safe
        if (f.exists()) {
            boolean ret = f.delete();
        }
        Intent in = new Intent(PracticeResultActivity.this, PracticeActivity.class);
        in.putExtra("Word", Sign);
        startActivity(in) ;
        this.finish();
    }
}
