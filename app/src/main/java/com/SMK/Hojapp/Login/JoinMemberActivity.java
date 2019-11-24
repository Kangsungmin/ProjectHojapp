package com.SMK.Hojapp.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.SMK.Hojapp.R;


public class JoinMemberActivity extends AppCompatActivity {
    private final String TAG = "JoinMemberActivity";
    EditText m_etId;
    String m_sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);

        m_etId = (EditText) findViewById(R.id.nickNameEditText);
    }


    public void onJoin(View view)
    {
        m_sId = m_etId.getText().toString();
        // 닉네임 유효성 검사
    }

    boolean isValidNickName(String nick)
    {
        // 닉네임 중복, 특수문자 유효성 결과 리턴


        return true;
    }
}
