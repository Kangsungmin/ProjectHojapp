package com.SMK.Hojapp.Contents;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.Account;
import com.SMK.Hojapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.w3c.dom.Text;

public class WriteContentsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private TextInputEditText contentsCategoryInput;
    private TextInputEditText contentsTitleInput;
    private TextInputEditText contentsBodyInput;
    private GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_contents);

        globalData = (GlobalData) getApplicationContext();

        findViewById(R.id.completeButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);

        contentsCategoryInput = (TextInputEditText) findViewById(R.id.categoryInput);
        contentsTitleInput = (TextInputEditText) findViewById(R.id.contentsTitle);
        contentsBodyInput = (TextInputEditText) findViewById(R.id.contentsBody);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        //writeNewContents("testID", "inputTitle", "inputBody");
        //카테고리, 작성자 정보, 제목, 내용, 시간 등 업로드
        int i = view.getId();
        if (i == R.id.completeButton) {
            if(globalData != null) {
                writeNewContents(contentsCategoryInput.getText().toString(), globalData.getAccount(), contentsTitleInput.getText().toString(), contentsBodyInput.getText().toString());
            }
        }
        else if(i == R.id.cancelButton) {
            finish();
        }
    }

    private void writeNewContents(String categoryName, Account user, String title, String body) {
        long nowTime = System.currentTimeMillis();
        Contents contents = new Contents(categoryName, title, body, user.uid, user.name, nowTime);
        mDatabase.child("contents").child(contents.cid).setValue(contents);
        // TODO : 작성완료 팝업 출력
        finish();
    }
}
