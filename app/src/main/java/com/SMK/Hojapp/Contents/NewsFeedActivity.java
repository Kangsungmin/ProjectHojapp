package com.SMK.Hojapp.Contents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "NewsFeedActivity";

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    AdapterContnets adapterContnets;

    private DatabaseReference newsFeedReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        newsFeedReference = FirebaseDatabase.getInstance().getReference().child("contents");

        //database = FirebaseDatabase.getInstance();
        findViewById(R.id.writeButton).setOnClickListener(this);

        // [데이터 초기화 시작]
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        // [데이터 초기화 완료]

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterContnets = new AdapterContnets(this, contentsArrayList);
        recyclerView.setAdapter(adapterContnets);

        swipeRefreshLayout.setOnRefreshListener(this);

        getNewsFeedData();
    }

    // DB의 뉴스피드 데이터들을 읽는 1회성 리스너 생성
    private void getNewsFeedData() {
        ValueEventListener preEventListener = new ValueEventListener() {
            // onDataChange() :
            // 리스너 : ValueEventListener
            // 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기합니다.
            // 리스너가 연결될 때 한 번 호출된 후 하위를 포함한 데이터가 변경될 때마다 다시 호출됩니다. (DB가 주도적으로 호출)
            // 모든 데이터를 포함하는 스냅샷이 이벤트 콜백에 전달
            // 데이터가 없으면 스냅샷은 exists() 호출 시 false를 반환하고, getValue() 호출 시 null을 반환
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
        // preEventListener는 일회성 이벤트 Listener. 호출된 이후에 자동 제거된다.
        newsFeedReference.addValueEventListener(preEventListener); // addValueEventListener를 사용하여 1회성 리스너로 사용.
    }

    private void populateRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 기존 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        contentsArrayList.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                contentsArrayList.add(val);
            }
        }
        adapterContnets.notifyDataSetChanged();     // [어댑터 변경 알림]
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

    // 새로고침 당기기 호출
    @Override
    public void onRefresh() {
        getNewsFeedData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
