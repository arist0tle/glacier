package alpha.common.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author tanghaiyang on 2017/12/15.
 */
public class DateUtils {

    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String LOCAL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Timestamp getTimeStamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getDaysBefore(new Date(), 1));
    }

    public static Date getYearsBefore(Date d, int years) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) - years);
        return now.getTime();
    }

    public static Date getDaysBefore(Date d, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return now.getTime();
    }

    public static Date getHoursBefore(Date d, int hours) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) - hours);
        return now.getTime();
    }

    public static String getTodayAndHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        return sdf.format(new Date());
    }

    public static String getTodayAndHourBefore() {
        Date date = getHoursBefore(new Date(), 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        return sdf.format(date);
    }

    public static String formatLocal(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOCAL_FORMAT);
        return sdf.format(millis);
    }

    public static String formatLocal(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOCAL_FORMAT);
        return sdf.format(date);
    }

    public static String toUTC(String time) {
        return toUTC(toLocal(time));
    }

    public static String toUTC(Date date) {
        return DateFormatUtils.format(date, ISO_8601_FORMAT);
    }

    public static String utc2Local(String utcTime) {
        return utc2Local(utcTime, ISO_8601_FORMAT, LOCAL_FORMAT);
    }

    public static String utc2Local(String utcTime, String utcPatten, String localPatten) {
        Date utcDate = utc2Local(utcTime, utcPatten);
        if (utcDate == null) {
            return utcTime;
        }
        SimpleDateFormat local = new SimpleDateFormat(localPatten);
        local.setTimeZone(TimeZone.getDefault());
        String localTime = local.format(utcDate.getTime());
        return localTime;
    }

    public static Date utc2Local(String utcTime, String utcPatten) {
        SimpleDateFormat utc = new SimpleDateFormat(utcPatten);
        utc.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = utc.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDate;
    }

    /**
     * Get local time millis(length=10).
     *
     * @param time
     * @return
     */
    public static long toLocalMillis(String time) {
        return toLocal(time).getTime() / 1000;
    }

    public static Date toLocal(String time) {
        if (StringUtils.isBlank(time)) {
            return new Date();
        }

        // UTC
        if (time.contains("T")) {
            return utc2Local(time, ISO_8601_FORMAT);
        }

        String format = "";
        time = time.replaceAll("[-|:| ]", "");
        if (time.length() == 8) {
            format = "yyyyMMdd";
        } else if (time.length() == 14) {
            format = "yyyyMMddHHmmss";
        }

        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static boolean isDate(String time) {
        if (StringUtils.isBlank(time)) {
            return false;
        }
        // UTC
        if (time.contains("T")) {
            Date date = utc2Local(time, ISO_8601_FORMAT);
            if (date == null) {
                return false;
            }
        }
        String format = "";
        time = time.replaceAll("[-|:| ]", "");
        if (time.length() == 8) {
            format = "yyyyMMdd";
        } else if (time.length() == 14) {
            format = "yyyyMMddHHmmss";
        }

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ParseException {
/*        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date local = df.parse("2014-08-23T09:20:05Z");
        System.out.println(DateUtils.formatLocal(local));

        local = df.parse("2010-03-05T16:00:00Z");
        System.out.println(DateUtils.formatLocal(local));

        System.out.println(DateUtils.toUTC(new Date()));
        local = DateUtils.utc2Local("2010-03-05T16:05:01.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(DateUtils.formatLocal(local));*/
        System.out.println(DateUtils.utc2Local("2018-05-01T10:00:03.000Z",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", LOCAL_FORMAT));
        System.out.println(DateUtils.utc2Local("2018-05-01T10:00:03Z"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println(sdf.format(new Date()));
        System.out.println(toUTC(getDaysBefore(new Date(), 3)));
        System.out.println(toUTC(getYearsBefore(new Date(), 3)));
        System.out.println(StringUtils.substringBefore(toUTC(new Date()), ":"));
        System.out.println(getYesterday());
    }

}
