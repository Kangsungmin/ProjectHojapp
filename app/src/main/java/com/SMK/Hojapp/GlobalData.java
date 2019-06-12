package com.SMK.Hojapp;

import android.app.Application;
import com.SMK.Hojapp.Login.Account;

public class GlobalData extends Application {
    private Account nowAccount;

    public void setAccount(Account account) {
        nowAccount = account;
    }

    public Account getAccount() {
        return nowAccount;
    }
}
