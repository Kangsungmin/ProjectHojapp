package com.SMK.Hojapp.Chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ChatRoomData> chatList;
    private String nick = "unknownName";

    private EditText EditText_chat;
    private Button Button_make_room;
    private DatabaseReference myRef;
    private GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        globalData = (GlobalData) getApplicationContext();
        nick = "익명의 참여자";
        Button_make_room = findViewById(R.id.Button_make_room);

        // 방 생성 함수
        Button_make_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoomData chatRoom = new ChatRoomData("대화를 시작하세요.", "상대방 ID", System.currentTimeMillis());
                // 채팅방 DB 추가
                myRef.child("message_room_list").child(chatRoom.getRoomID()).setValue(chatRoom);
            }
        });

        mRecyclerView = findViewById(R.id.chat_room_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new AdapterChatRoom(chatList, ChatRoomListActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // caution!!!
        // Firebase DB로 부터 채팅방 리스트를 가져온다.
        myRef.child("message_room_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //TODO: 채팅방정보를 읽는 부분에서 싱크가 맞지 않아 죽는다. 수정해야 함. 콘텐츠 구현 참고.
                ChatRoomData chatRoom = dataSnapshot.getValue(ChatRoomData.class);
                ((AdapterChatRoom) mAdapter).addChat(chatRoom);
                Log.d("chat room added.", dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
