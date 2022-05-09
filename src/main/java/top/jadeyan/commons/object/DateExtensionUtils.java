package top.jadeyan.commons.object;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * 日期扩展工具
 *
 * @author yan
 */
public final class DateExtensionUtils {

    private DateExtensionUtils() {
        // hide constructor
    }

    /**
     * format yyyyMMddHHmmss
     */
    public static final String FORMAT_TIMESTAMP_NUM = "yyyyMMddHHmmss";
    /**
     * format yyyyMMddHHmmssSSS
     */
    public static final String FORMAT_TIMESTAMP_NUMS = "yyyyMMddHHmmssSSS";
    /**
     * format yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_TIMESTAMP_SEC = "yyyy-MM-dd HH:mm:ss";
    /**
     * format yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String FORMAT_TIMESTAMP_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * format yyyy/MM/dd HH:mm:ss
     */
    public static final String FORMAT_TIMESTAMP_YMD = "yyyy/MM/dd HH:mm:ss";
    /**
     * format yyyy/MM/dd HH:mm:ss.SSS
     */
    public static final String FORMAT_TIMESTAMP_YMS = "yyyy/MM/dd HH:mm:ss.SSS";
    /**
     * format yyyyMMdd
     */
    public static final String FORMAT_DATE_NUM = "yyyyMMdd";
    /**
     * format yyyy-MM-dd
     */
    public static final String FORMAT_DATE_SIMPLE = "yyyy-MM-dd";
    /**
     * format yyyy/MM/dd
     */
    public static final String FORMAT_DATE_SLASH = "yyyy/MM/dd";
    /**
     * format yyyyMM
     */
    public static final String FORMAT_MONTH_NUM = "yyyyMM";
    /**
     * format yyyy-MM
     */
    public static final String FORMAT_MONTH_SIMPLE = "yyyy-MM";
    /**
     * format yyyy/MM
     */
    public static final String FORMAT_MONTH_SLASH = "yyyy/MM";
    /**
     * format HH:mm:ss
     */
    public static final String FORMAT_HOUR_COLON = "HH:mm:ss";

    private static final String[] COMMON_DATE_FORMAT = new String[]{
            FORMAT_TIMESTAMP_NUMS,
            FORMAT_TIMESTAMP_NUM,

            FORMAT_TIMESTAMP_SSS,
            FORMAT_TIMESTAMP_SEC,

            FORMAT_TIMESTAMP_YMS,
            FORMAT_TIMESTAMP_YMD,

            FORMAT_DATE_NUM,
            FORMAT_DATE_SIMPLE,
            FORMAT_DATE_SLASH,

            FORMAT_MONTH_NUM,
            FORMAT_MONTH_SIMPLE,
            FORMAT_MONTH_SLASH,

            FORMAT_HOUR_COLON
    };

    /**
     * 转化日期
     *
     * @param dateStr 时间字符串
     * @return 日期
     * @throws ParseException 转化异常
     */
    public static Date parseDate(String dateStr) throws ParseException {
        java.util.Date date = parseDateInternal(dateStr);
        return new Date(DateUtils.truncate(date, Calendar.DATE).getTime());
    }

    /**
     * 转化日期带时间
     *
     * @param dateStr 时间字符串
     * @return 日期带时间
     * @throws ParseException 转化异常
     */
    public static Timestamp parseTimestamp(String dateStr) throws ParseException {
        java.util.Date date = parseDateInternal(dateStr);
        return new Timestamp(date.getTime());
    }

    /**
     * 格式化时间
     *
     * @param date    时间
     * @param pattern 格式
     * @return 格式化后字符串
     */
    public static String format(java.util.Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    private static java.util.Date parseDateInternal(String dateStr) throws ParseException {
        return DateUtils.parseDate(dateStr, COMMON_DATE_FORMAT);
    }

    /**
     * 两个时间相减后相差的天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个时间相减结果
     */
    public static long getDiffDays(Date startDate, Date endDate) {
        long diff = DateUtils.truncate(endDate, Calendar.DATE).getTime() - DateUtils.truncate(startDate, Calendar.DATE).getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取两个日期之间的日期列表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期之间的列表
     */
    public static List<Date> listRangeDate(Date startDate, Date endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Collections.emptyList();
        }
        List<Date> dateList = Lists.newArrayListWithExpectedSize((int) getDiffDays(startDate, endDate));
        for (LocalDate localDate = startDate.toLocalDate(); localDate.compareTo(endDate.toLocalDate()) <= 0;
             localDate = localDate.plusDays(1)) {
            dateList.add(Date.valueOf(localDate));
        }
        return dateList;
    }

    /**
     * 两个时间相减后相差的天数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 两个时间相减结果
     */
    public static long getDiffDays(Timestamp startTime, Timestamp endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * 是否是同一天
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 是否是同一天
     */
    public static boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * 是否是同一月
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 是否是同一月
     */
    public static boolean isSameMonth(java.util.Date date1, java.util.Date date2) {
        final LocalDate localDate1 = new Date(date1.getTime()).toLocalDate();
        final LocalDate localDate2 = new Date(date2.getTime()).toLocalDate();
        return localDate1.getMonthValue() == localDate2.getMonthValue();
    }

    /**
     * 是否是同一年
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 是否是同一年
     */
    public static boolean isSameYear(java.util.Date date1, java.util.Date date2) {
        final LocalDate localDate1 = new Date(date1.getTime()).toLocalDate();
        final LocalDate localDate2 = new Date(date2.getTime()).toLocalDate();
        return localDate1.getYear() == localDate2.getYear();
    }

    /**
     * 日期加上指定的天数
     *
     * @param date    日期
     * @param addDays 加的天数
     * @return 加上天数后的日期
     */
    public static Date addDays(Date date, int addDays) {
        return new Date(DateUtils.addDays(date, addDays).getTime());
    }

    /**
     * 日期加上指定的天数
     *
     * @param date    日期
     * @param addDays 加的天数
     * @return 加上天数后的日期 和时间
     */
    public static Timestamp addDays(Timestamp date, int addDays) {
        return new Timestamp(DateUtils.addDays(date, addDays).getTime());
    }

}
