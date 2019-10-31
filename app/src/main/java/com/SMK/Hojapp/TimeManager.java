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
}
