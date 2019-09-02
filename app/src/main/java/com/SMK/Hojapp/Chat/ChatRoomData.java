package com.SMK.Hojapp.Chat;

// 방에 대한 정보는 {대화 상대닉네임들, 가장 마지막 대화, 마지막 갱신 시간}
public class ChatRoomData {
    private String lastMsg = "";    // 마지막으로 주고 받은 대화
    private String members = "";    // 대화 상대닉네임들
    private long updateTime;        // 갱신 시간

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public String getMembers() {
        return members;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
