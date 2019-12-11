package com.SMK.Hojapp.Profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.SMK.Hojapp.Basic.SplashActivity;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private GlobalData globalData;
    ImageView logoutImage, backImage , profileEditImage;
    TextView profileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        globalData = (GlobalData) getApplicationContext();

        profileName = findViewById(R.id.profileName);
        profileName.setText("닉네임  " + globalData.getAccount().getName()); // 닉네임 표시

        logoutImage = findViewById(R.id.logoutImage);
        logoutImage.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SplashActivity.restartApp(getApplicationContext());
            }
        });

        backImage = findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileEditImage = findViewById(R.id.profileEditImage);
        profileEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 프로필 수정 액티비티를 실행한다.
            }
        });
    }
}
