package com.SMK.Hojapp.Chat;

import java.util.ArrayList;
import java.util.UUID;

// 방에 대한 정보는 {대화 상대닉네임들, 가장 마지막 대화, 마지막 갱신 시간}
public class ChatRoomData {
    private String lastMsg = "";    // 마지막으로 주고 받은 대화
    private ArrayList<Member> members = new ArrayList<>();    // 대화 상대닉네임들
    private  String roomID = "";
    private long updateTime;        // 갱신 시간

    public ChatRoomData() {

    }

    public ChatRoomData(String lastMsg, ArrayList<Member> members, long updateTime) {
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

    public void addMember(Member member) {
        if(member.uid.equals("") == false){ // uid가 존재할 때
            this.members.add(member);
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

    public ArrayList<Member> getMembers() {
        return members;
    }

    public String getMembersName() { // 채팅방 멤버들의 이름을 나열하여 문자열 리턴
        String rtn = "";
        for(Member tmp : members){
            rtn += tmp.name;
        }
        return rtn;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
