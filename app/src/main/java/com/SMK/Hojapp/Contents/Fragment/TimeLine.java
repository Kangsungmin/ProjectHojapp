package com.SMK.Hojapp.Contents.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.SMK.Hojapp.Contents.AdapterContnets;
import com.SMK.Hojapp.Contents.Contents;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
/*
 * 모든 카테고리를 보여주는 타임라인
 */
public class TimeLine extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    final String TAG = "TimeLine";

    private DatabaseReference newsFeedDbReference;

    ArrayList<Contents> contentsArrayList = new ArrayList<>();
    AdapterContnets adapterContnets;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    public TimeLine() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_time_line, container, false);

        newsFeedDbReference = FirebaseDatabase.getInstance().getReference().child("contents");

        // [리소스 초기화 시작]
        recyclerView = (RecyclerView) v.findViewById(R.id.timeLineRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.timeLineSwipeLayout);
        // [리소스 초기화 끝]
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapterContnets = new AdapterContnets(v.getContext(), contentsArrayList);
        recyclerView.setAdapter(adapterContnets);

        swipeRefreshLayout.setOnRefreshListener(this);

        getNewsFeedData();

        return v;
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

        Query query = newsFeedDbReference.orderByChild("createTime");
        query.addValueEventListener(preEventListener);
        // preEventListener는 일회성 이벤트 Listener. 호출된 이후에 자동 제거된다.
        // newsFeedDbReference.addValueEventListener(preEventListener); // addValueEventListener를 사용하여 1회성 리스너로 사용.
    }

    private void populateRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 기존 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        contentsArrayList.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            // contents의 하위 카테고리의 contents들을 가져온다.
            Contents val = snapshot.getValue(Contents.class);
            if(val != null) {
                contentsArrayList.add(0, val); // 시간에 대해 오름차순 결과를 리턴 받기에(내림차순 지원X) 리스트 앞에 삽입.
            }
        }
        adapterContnets.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    @Override
    public void onRefresh() {
        getNewsFeedData();
        swipeRefreshLayout.setRefreshing(false);
    }
}