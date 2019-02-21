package com.example.amine.learn2sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PracticeActivity extends AppCompatActivity {

    @BindView(R.id.tv_word_to_practice)
    TextView tv_word_to_practice;

    @BindView(R.id.ll_not_enough_videos)
    LinearLayout ll_not_enough_videos;

    @BindView(R.id.bt_go_back)
    Button bt_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);

        // First check if the user has enough videos recorded to do this action.
        String url = "http://10.211.17.171/check_video_count.php";
        RequestParams params = new RequestParams();
        params.put("id", Constants.userId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("UPLOAD", s);
                ll_not_enough_videos.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("FAIL", "" + statusCode + ": " + new String(responseBody));
                ll_not_enough_videos.setVisibility(View.VISIBLE);
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
        /*
         } else {
            // Its in Practice Mode, so now we need to goto practice activity, and show both
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("Sign", chosenWord);
            intent.putExtra("UserVideo", returnedURI);
            startActivityForResult(intent, Constants.REQUEST_SHOW_VIDEO);
        }
         */
    }
}
