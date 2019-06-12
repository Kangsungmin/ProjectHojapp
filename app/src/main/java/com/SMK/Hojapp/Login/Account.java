package com.SMK.Hojapp.Login;
/*
 * 사용자의 계정정보를 의미하는 Class입니다.
 *
 */
public class Account {
    public String uid;
    public String name;
    public long lastLoginTime;

    public Account(String uid, long lastLoginTime) {
        this.uid = uid;
        this.lastLoginTime = lastLoginTime;
    }
}
