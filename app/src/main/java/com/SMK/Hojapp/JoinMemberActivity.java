package com.SMK.Hojapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import io.socket.client.Socket;


public class JoinMemberActivity extends AppCompatActivity {
    private final String TAG = "JoinMemberActivity";
    private Socket m_socket;
    EditText m_etId, m_etPw, m_etPwChk;
    String m_sId, m_sPw, m_sPwChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);

        m_etId = (EditText) findViewById(R.id.editEmailText);
        m_etPw = (EditText) findViewById(R.id.editPwText);
        m_etPwChk = (EditText) findViewById(R.id.editPwchkText);
    }


    public void OnJoin(View view)
    {
        m_sId = m_etId.getText().toString();
        m_sPw = m_etPw.getText().toString();
        m_sPwChk = m_etPwChk.getText().toString();

        if(m_sPw.equals(m_sPwChk))
        {
            /*패스워드 일치*/

        }
        else
        {
            /*패스워드 불일치*/
        }
    }


}
