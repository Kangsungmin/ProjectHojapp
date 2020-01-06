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
    private String TAG = "ChatRoomListActivity";
    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ChatRoomData> chatList;
    private String nick = "unknownName";

    private EditText EditText_chat;
    private Button Button_make_room;
    private GlobalData globalData;
    private DatabaseReference mDatabase;
    private DatabaseReference mChatRoomRef;

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

        // 액티비티 시작시, DB에서 자신이 속한 채팅방 ID리스트를 받아온다.
        mChatRoomRef = mDatabase.child("accounts").child(globalData.getAccount().getUid()).child("roomDataMap");
        mChatRoomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String roomID = dataSnapshot.getKey();
                if(globalData.getAccount().isMyChatRoom(roomID) == false) { // 현재 없는 roomID라면.
                    globalData.getAccount().addChatRoom(roomID);
                    mAdapter.notifyDataSetChanged();     // [어댑터 변경 알림]
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

        // caution!!!
        // Firebase DB로 부터 채팅방 리스트를 가져온다.
        // TODO : 자신이 속한 채팅방만 리스트로 가져온다.
        mDatabase.child("message_room_list").addChildEventListener(new ChildEventListener() {
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
            //채팅방 나감 메세지 전송
            String lastMsg =  globalData.getAccount().getName() + "님이 채팅을 나갔습니다.";
            final ChatData chat = new ChatData(room.getRoomID(), lastMsg, globalData.getAccount().getName(), System.currentTimeMillis());
            mDatabase.child("message").push().setValue(chat);

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

    //유저의 채팅방 정보를 DB에서 읽어온다.


    private void populateRecyclerView(@NonNull DataSnapshot dataSnapshot) {
        //Firebase의 데이터베이스에서 뉴스피드데이터를 받아온다.
        //1. 채팅방 리스트 초기화
        //2. 스냅샷 객체화 & 리스트 추가
        //3. 어댑터 갱신
        //globalData.getAccount().clearChatRoomMap();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            String roomID = snapshot.getKey();
            if(roomID.equals("") == false) {
                globalData.getAccount().addChatRoom(roomID);
            }
        }
        mAdapter.notifyDataSetChanged();     // [어댑터 변경 알림]
    }
}
