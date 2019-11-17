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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.Contents.ContentsTypes.ViewType;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.Account;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

/*
 * 콘텐츠 상세화면
 * 게시물의 제목, 작성자, 내용, 작성시간, 댓글이 표시된다.
 */
public class ContentsDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    final String TAG = "ContentsDetailActivity";

    String nowContent_ID = "";
    //TextView categoryView, titleView, writerNameView, writeTimeView, bodyTextView, hitCountView, likeCountView;
    private EditText commentInputText;
    private  TextView enterText;

    ArrayList<Contents> ContentsDetailDataArrayList;
    AdapterDetailedContents adapterDetailedContents; // 댓글 어댑터

    RecyclerView recyclerView; // 댓글 리사이클러뷰
    SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference dbReference;
    private GlobalData globalData;

    private InputMethodManager inputMethodManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_detail);

        dbReference = FirebaseDatabase.getInstance().getReference().child("contents");
        globalData = (GlobalData) getApplicationContext();

        commentInputText = (EditText) findViewById(R.id.commentInput);
        enterText = (TextView) findViewById(R.id.enterTextView);
        enterText.setOnClickListener(this);

        Intent intent = getIntent(); /*데이터 수신*/
        nowContent_ID = intent.getExtras().getString("CONTENTS_ID");

        recyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contentsDetailSwipeLayout);
        ContentsDetailDataArrayList = new ArrayList<Contents>();
        adapterDetailedContents = new AdapterDetailedContents(getApplicationContext(), ContentsDetailDataArrayList);
        recyclerView.setAdapter(adapterDetailedContents);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        swipeRefreshLayout.setOnRefreshListener(this);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // 상단 게시글을 ContentsDetailDataArrayList 에 추가한다.
        setContentsDetailData(intent);
        getCommentContentsData();
    }

    /*
    DB의 해당 콘텐츠(게시글)에 대한 업데이트를 받아서 데이터를 갱신한다.
     */
    private void setContentsDetailData(Intent intent) {
        ValueEventListener contentsPreEventListener = new ValueEventListener() {
            // onDataChange() :
            // 리스너 : ValueEventListener
            // 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기합니다.
            // 리스너가 연결될 때 한 번 호출된 후 하위를 포함한 데이터가 변경될 때마다 다시 호출됩니다. (DB가 주도적으로 호출)
            // 모든 데이터를 포함하는 스냅샷이 이벤트 콜백에 전달
            // 데이터가 없으면 스냅샷은 exists() 호출 시 false 를 반환하고, getValue() 호출 시 null을 반환
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populateContentsRecyclerView(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        // 현재 콘텐츠와 같은 게시물만 받아온다.
        Query query = dbReference.orderByChild("cid").equalTo( intent.getExtras().getString("CONTENTS_ID") );
        query.addValueEventListener(contentsPreEventListener);

        adapterDetailedContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    /*
    DB의 해당 [콘텐츠의 댓글]에 대한 업데이트를 받아서 데이터를 갱신한다.
    */
    private void getCommentContentsData() {
        ValueEventListener commentPreEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populateCommentRecyclerView(dataSnapshot);
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
        query.addValueEventListener(commentPreEventListener);
    }

    // 게시글 UI 갱신
    private void populateContentsRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        // FireBase 의 데이터베이스에서 뉴스피드데이터를 받아온다.
        // 댓글을 제외한 게시글(0번째 리스트 원소)을 삭제후 다시 추가한다.
        clearContents();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                ContentsDetailDataArrayList.add(0, val);
            }
        }
        adapterDetailedContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    // 댓글 UI 갱신
    private void populateCommentRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 기존 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        // 게시글을 제외한 댓글만 삭제한다.
        clearComments();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                ContentsDetailDataArrayList.add(val);
            }
        }
        adapterDetailedContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    private void clearContents(){
        if(ContentsDetailDataArrayList.size() > 0) {
            Contents detailedContents = ContentsDetailDataArrayList.remove(0);
        }
    }

    private void clearComments() {
        // 상단 게시물을 제외하고 댓글 모두 삭제
        if(ContentsDetailDataArrayList.size() > 0) {
            Contents detailedContents = ContentsDetailDataArrayList.get(0);
            ContentsDetailDataArrayList.clear();
            ContentsDetailDataArrayList.add(detailedContents);
        }
        else {
            // 게시물
            Log.w(TAG, "ContentsDetailDataArrayList size is zero.");
        }
    }

    // 댓글 작성 함수
    private void writeNewComment(Account user, String body) {
        long nowTime = System.currentTimeMillis();
        Contents contents = new Contents(ViewType.COMMENT, nowContent_ID, body, user.getUid(), user.getName(), nowTime);
        dbReference.child(contents.getCid()).setValue(contents);

        // 키패드 사라짐
        inputMethodManager.hideSoftInputFromWindow(commentInputText.getWindowToken(),0);
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
