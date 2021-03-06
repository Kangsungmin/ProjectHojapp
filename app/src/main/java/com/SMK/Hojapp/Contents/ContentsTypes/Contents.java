package com.SMK.Hojapp.Contents.ContentsTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Contents {
    private int viewType;
    String category;     // 카테고리
    String cid;          // 콘텐츠 식별 ID
    String parentCid;    // 댓글일 경우 : 부모 게시글 id
    String state;
    String title;
    String subTitle;
    String titleUrl;
    String profileUrl;
    String body;
    ArrayList<String> bodyPicUrlList;
    String wUid;         // 작성자 uid
    String wName;        // 작성자 name
    int hitCount;        // 조회수
    // 좋아요를 누를 유저 id 리스트
    Map<String, Integer> locationHash; // 위치 해시태그
    Map<String, Long> likeUserMap; // 유저 식별자 : 키 , 좋아요 누른 시간 : 값
    // 댓글이 달린 댓글(게시글) id 리스트
    ArrayList<Contents> commentList;
    long createTime;     // 생성 시간
    boolean isEdited;    // 수정 여부

    public Contents() {
        this.locationHash = new HashMap<String, Integer>();
        this.likeUserMap = new HashMap<String, Long>();
        this.commentList = new ArrayList<Contents>();
    }

    public Contents(int vType, String category, String title, String body, String writerUid, String writerName, long createTime) {
        this.viewType = vType;
        this.category = category;
        this.cid = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.parentCid = "0"; // 댓글이 아닐 경우
        this.title = title;
        this.body = body;
        this.wUid = writerUid;
        this.wName = writerName;
        this.createTime = createTime;
        this.locationHash = new HashMap<>();
        this.likeUserMap = new HashMap<>();
        this.commentList = new ArrayList<>();
        this.bodyPicUrlList = new ArrayList<>();
    }

    public Contents(int vType, String parentCid, String body, String writerUid, String writerName, long createTime) {
        this.viewType = vType;
        this.cid = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.parentCid = parentCid; // 댓글 부모 게시글 식별 아이디
        this.body = body;
        this.wUid = writerUid;
        this.wName = writerName;
        this.createTime = createTime;
        this.locationHash = new HashMap<>();
        this.likeUserMap = new HashMap<>();
        this.commentList = new ArrayList<>();
        this.bodyPicUrlList = new ArrayList<>();
    }


    //-----------------------------------------------------------------------------------------
    // getter setter 단축키는 Alt + Ins
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String title) {
        this.subTitle = title;
    }

    public String getTitleUrl(){ return titleUrl; }

    public void setTitleUrl(String url){ this.titleUrl = url; }

    public String getProfileUrl(){return profileUrl;}

    public void setProfileUrl(String url){this.profileUrl = url;}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getBodyPicUrlSize() {
        if(bodyPicUrlList == null)
        {
            return 0;
        }
        else return bodyPicUrlList.size();
    }

    public String getBodyPicUrl(int index)
    {
        return bodyPicUrlList.get(index);
    }

    public void addBodyPicUrl(String url) {
        this.bodyPicUrlList.add(url);
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

    public void insertLikeUser(String user) {
        this.likeUserMap.put(user, System.currentTimeMillis());
    }

    public void deleteLikeUser(String user) {
        this.likeUserMap.remove(user);
    }

    //해당 유저가 맵에 존재하는지 확인
    public Boolean isInLikeUserMap(String user) {
        if(this.likeUserMap != null) {
            return this.likeUserMap.containsKey(user);
        }
        else return false;
    }

    public Map<String, Integer> getLocationHash() { return  this.locationHash; }

    public Map<String, Long> getLikeUserMap() {
        return this.likeUserMap;
    }

    public int getLikeUserCount() {
        if(this.likeUserMap != null)
            return this.likeUserMap.size();
        else return 0;
    }

    public void insertCommentList(Contents comment) {
        this.commentList.add(comment);
    }

    public ArrayList<Contents> getCommentList() {
        return this.commentList;
    }

    public int getCommentsCount() {
        if(this.commentList != null)
            return this.commentList.size();
        else return 0;
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


