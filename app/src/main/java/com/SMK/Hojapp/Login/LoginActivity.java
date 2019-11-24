package com.SMK.Hojapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.SMK.Hojapp.Contents.NewsFeedActivity;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 10;

    GlobalData globalData;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("LoginActivity","onCreate()");

        globalData = (GlobalData) getApplicationContext();

        findViewById(R.id.joinGoogleButton).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        // Initialize DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed:구글 로그인 실패", e);
                Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, "signInWithCredential:파이어베이스 아이디 생성 완료!");

                            FirebaseUser user = mAuth.getCurrentUser();
                            // [Set Global Data]
                            long nowTime = System.currentTimeMillis();

                            // UID 를 기준으로 DB에 가입 여부를 확인한다.
                            checkMember(user.getUid(), nowTime);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Log.w(TAG, "signInWithCredential:파이어베이스 생성 실패.");
                            //Snackbar.make(findViewById(R.id.), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }


    // 액티비티가 시작할 때.
    @Override
    protected  void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) { // 로그인 되어있다면.
            long nowTime = System.currentTimeMillis();
            Account newAccount = new Account(currentUser.getUid(), nowTime);
            globalData.setAccount(newAccount);

            startNewsFeedActivity();
        }
    }

    // 액티비티가 화면 상단으로 왔을 때.
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.joinGoogleButton) {
            SignIn();
        }
    }

    protected void checkMember(final String UID, final long time)
    {
        mDatabase.child("accounts").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() == true)
                {
                    // 이미 계정이 존재할 경우
                    startNewsFeedActivity();
                }
                else
                {
                    // 계정이 없을 경우 : 닉네임 생성 액티비티 실행
                    Account newAccount = new Account(UID, time);
                    globalData.setAccount(newAccount);
                    // [아이디 생성 액티비티 실행]
                    startJoinMemberActivity();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    protected void startNewsFeedActivity() {
        Intent i = new Intent(LoginActivity.this, NewsFeedActivity.class); startActivity(i);
        finish();
    }

    protected void startJoinMemberActivity() {
        Intent i = new Intent(LoginActivity.this, JoinMemberActivity.class); startActivity(i);
        finish();
    }
}
