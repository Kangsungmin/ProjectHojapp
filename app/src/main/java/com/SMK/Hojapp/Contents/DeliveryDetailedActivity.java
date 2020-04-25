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

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class DeliveryDetailedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    final String TAG = "DeliveryDetailedActivity";

    RecyclerView recyclerView; // 댓글 리사이클러뷰
    SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference dbReference;
    private GlobalData globalData;

    ArrayList<Contents> deliveryDataArrayList;
    AdapterContents adapterContents;
    private String nowTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detailed);

        dbReference = FirebaseDatabase.getInstance().getReference().child("delivery_contents");
        globalData = (GlobalData) getApplicationContext();

        Intent intent = getIntent(); /*데이터 수신*/
        nowTitle = intent.getExtras().getString("TITLE");

        recyclerView = (RecyclerView) findViewById(R.id.deliveryRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.deliveryDetailSwipeLayout);
        deliveryDataArrayList = new ArrayList<>();
        adapterContents = new AdapterContents(getApplicationContext(), deliveryDataArrayList);
        recyclerView.setAdapter(adapterContents);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        swipeRefreshLayout.setOnRefreshListener(this);
        getDeliveryData();
    }

    private void getDeliveryData() {
        ValueEventListener commentPreEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populateCommentRecyclerView(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        // 참고 : https://stackoverflow.com/questions/40471284/firebase-search-by-child-value
        // 현재 배달 카테고리에 속하는 음식 리스트를 가져온다.
        // 쿼리 생성: 현재 선택한 타이틀 (&& 현재 유저 위치) 파이어베이스 db는 && 사용 불가 따라서 스냅샷의 데이터를 검열하여 필요한 데이터만 표시한다.
        Query query = dbReference.orderByChild("category").equalTo(nowTitle);
        query.addValueEventListener(commentPreEventListener);
    }

    private void populateCommentRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 기존 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        // 게시글을 제외한 댓글만 삭제한다.
        clearDeliveryData();

        Log.w("Delivery", "data snapshot" + dataSnapshot.getChildrenCount());
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                // 현재 사용자 위치로 필터링
                String nowLocation = globalData.getAccount().getLocation();
                if(val.getLocationHash().containsKey(nowLocation))
                {
                    deliveryDataArrayList.add(val);
                }
            }
        }
        adapterContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    private void clearDeliveryData() {
            deliveryDataArrayList.clear();
    }

    @Override
    public void onRefresh() {
        getDeliveryData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
