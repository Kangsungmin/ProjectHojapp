package com.SMK.Hojapp.Contents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.SMK.Hojapp.R;

public class NewsFeedActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "NewsFeedActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        findViewById(R.id.writeButton).setOnClickListener(this);
    }

    // 액티비티가 시작할 때.
    @Override
    protected  void onStart() {
        Log.d(TAG, "start activity");
        super.onStart();
    }

    // 액티비티가 화면 상단으로 왔을 때.
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "event occurred.");

        int i = view.getId();
        if (i == R.id.writeButton) { // Open WriteContentsActivity
            Intent intent = new Intent(NewsFeedActivity.this, WriteContentsActivity.class); startActivity(intent);
        }
    }
}
