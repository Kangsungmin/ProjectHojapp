package com.SMK.Hojapp.Chat;

// 그룹 채팅을 위한 로직 (1:1 채팅 포함)
// 1. 채팅 방의 식별 번호가 존재한다.
// 2. 각 사용자는 내가 포함된 방번호 리스트를 DB에서 받아온다.
// 3. DB는 사용자 uid와 방식별자로 리스트를 관리한다.
// 4. 방에 대한 정보는 {대화 상대닉네임들, 가장 마지막 대화, 마지막 갱신 시간}으로 구성된다.

public class ChatData {
    private String roomID = "";
    private String msg = "";
    private String nickname = "";
    private long genTime = 0;

    public ChatData() {

    }

    public ChatData(String roomID, String msg, String nickname, long genTime) {
        this.roomID = roomID;
        this.msg = msg;
        this.nickname = nickname;
        this.genTime = genTime;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getGenTime() {
        return genTime;
    }

    public void setGenTime(long genTime) {
        this.genTime = genTime;
    }
}
