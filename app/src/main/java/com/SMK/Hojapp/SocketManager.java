package com.SMK.Hojapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SocketManager extends Application {
    private static final SocketManager instance = new SocketManager();
    private static Context context = null;

    private ServiceConnection connection = new ServiceConnection() {
        //onServiceConnected callback fun
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            Log.i("SocketManager", "onServiceConnected()/name : " + name);
            binder = IConnectionService.Stub.asInterface(service);
            instance.SetBinder(binder);
            isBinded = true;

            // LoginActivity 실행
            Intent intent = new Intent(SocketManager.this, LoginActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        //onServiceDisconnected callback fun
        @Override
        public void onServiceDisconnected(ComponentName name){
            Log.i("SocketManager", "onServiceDisconnected()");
            isBinded = false;
        }
    };
    private IConnectionService binder = null;
    public boolean isBinded  = false;

    public SocketManager(){
        Log.i("SocketManager","SocketManager()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SocketManager", "OnCreate()");

        //get context
        context = getApplicationContext();

        // bind service
        Intent intent = new Intent(context, ConnectionService.class); // Start ConnectionService
        context.bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public static SocketManager getInstance(){
        return instance;
    }

    public void SetBinder(IConnectionService binder){
        this.binder = binder;
    }

    int GetStatus() throws RemoteException {
        return binder.GetStatus();
    }

    void SetSocket(String ip, int port) throws RemoteException {
        if(binder != null) {
            binder.SetSocket(ip, port);
        }
    }

    void Connect() throws RemoteException {
        if(binder != null) {
            binder.Connect();
        }
    }

    void Disconnect() throws RemoteException {
        binder.Disconnect();
    }
}
