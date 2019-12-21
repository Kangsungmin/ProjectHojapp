package com.SMK.Hojapp.Contents;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.*;
import com.SMK.Hojapp.Contents.ContentsTypes.Contents;
import com.SMK.Hojapp.Contents.ContentsTypes.ViewType;
import com.SMK.Hojapp.GlobalData;
import com.SMK.Hojapp.Login.Account;
import com.SMK.Hojapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class WriteContentsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private DatabaseReference mDatabase;
    private Button contentsCategoryButton;
    private TextInputEditText contentsTitleInput;
    private TextInputEditText contentsBodyInput;
    private GlobalData globalData;
    private ProgressBar progressBar;
    private ImageView imageView;

    private Uri imageUri;

    private StorageReference storageRef;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_contents);

        globalData = (GlobalData) getApplicationContext();

        findViewById(R.id.completeButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.imageUploadButton).setOnClickListener(this);

        contentsCategoryButton = (Button) findViewById(R.id.categoryButton);

        contentsTitleInput = (TextInputEditText) findViewById(R.id.contentsTitle);
        contentsBodyInput = (TextInputEditText) findViewById(R.id.contentsBody);
        imageView = findViewById(R.id.imageView);

        storageRef = FirebaseStorage.getInstance().getReference("upload");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void onClick(View view) {
        //writeNewContents("testID", "inputTitle", "inputBody");
        //카테고리, 작성자 정보, 제목, 내용, 시간 등 업로드
        int i = view.getId();
        if (i == R.id.completeButton) {
            if(globalData != null) {
                if(contentsCategoryButton.getText().toString().equals("카테고리"))
                {
                    // 카테고리를 선택하지 않았을 때
                    Toast.makeText(this, "카테고리를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(contentsTitleInput.getText().toString().equals(""))
                {
                    // 제목이 아무 것도 없을 때
                    Toast.makeText(this, "제목을 작성해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(contentsBodyInput.getText().toString().equals(""))
                {
                    // 내용이 아무 것도 없을 때
                    Toast.makeText(this, "내용을 작성해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                writeNewContents(contentsCategoryButton.getText().toString(), globalData.getAccount(), contentsTitleInput.getText().toString(), contentsBodyInput.getText().toString());
                uploadFile();
            }
        }
        else if(i == R.id.cancelButton) {
            finish();
        }
        //이미지 업로드
        else if(i == R.id.imageUploadButton){
            openFileChooser();
        }
    }

    private void writeNewContents(String categoryName, Account user, String title, String body) {
        long nowTime = System.currentTimeMillis();
        Contents contents = new Contents(ViewType.ROW_CONTENTS_DETAIL, categoryName, title, body, user.getUid(), user.getName(), nowTime);
        mDatabase.child("contents").child(contents.getCid()).setValue(contents);
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
        contentsCategoryButton.setText(menuItem.getTitle());
        return false;
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(imageUri != null) {
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtention(imageUri)); // 파일 업로드 명 : 현재시간.확장자

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { // 성공 콜백
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(WriteContentsActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                            /* 파일업로드 된 객체를 DB에 저장한다.
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);
                            */
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() { // 실패 콜백
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(WriteContentsActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(this, "파일을 선택해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
