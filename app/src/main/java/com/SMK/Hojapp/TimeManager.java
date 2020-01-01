package com.SMK.Hojapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeManager {

    public static String getFormedDate(long rawDate) {
        //currentTimeMillis를 월/일 포맷으로 변환
        //TODO:1년 이내는 년도 표시 하지 않음. 7일 이내는 n일 전으로 표시.

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date resultDate = new Date(rawDate);
        return sdf.format(resultDate);
    }

    public static String getFormedMinute(long rawDate) {
        // 오늘일 경우 시간 : 분 표시. 오늘이 아닌 경우 날짜까지만 표시

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date resultDate = new Date(rawDate);
        return sdf.format(resultDate);
    }
}
