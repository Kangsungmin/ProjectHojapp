// IConnectionService.aidl
package com.SMK.Hojapp;

// Declare any non-default types here with import statements

interface IConnectionService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int GetStatus();
    void SetSocket(String ip, int port);
    void Connect();
    void Disconnect();
}
