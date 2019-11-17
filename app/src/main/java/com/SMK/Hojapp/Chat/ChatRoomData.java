package com.SMK.Hojapp.Chat;

import java.util.ArrayList;
import java.util.UUID;

// 방에 대한 정보는 {대화 상대닉네임들, 가장 마지막 대화, 마지막 갱신 시간}
public class ChatRoomData {
    private String lastMsg = "";    // 마지막으로 주고 받은 대화
    private ArrayList<String> members = new ArrayList<>();    // 대화 상대닉네임들
    private  String roomID = "";
    private long updateTime;        // 갱신 시간

    public ChatRoomData() {

    }

    public ChatRoomData(String lastMsg, ArrayList<String> members, long updateTime) {
        roomID = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.lastMsg = lastMsg;
        this.members = members;
        this.updateTime = updateTime;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void addMember(String member) {
        this.members.add(member);
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
