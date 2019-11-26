package com.SMK.Hojapp.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.SMK.Hojapp.R;
import com.google.firebase.database.*;


public class JoinMemberActivity extends AppCompatActivity{
    private final String TAG = "JoinMemberActivity";
    Context context;
    EditText m_etId;
    String m_sId;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();

        m_etId = findViewById(R.id.nickNameEditText);
    }


    public void onJoin(View view)
    {
        m_sId = m_etId.getText().toString();
        // 닉네임 유효성 검사
        createNickname(m_sId);

    }

    void createNickname(String nick)
    {
        // 닉네임 중복, 특수문자 유효성 결과 리턴
        mDatabase.child("accounts").child(nick).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    // 이미 존재하는 닉네임
                    Toast.makeText(context, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // 계정 생성 진행
                    Toast.makeText(context, "계정생성 진행", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

}
