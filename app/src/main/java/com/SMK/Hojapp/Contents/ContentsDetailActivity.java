package com.SMK.Hojapp.Contents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.Account;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

/*
 * 콘텐츠 상세화면
 * 게시물의 제목, 작성자, 내용, 작성시간, 댓글이 표시된다.
 */
//TODO: 댓글을 RecyclerView가 아닌
public class ContentsDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    final String TAG = "ContentsDetailActivity";

    String nowContent_ID = "";
    TextView categoryView, titleView, writerNameView, writeTimeView, bodyTextView, hitCountView, likeCountView;
    private EditText commentInputText;
    private  TextView enterText;

    ArrayList<Contents> commentContentsArrayList;
    AdapterComment adapterCommentContents; // 댓글 어댑터

    RecyclerView recyclerView; // 댓글 리사이클러뷰
    SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference dbReference;
    private GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_detail);

        dbReference = FirebaseDatabase.getInstance().getReference().child("contents");
        globalData = (GlobalData) getApplicationContext();

        categoryView = (TextView) findViewById(R.id.categoryView);
        titleView = (TextView) findViewById(R.id.titleView);
        writerNameView = (TextView) findViewById(R.id.writerView);
        writeTimeView = (TextView) findViewById(R.id.writeTimeView);
        bodyTextView = (TextView) findViewById(R.id.contentsTextView);
        hitCountView = (TextView) findViewById(R.id.rowContentsHitcountView);
        likeCountView = (TextView) findViewById(R.id.rowContentsLikeCountView);
        commentInputText = (EditText) findViewById(R.id.commentInput);
        enterText = (TextView) findViewById(R.id.enterTextView);
        enterText.setOnClickListener(this);

        Intent intent = getIntent(); /*데이터 수신*/

        nowContent_ID = intent.getExtras().getString("CONTENTS_ID");
        categoryView.setText("#" + intent.getExtras().getString("CATEGORY"));
        titleView.setText(intent.getExtras().getString("TITLE"));
        writerNameView.setText(intent.getExtras().getString("WRITER"));
        writeTimeView.setText(intent.getExtras().getString("TIME"));
        bodyTextView.setText(intent.getExtras().getString("BODY"));
        hitCountView.setText(intent.getExtras().getString("HIT_COUNT"));
        likeCountView.setText(intent.getExtras().getString("LIKE_COUNT"));

        recyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contentsDetailSwipeLayout);
        commentContentsArrayList = new ArrayList<Contents>();
        adapterCommentContents = new AdapterComment(getApplicationContext(), commentContentsArrayList);
        recyclerView.setAdapter(adapterCommentContents);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        swipeRefreshLayout.setOnRefreshListener(this);

        getCommentContentsData();
    }

    private void getCommentContentsData() {
        ValueEventListener preEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populateRecyclerView(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        // 참고 : https://stackoverflow.com/questions/40471284/firebase-search-by-child-value
        // 현재 게시글의 cid의 하위에 있는 댓글만 가져온다.
        Query query = dbReference.orderByChild("parentCid").equalTo(nowContent_ID); // 쿼리 생성
        query.addValueEventListener(preEventListener);
    }

    private void populateRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 기존 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        commentContentsArrayList.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                commentContentsArrayList.add(val);
            }
        }
        adapterCommentContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    private void writeNewComment(Account user, String body) {
        long nowTime = System.currentTimeMillis();
        Contents contents = new Contents(nowContent_ID, body, user.getUid(), user.getName(), nowTime);
        dbReference.child(contents.getCid()).setValue(contents);
        // TODO : 작성완료 팝업 출력
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enterTextView :
                writeNewComment(globalData.getAccount(), commentInputText.getText().toString());
                break;
        }
    }

    @Override
    public void onRefresh() {
        getCommentContentsData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
