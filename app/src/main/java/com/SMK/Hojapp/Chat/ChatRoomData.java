package com.SMK.Hojapp.Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// 방에 대한 정보는 {대화 상대닉네임들, 가장 마지막 대화, 마지막 갱신 시간}
public class ChatRoomData {
    private String lastMsg = "";    // 마지막으로 주고 받은 대화
    private Map<String, Member> members;    // 대화 상대닉네임들(KEY : UID)
    private  String roomID = "";
    private long updateTime;        // 갱신 시간

    public ChatRoomData() {

    }

    public ChatRoomData(String lastMsg, long updateTime) {
        roomID = UUID.randomUUID().toString(); // 랜덤 식별 ID 생성
        this.lastMsg = lastMsg;
        this.members = new HashMap<String, Member>();
        this.updateTime = updateTime;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void addMember(Member member) {
        if(member.uid.equals("") == false){ // uid가 존재할 때
            this.members.put(member.uid, member);
        }
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

    public Map<String, Member> getMembers() {
        return members;
    }

    public String getMembersName() { // 채팅방 멤버들의 이름을 나열하여 문자열 리턴
        String rtn = "";
        // Map 순회

        for(Map.Entry<String, Member> tmp : members.entrySet()){
            rtn += tmp.getValue().name;
        }
        return rtn;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
