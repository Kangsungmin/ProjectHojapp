package com.SMK.Hojapp.Login;
/*
 * 사용자의 계정정보를 의미하는 Class입니다.
 *
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Account {
    private String uid;
    private String name;
    private long lastLoginTime;
    private Map<String, String> roomDataMap; // 자신이 참가하고있는 채팅방을 Set으로 관리한다.
    private String location;
    private Random rnd;

    public Account()
    {

    }

    public Account(String uid, String nickName,long lastLoginTime) {
        rnd = new Random();

        this.uid = uid;
        this.name = nickName;
        this.lastLoginTime = lastLoginTime;
        this.roomDataMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    private String makeRandomName()
    {
        StringBuffer temp = new StringBuffer();

        // 12자리의 무작위 문자열을 생성한다.
        for(int i= 0; i < 12; i++)
        {
            int rIndex = rnd.nextInt(3);
            switch (rIndex)
            {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUid(){
        return uid;
    }

    public String getLocation() { return location; }

    public void setLocation(String data) { location = data; }

    public void clearChatRoomMap() {
        this.roomDataMap.clear();
    }

    public void addChatRoom(String roomID) {
        roomDataMap.put(roomID,"");
    }

    public void removeChatRoom(String roomID) {
        roomDataMap.remove(roomID);
    }

    public Map<String,String> getRoomDataMap() {
        return this.roomDataMap;
    }

    public Boolean isMyChatRoom(String roomID) {
        return this.roomDataMap.containsKey(roomID);
    }
}
