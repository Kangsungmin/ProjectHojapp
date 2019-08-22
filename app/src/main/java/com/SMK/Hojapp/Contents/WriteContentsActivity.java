package com.SMK.Hojapp.Contents;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.Account;
import com.SMK.Hojapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteContentsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private DatabaseReference mDatabase;
    private Button contentsCategoryButton;
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

        contentsCategoryButton = (Button) findViewById(R.id.cancelButton);
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
                writeNewContents(contentsCategoryButton.getText().toString(), globalData.getAccount(), contentsTitleInput.getText().toString(), contentsBodyInput.getText().toString());
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

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) { // 팝업메뉴의 아이템을 클릭 시.
        switch (menuItem.getItemId()){
            case R.id.item1 :
                Toast.makeText(this, "Item 1 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2 :
                Toast.makeText(this, "Item 2 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3 :
                Toast.makeText(this, "Item 3 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item4 :
                Toast.makeText(this, "Item 4 clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
