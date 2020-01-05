package com.SMK.Hojapp.Chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ChatRoomListActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ChatRoomData> chatList;
    private String nick = "unknownName";

    private EditText EditText_chat;
    private Button Button_make_room;
    private DatabaseReference myRef;
    private GlobalData globalData;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        globalData = (GlobalData) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nick = "익명의 참여자";
        //Button_make_room = findViewById(R.id.Button_make_room);

        // 방 생성 함수
       /*
        Button_make_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> newMember = new ArrayList<>();
                newMember.add("새로운 대화상대");
                ChatRoomData chatRoom = new ChatRoomData("대화를 시작하세요.", newMember, System.currentTimeMillis());
                // 채팅방 DB 추가
                myRef.child("message_room_list").child(chatRoom.getRoomID()).setValue(chatRoom);

            }
        });
       */


        mRecyclerView = findViewById(R.id.chat_room_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new AdapterChatRoom(chatList, ChatRoomListActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        // swipe 삭제 이벤트를 연결한다.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // caution!!!
        // Firebase DB로 부터 채팅방 리스트를 가져온다.
        // TODO : 자신이 속한 채팅방만 리스트로 가져온다.
        myRef.child("message_room_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //TODO: 채팅방정보를 읽는 부분에서 싱크가 맞지 않아 죽는다. 수정해야 함. 콘텐츠 구현 참고.
                ChatRoomData chatRoom = dataSnapshot.getValue(ChatRoomData.class);
                if( globalData.getAccount().isMyChatRoom(chatRoom.getRoomID()) ) { // 내가 참여한 채팅방이라면
                    ((AdapterChatRoom) mAdapter).addChat(chatRoom);
                    Log.d("chat room added.", dataSnapshot.getValue().toString());
                }

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

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // 삭제되는 아이템의 포지션을 가져온다
            final int position = viewHolder.getAdapterPosition();


            //메모리와 DB에서 해당 채팅방을 가져와서 멤버리스트 중 자신을 삭제한다.
            ChatRoomData room = chatList.get(position);
            //메모리 삭제
            globalData.getAccount().removeChatRoom(room.getRoomID());
            chatList.remove(position);
            //DB삭제
            mDatabase.child("message_room_list").child(room.getRoomID()).child("members").child(globalData.getAccount().getUid()).removeValue();
            mDatabase.child("accounts").child(globalData.getAccount().getUid()).child("roomDataMap").child(room.getRoomID()).removeValue();

            // 아답타에게 알린다
            mAdapter.notifyItemRemoved(position);
        }
    };

}
