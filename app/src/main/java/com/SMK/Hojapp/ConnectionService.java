package com.SMK.Hojapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ConnectionService extends Service {
    final String TAG = "ConnectionService";
    // 소켓의 상태를 표현하는 상수
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;
    // 소켓 소멸 대기시간 (5초)
    final int TIME_OUT = 5000;

    private int status = STATUS_DISCONNECTED;
    private static Socket socket = null;
    private static int Test = 0;
    //private SocketAddress socketAddress = null;
    private final String IP = "http://192.168.234.49";
    private final int PORT = 3000;

    IConnectionService.Stub binder = new IConnectionService.Stub() {
        @Override
        public int GetStatus() throws RemoteException {
            return status;
        }

        @Override
        public void SetSocket(String ip, int port) throws RemoteException {
            MySetSocket(ip, port);
        }


        @Override
        public void Connect() throws RemoteException {
            MyConnect();
        }

        @Override
        public void Disconnect() throws RemoteException {
            MyDisconnect();
        }
    };

    public ConnectionService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ConnectionService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        Log.i("ConnectionService","OnStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("ConnectionService", "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.i("ConnectionService", "onBind()");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        Log.i("ConnectionService", "onUnbind()");
        return super.onUnbind(intent);
    }

    void MySetSocket(String ip, int port) {
        Log.i("ConnectionService", "MySetSocket");
        try {
            socket = IO.socket(ip + ":" + port);
            Test++;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /*
    try{
        m_socket = IO.socket(IP + ":" + PORT);
        m_socket.connect();
        m_socket.on(Socket.EVENT_CONNECT, onConnect);
        m_socket.on("serverMessage", onMessageReceived);
    }
    catch (URISyntaxException e){
        e.printStackTrace();
    }
    */
    void MyConnect() {
        Log.i("ConnectionService", "myConnect()..start");
        //socket = new Socket();
        if(socket == null) return;

        new Thread(new Runnable(){
            @Override
            public void run() {
                socket.connect();
                socket.on(Socket.EVENT_CONNECT, OnConnect);
                socket.on("serverMessage", OnMessageReceived);
                Log.i("ConnectionService", "myConnect()..connected");
            }
        }).start();
    }

    void MyDisconnect() {
        socket.close();
        status = STATUS_DISCONNECTED;
    }

    private Emitter.Listener OnConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            status = STATUS_CONNECTED;
            socket.emit("clientMessage", "This is client [ " + socket.id() + " ]");
        }
    };

    private Emitter.Listener OnMessageReceived = new Emitter.Listener() {
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
}
