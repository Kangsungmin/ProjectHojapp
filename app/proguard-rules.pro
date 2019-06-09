package com.SMK.Hojapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectionService extends Service {
    // 소켓의 상태를 표현하는 상수
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;
    // 소켓 소멸 대기시간 (5초)
    final int TIME_OUT = 5000;

    private int status = STATUS_DISCONNECTED;
    private Socket socket = null;
    private SocketAddress socketAddress = null;
    
    private int port = 3000;

    IConnectionService.Stub binder = new IConne