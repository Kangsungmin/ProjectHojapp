package com.SMK.Hojapp.Contents;

import com.SMK.Hojapp.Login.Account;

import java.util.UUID;

public class Contents {
    public String cid;          // 콘텐츠 식별 ID
    public String parentCid;    // 댓글일 경우 : 부모 게시글 id
    public String title;
    public String body;
    public String wUid;         // 작성자 uid
    public String wName;        // 작성자 name
    public int hitCount;        // 조회수
    public int likeCount;       // 좋아요
    public int CommentCount;    // 댓글수
    public long createTime;     // 생성 시간
    public boolean isEdited;    // 수정 여부


    public Contents() {

    }

    public Contents(String title, String body, String writerUid, String writerName,long createTime) {
        this.cid = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.title = title;
        this.body = body;
        this.wUid = writerUid;
        this.wName = writerName;
        this.createTime = createTime;
    }
}
