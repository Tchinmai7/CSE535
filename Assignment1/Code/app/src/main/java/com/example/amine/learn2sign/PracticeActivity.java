package com.example.amine.learn2sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;

public class PracticeActivity extends AppCompatActivity {

    @BindView(R.id.tv_word_to_practice)
    TextView tv_word_to_practice;

    @BindView(R.id.ll_not_enough_videos)
    LinearLayout ll_not_enough_videos;

    @BindView(R.id.bt_go_back)
    Button bt_go_back;
    @BindView(R.id.ll_record_videos)
    LinearLayout ll_record_videos;
    @BindView(R.id.bt_record)
    Button bt_record;

    String chosenWord  = "";
    long timeStarted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        timeStarted = System.currentTimeMillis();
        // First check if the user has enough videos recorded to do this action.
        String url = "http://10.211.17.171/check_video_count.php";
        RequestParams params = new RequestParams();
        params.put("id", Constants.userId);
        //params.put("id", 1213090617);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                s = s.replaceAll(" ","");
                s = s.replaceAll("\n", "");

                int val = Integer.parseInt(s);
                String[] allWords = getResources().getStringArray(R.array.spinner_words);
                int totalWords = allWords.length;
                chosenWord =  allWords[(int) (Math.random() * totalWords)];
                // Each video needs to be uploaded 3 times
                if (val >= totalWords * 3) {
                    tv_word_to_practice.setText(chosenWord);
                    ll_not_enough_videos.setVisibility(View.GONE);
                    ll_record_videos.setVisibility(View.VISIBLE);
                } else {
                    ll_not_enough_videos.setVisibility(View.VISIBLE);
                    ll_record_videos.setVisibility(View.GONE);
                    bt_go_back.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("FAIL", "" + statusCode + ": " + new String(responseBody));
                ll_not_enough_videos.setVisibility(View.VISIBLE);
                ll_record_videos.setVisibility(View.GONE);
                bt_go_back.setVisibility(View.VISIBLE);

            }
        });
        bt_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PracticeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PracticeActivity.this, VideoActivity.class);
                intent.putExtra(INTENT_WORD, chosenWord);
                File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");
                timeStarted = System.currentTimeMillis() - timeStarted;
                if (!f.exists()) {
                    f.mkdirs();
                }
                intent.putExtra(INTENT_TIME_WATCHED, timeStarted);

                startActivityForResult(intent, Constants.REQUEST_VIDEO_CAPTURE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.e("OnActivityresult", requestCode + " " + resultCode);
        if (requestCode == Constants.REQUEST_VIDEO_CAPTURE && resultCode == Constants.RETURN_VIDEO_ACTIVITY_SUCCESS) {
            // Video successfully recorded.
            String returnUri = intent.getStringExtra(INTENT_URI);
            Intent in = new Intent(PracticeActivity.this, PracticeResultActivity.class);
            in.putExtra("UserVideo",returnUri );
            in.putExtra("Sign", chosenWord);
            startActivity(in);
            finish();
        }
    }
}
