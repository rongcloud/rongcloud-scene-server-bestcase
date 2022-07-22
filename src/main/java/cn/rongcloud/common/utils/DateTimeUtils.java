package cn.rongcloud.common.utils;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by sunyinglong on 2020/6/25
 */
public class DateTimeUtils {
    private DateTimeUtils() {}
    public static Date currentUTC() {
        return new DateTime(DateTimeZone.UTC).toDate();
    }
    public static Date currentDt() {
        return new Date();
    }

    //获取当天的开始时间
    public static Date getDayBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

}
