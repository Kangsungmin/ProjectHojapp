package com.SMK.Hojapp.Basic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.LoginActivity;
import com.SMK.Hojapp.R;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        globalData = (GlobalData) getApplicationContext();
        // 시드니 지역 정보 로드
        Resources res = getApplicationContext().getResources();
        String[] locationArr = res.getStringArray(R.array.Region);
        globalData.setSydneyCityArr(locationArr);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class); startActivity(i);
                finish();
            } }, 3000);

    }

    public static void restartApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }
}
