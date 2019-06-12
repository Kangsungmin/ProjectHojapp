package com.SMK.Hojapp.Contents;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.SMK.Hojapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.w3c.dom.Text;

public class WriteContentsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private TextInputEditText contentsTitleInput;
    private TextInputEditText contentsBodyInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_contents);

        findViewById(R.id.completeButton).setOnClickListener(this);
        contentsTitleInput = (TextInputEditText) findViewById(R.id.contentsTitle);
        contentsBodyInput = (TextInputEditText) findViewById(R.id.contentsBody);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        //writeNewContents("testID", "inputTitle", "inputBody");
        //카테고리, 작성자 정보, 제목, 내용, 시간 등 업로드
    }

    private void writeNewContents(String userId, String title, String body) {
        Contents contents = new Contents(title, body);
        mDatabase.child("contents").child(userId).setValue(contents);
        finish();
    }
}
