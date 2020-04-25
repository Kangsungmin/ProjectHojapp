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

import com.SMK.Hojapp.Contents.AdapterContents;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/***************************/
/******라이더 프로필 리스트******/
/***************************/
public class DeliveryService extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    final String TAG = "DeliveryService";
    final String nameOfCategory = "profile_preview"; // DB에 저장되는 카테고리 이름
    private DatabaseReference dbReference;

    ArrayList<Contents> contentsArrayList;
    AdapterContents adapterContents;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public DeliveryService() {
        // 생성자
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_basic_fragment, container, false);

        dbReference = FirebaseDatabase.getInstance().getReference().child("delivery_contents");

        // [리소스 초기화 시작]
        recyclerView = (RecyclerView) v.findViewById(R.id.basicRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.basicSwipeLayout);
        // [리소스 초기화 끝]
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        contentsArrayList = new ArrayList<Contents>();
        adapterContents = new AdapterContents(v.getContext(), contentsArrayList);
        recyclerView.setAdapter(adapterContents);

        swipeRefreshLayout.setOnRefreshListener(this);

        getNewsFeedData();
        return v;
    }

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
        // 참고 : https://stackoverflow.com/questions/40471284/firebase-search-by-child-value
        Query query = dbReference.orderByChild("category").equalTo(nameOfCategory); // 쿼리 생성
        query.addValueEventListener(preEventListener);
        //marketDbReference
        //marketDbReference.addValueEventListener(preEventListener); // addValueEventListener를 사용하여 1회성 리스너로 사용.
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
        adapterContents.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    @Override
    public void onRefresh() {
        getNewsFeedData(); // 피드 새로고침 호출
        swipeRefreshLayout.setRefreshing(false);
    }
}