package com.SMK.Hojapp.Contents;

import java.util.UUID;

public class Contents {
    String category;     // 카테고리
    String cid;          // 콘텐츠 식별 ID
    String parentCid;    // 댓글일 경우 : 부모 게시글 id
    String title;
    String body;
    int bodyPic;
    String wUid;         // 작성자 uid
    String wName;        // 작성자 name
    int hitCount;        // 조회수
    int likeCount;       // 좋아요
    int commentCount;    // 댓글수
    long createTime;     // 생성 시간
    boolean isEdited;    // 수정 여부


    public Contents() {

    }

    public Contents(String category, String title, String body, String writerUid, String writerName,long createTime) {
        this.category = category;
        this.cid = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.title = title;
        this.body = body;
        this.wUid = writerUid;
        this.wName = writerName;
        this.createTime = createTime;
    }



    // getter setter 단축키는 Alt + Ins
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentCid() {
        return parentCid;
    }

    public void setParentCid(String parentCid) {
        this.parentCid = parentCid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getBodyPic() {
        return bodyPic;
    }

    public void setBodyPic(int bodyPic) {
        this.bodyPic = bodyPic;
    }

    public String getwUid() {
        return wUid;
    }

    public void setwUid(String wUid) {
        this.wUid = wUid;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
