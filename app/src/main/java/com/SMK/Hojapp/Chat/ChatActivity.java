package com.SMK.Hojapp.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
        final String TAG = "ChatActivity";
    private DatabaseReference chatDbReference;
    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String roomID = ""; // 현재 방의 식별자
    private String nick = "unknownName";

    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;
    private GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        globalData = (GlobalData) getApplicationContext();
        chatDbReference = FirebaseDatabase.getInstance().getReference().child("message");

        Intent intent = getIntent(); /*데이터 수신*/
        roomID = intent.getExtras().getString("ID_ROOM");
        nick = globalData.getAccount().getUid();
        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString(); //msg

                if(msg != null) {
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    chat.setRoomID(roomID);
                    myRef.child("message").push().setValue(chat); //push().setValue(chat);
                }
            }
        });



        mRecyclerView = findViewById(R.id.chat_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new AdapterChat(chatList, ChatActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // 메세지 갱신함수
        getChatData();
    }


    private void getChatData() {
        // 리스너 생성
        // 정렬
        // 리스너 추가
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

        Query query = chatDbReference.orderByChild("roomID").equalTo(roomID); // 쿼리 생성. 현재 방과 일치하는 채팅 필터
        query.addValueEventListener(preEventListener);
    }

    private void populateRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        chatList.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            // contents의 하위 카테고리의 contents들을 가져온다.
            ChatData val = snapshot.getValue(ChatData.class);
            if(val != null) {
                chatList.add(val); // 리스트 뒤에 삽입
            }
        }
        mAdapter.notifyDataSetChanged();     // [어댑터 변경 알림]
    }

    @Override
    public void onRefresh() {
        getChatData(); // 피드 새로고침 호출
        //swipeRefreshLayout.setRefreshing(false);
    }
}