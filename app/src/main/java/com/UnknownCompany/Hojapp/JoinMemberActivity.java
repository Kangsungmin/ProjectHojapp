package com.UnknownCompany.Hojapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


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

        //로그인&회원가입 서버 연결
        try{
            m_socket = IO.socket("http://192.168.234.49:3000");
            m_socket.connect();
            m_socket.on(Socket.EVENT_CONNECT, onConnect);
            m_socket.on("serverMessage", onMessageReceived);
        }
        catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    // Socket서버에 connect 되면 발생하는 이벤트
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            m_socket.emit("clientMessage", "hi this is client");
        }
    };

    // 서버로부터 전달받은 'chat-message' Event 처리.
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            try {
                JSONObject receivedData = (JSONObject) args[0];
                Log.i(TAG, receivedData.getString("msg"));
                Log.i(TAG, receivedData.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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
