package top.jadeyan.commons.object;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    /**
     * 计算两个日期之间有多少个闰年月份，即2.29出现的次数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 出现次数
     */
    private int countLeapYearMonths(LocalDate startDate, LocalDate endDate) {
        int count = 0;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate) || currentDate.equals(endDate)) {
            if (currentDate.isLeapYear() && currentDate.getMonthValue() == 2) {
                count++;
            }
            currentDate = currentDate.plusMonths(1);
        }
        return count;
    }

    /**
     * 获取日期所属的周一
     *
     * @param date 日期
     * @return 获取日期所属周一
     */
    public static Date getMondayOfDate(@NotNull Date date) {
        LocalDate localDate = date.toLocalDate();
        return Date.valueOf(localDate.with(DayOfWeek.MONDAY));
    }

    /**
     * 根据日期获取月份数据 格式 yyyyMM
     *
     * @param date 日期
     * @return 年+月 yyyyMM 如：202201 代表 202201 月
     */
    public static Integer getMonthNumOfDate(@NotNull Date date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_MONTH_NUM);
        return Integer.valueOf(date.toLocalDate().format(dateTimeFormatter));
    }

    /**
     * 一个季度的月跨度
     */
    private static final Integer QUARTER_MONTH_SPAN = 3;

    /**
     * 年季度组合年的权重
     */
    private static final Integer QUARTER_YEAR_WEIGHT = 100;

    /**
     * 根据日期获取季度数据 格式 yyyy * 100 + [季度： 1: 1季度;2: 2季度；3:3 季度；4: 4季度]
     * 示例： 2022 年 1 季度， 2 季度， 3 季度， 4 季度分别表示为： 202201， 202202，202203,202204
     *
     * @param date 日期
     * @return yyyy * 100 + 季度
     */
    public static Integer getQuarterNumOfDate(@NotNull Date date) {
        LocalDate localDate = date.toLocalDate();
        int month = (localDate.getMonthValue() - 1) / QUARTER_MONTH_SPAN + 1;
        return localDate.getYear() * QUARTER_YEAR_WEIGHT + month;
    }

    /**
     * 获取日期的年
     *
     * @param date 日期
     * @return 当前日期所属年
     */
    public static Integer getYearOfDate(@NotNull Date date) {
        return date.toLocalDate().getYear();
    }


    /**
     * 获取当天剩余的毫秒数
     *
     * @return Millis
     */
    public static long getTodayRemainingMillis() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.MILLIS.between(LocalDateTime.now(), midnight);
    }

    /**
     * 获取当天剩余的纳秒数
     *
     * @return Nanos
     */
    public static long getTodayRemainingNanos() {
        return TimeUnit.MILLISECONDS.toNanos(DateExtensionUtils.getTodayRemainingMillis());
    }

}
