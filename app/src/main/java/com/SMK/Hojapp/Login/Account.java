package com.SMK.Hojapp.Login;
/*
 * 사용자의 계정정보를 의미하는 Class입니다.
 *
 */
public class Account {
    private String uid;
    private String name;
    private long lastLoginTime;

    public Account(String uid, long lastLoginTime) {
        this.uid = uid;
        this.lastLoginTime = lastLoginTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUid(){
        return uid;
    }
}
