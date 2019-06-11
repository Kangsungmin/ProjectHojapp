package com.SMK.Hojapp.Contents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.SMK.Hojapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteContentsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_contents);

        findViewById(R.id.completeButton).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        writeNewContents("testID", "inputTitle", "inputBody");
    }

    private void writeNewContents(String userId, String title, String body) {
        Contents contents = new Contents(title, body);
        mDatabase.child("contents").child(userId).setValue(contents);
        finish();
    }
}
