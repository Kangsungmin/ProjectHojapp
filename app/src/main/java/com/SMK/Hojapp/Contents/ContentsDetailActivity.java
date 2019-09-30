package com.SMK.Hojapp.Contents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.SMK.Hojapp.R;
/*
 * 콘텐츠 상세화면
 * 게시물의 제목, 작성자, 내용, 작성시간, 등이 표시된다.
 */
public class ContentsDetailActivity extends AppCompatActivity {

    TextView categoryView, titleView, writerNameView, writeTimeView, bodyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_detail);

        categoryView = (TextView) findViewById(R.id.categoryView);
        titleView = (TextView) findViewById(R.id.titleView);
        writerNameView = (TextView) findViewById(R.id.writerView);
        writeTimeView = (TextView) findViewById(R.id.writeTimeView);
        bodyTextView = (TextView) findViewById(R.id.contentsTextView);

        Intent intent = getIntent(); /*데이터 수신*/
        categoryView.setText(intent.getExtras().getString("CATEGORY"));
        titleView.setText(intent.getExtras().getString("TITLE"));
        writerNameView.setText(intent.getExtras().getString("WRITER"));
        writeTimeView.setText(intent.getExtras().getString("TIME"));
        bodyTextView.setText(intent.getExtras().getString("BODY"));
    }
}
