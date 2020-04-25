package com.SMK.Hojapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.SMK.Hojapp.Login.Account;

import java.util.ArrayList;

// 전역 데이터 관리 클래스
public class GlobalData extends Application {
    private Account nowAccount;
    private String[] sydneyCityArr;
    public GlobalData()
    {

    }
    //TODO : XML에서 읽어들인 카테고리 정보에 대한 리스트를 저장한다. 이를 기반으로 Contents 의 카테고리를 초기화한다.

    public void setAccount(Account account) {
        nowAccount = account;
    }

    public Account getAccount() {
        return nowAccount;
    }

    public String[] getSydneyCityList() {return sydneyCityArr; }

    public void setSydneyCityArr(String[] arr) {sydneyCityArr = arr;}

    public String getSydneyRegion(int i) {return sydneyCityArr[i];}

    public void setAccountNick(String nick)
    {
        nowAccount.setName(nick);
    }
}
