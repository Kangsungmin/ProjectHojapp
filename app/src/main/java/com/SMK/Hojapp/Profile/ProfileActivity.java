package com.SMK.Hojapp.Profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.SMK.Hojapp.Basic.SplashActivity;
import com.SMK.Hojapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    ImageView logoutImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutImage = findViewById(R.id.logoutImage);
        logoutImage.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SplashActivity.restartApp(getApplicationContext());
            }
        });
    }
}
