package com.SMK.Hojapp.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.SMK.Hojapp.R;
/*
 * 이곳에 현재 내가 속한 채팅 방 리스트가 표시된다.
 * 항목을 선택하면 ChatActivity로 이동한다.
 */
public class ChatRoomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);
    }
}
