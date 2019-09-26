package com.alan.shiro.test.comm;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    public static String getNowTimeStr(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(cal.getTime());
        return date;

    }
}
