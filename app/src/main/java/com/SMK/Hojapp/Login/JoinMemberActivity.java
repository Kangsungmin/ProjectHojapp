package com.SMK.Hojapp.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.SMK.Hojapp.Contents.NewsFeedActivity;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.*;


public class JoinMemberActivity extends AppCompatActivity{
    private final String TAG = "JoinMemberActivity";
    Context context;
    private FirebaseAuth mAuth;
    GlobalData globalData;
    EditText m_etId;
    String m_sId;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();
        globalData = (GlobalData) getApplicationContext();
        mAuth = FirebaseAuth.getInstance();

        m_etId = findViewById(R.id.nickNameEditText);
    }


    public void onJoin(View view)
    {
        m_sId = m_etId.getText().toString();
        // 닉네임 유효성 검사
        createNickname(m_sId);

    }

    void createNickname(final String nick)
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
                    // Account 객체 생성 후 쿼리 전송
                    // Toast.makeText(context, "계정생성 진행", Toast.LENGTH_SHORT).show();
                    globalData.setAccountNick(nick);
                    registerAccountToDB();

                    //Firebase 프로필 업데이트 진행
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nick)
                            .build();

                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                    startNewsFeedActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    void registerAccountToDB()
    {
        // 현재 계정을 DB 등록 요청한다.
        mDatabase.child("accounts").child(globalData.getAccount().getUid()).setValue(globalData.getAccount());
    }

    protected void startNewsFeedActivity() {
        Intent i = new Intent(JoinMemberActivity.this, NewsFeedActivity.class); startActivity(i);
        finish();
    }

}
