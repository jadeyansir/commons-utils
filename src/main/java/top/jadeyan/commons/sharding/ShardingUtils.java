package top.jadeyan.commons.sharding;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 分片帮助工具类
 *
 * @author yan
 */
public final class ShardingUtils {

    private static final long DATE_PAGE_WIGHT = 1_000_000;
    private static final long YIELD_NET_PRICE_WIGHT = 1_000_000_000_000_000L;
    private static final Integer MONTHS_OF_YEAR = 12;
    private static final Integer YEAR_MONTH_WIGHT = 100;
    private static final long YIELD_PRICE_SUB_VALUE = 1000;


    private ShardingUtils() {
    }

    /**
     * 获取分页id
     *
     * @param issueTime 发布是时间
     * @param flowId    流水号
     * @return 分页id
     */
    public static long getPageId(java.util.Date issueTime, long flowId) {
        long time;
        if (Objects.isNull(issueTime)) {
            time = new Timestamp(0).getTime();
        } else {
            time = issueTime.getTime();
        }
        return time * DATE_PAGE_WIGHT + flowId;
    }

    /**
     * 获取收益率或净价的排序id
     *
     * @param issueTime    发布是时间
     * @param yieldOrPrice 收益率或净价
     * @return 排序id
     */
    public static long getYieldAndPricePk(java.util.Date issueTime, BigDecimal yieldOrPrice) {
        long time;
        if (Objects.isNull(issueTime)) {
            time = new Timestamp(0).getTime() / YIELD_PRICE_SUB_VALUE;
        } else {
            time = issueTime.getTime() / YIELD_PRICE_SUB_VALUE;
        }
        long wightId = yieldOrPrice.multiply(new BigDecimal(YIELD_NET_PRICE_WIGHT)).longValue();
        return wightId + time;
    }

    /**
     * 利用pageid 还原 flowid
     *
     * @param pageId 分页id
     * @return 流水id
     */
    public static long getFlowId(Long pageId) {
        if (Objects.isNull(pageId)) {
            return 0L;
        }

        Date issueDate = ShardingUtils.getPkDate(pageId);
        // 还原之前的流水号
        return pageId - ShardingUtils.getPkValue(issueDate);
    }

    /**
     * 获取自定义分页id
     * 一般我们只针对当天的,不要在历史里面使用
     * 比如我们用在收益率上面排序, 权重为 1000000000000
     *
     * @param value  值
     * @param weight 权重
     * @param flowId 流水id
     * @return 分页id
     */
    public static Optional<Long> getCustomPageId(BigDecimal value, Long weight, Long flowId) {
        if (Objects.isNull(value) || Objects.isNull(weight) || Objects.isNull(flowId)) {
            return Optional.empty();
        }
        long longValue = BigDecimal.valueOf(weight).multiply(value).longValue();
        long result;
        if (longValue >= 0) {
            result = longValue + flowId;
        } else {
            result = longValue - flowId;
        }
        return Optional.of(result);
    }

    /**
     * 获取自定义分页id
     * 一般我们只针对当天的,不要在历史里面使用
     * 比如我们用在收益率上面排序, 权重为 1000000000000
     *
     * @param value  值
     * @param weight 权重
     * @param flowId 流水id
     * @return 分页id
     */
    public static Optional<Long> getCustomPageId(Integer value, Long weight, Long flowId) {
        if (Objects.isNull(value) || Objects.isNull(weight) || Objects.isNull(flowId)) {
            return Optional.empty();
        }
        return getCustomPageId(BigDecimal.valueOf(value), weight, flowId);
    }

    /**
     * 获取自定义分页id
     * 一般我们只针对当天的,不要在历史里面使用
     * 比如我们用在收益率上面排序, 权重为 1000000000000
     *
     * @param value  值
     * @param weight 权重
     * @param flowId 流水id
     * @return 分页id
     */
    public static Optional<Long> getCustomPageId(Long value, Long weight, Long flowId) {
        if (Objects.isNull(value) || Objects.isNull(weight) || Objects.isNull(flowId)) {
            return Optional.empty();
        }
        return getCustomPageId(BigDecimal.valueOf(value), weight, flowId);
    }

    /**
     * 获取分片主键日期 （主键是根据时间生成的）
     *
     * @param pk 主键
     * @return 分片主键时间
     */
    public static Date getPkDate(Long pk) {
        return new Date(pk / DATE_PAGE_WIGHT);
    }

    /**
     * 获取某天最小PK
     *
     * @param date 日期
     * @return 最小PK
     */
    public static Long getMinPkOfDate(java.util.Date date) {
        java.util.Date todayMin = DateUtils.truncate(date, Calendar.DATE);
        return getPageId(todayMin, 0);
    }

    /**
     * 获取某天最大PK
     *
     * @param date 日期
     * @return 最大PK
     */
    public static Long getMaxPkOfDate(java.util.Date date) {
        java.util.Date todayMin = DateUtils.truncate(date, Calendar.DATE);
        java.util.Date tomorrow = DateUtils.addDays(todayMin, 1);
        return getPageId(tomorrow, 0) - 1;
    }

    /**
     * 获取这一秒的最小PK
     *
     * @param date 日期
     * @return 获取这一秒最小pk
     */
    public static Long getMinPkOfSecond(java.util.Date date) {
        return getPageId(date, 0);
    }

    /**
     * 获取这一秒最大pk
     *
     * @param date 日期
     * @return 获取这一秒最大pk
     */
    public static Long getMaxPkOfSecond(java.util.Date date) {
        java.util.Date nextSecond = DateUtils.addSeconds(date, 1);
        return getPageId(nextSecond, 0) - 1;
    }

    /**
     * 获取分片值
     *
     * @param date 日期
     * @return 分片值
     */
    public static Long getPkValue(java.util.Date date) {
        if (date == null) {
            return 0L;
        }

        return date.getTime() * DATE_PAGE_WIGHT;
    }

    /// region month sharding

    /**
     * 判断两个日期是否是同年同月
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 两个日期是否是同年同月
     */
    public static boolean isSameYearMonth(java.util.Date date1, java.util.Date date2) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            return false;
        }
        LocalDate useDate1 = new Date(date1.getTime()).toLocalDate();
        LocalDate useDate2 = new Date(date2.getTime()).toLocalDate();
        return useDate1.getYear() == useDate2.getYear() && useDate1.getMonthValue() == useDate2.getMonthValue();
    }

    /**
     * 获取pk 同月最大值
     *
     * @param pk 主键
     * @return 最大同月的pk
     */
    public static Long getCurrentMonthMaxPk(Long pk) {
        Date currentPkDate = getPkDate(pk);
        LocalDate currentPkLocalDate = currentPkDate.toLocalDate();
        LocalDate endPkLocalDate = currentPkLocalDate.with(TemporalAdjusters.firstDayOfNextMonth());
        long endDateValue = Date.valueOf(endPkLocalDate).getTime();
        return endDateValue * DATE_PAGE_WIGHT - 1;
    }

    /**
     * 获取pk 下月最大值
     *
     * @param pk 主键
     * @return 最小下个月pk
     */
    public static Long getNextMonthMinPk(Long pk) {
        Date currentPkDate = getPkDate(pk);
        LocalDate currentPkLocalDate = currentPkDate.toLocalDate();
        LocalDate firstDayOfNextMonth = currentPkLocalDate.with(TemporalAdjusters.firstDayOfNextMonth());
        long firstDayOfNextMonthValue = Date.valueOf(firstDayOfNextMonth).getTime();
        return firstDayOfNextMonthValue * DATE_PAGE_WIGHT;
    }

    /**
     * 获取按月分片表名
     *
     * @param logicTableName 逻辑表名
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @return 分片表
     */
    public static List<String> getMonthShardingTableNames(String logicTableName, Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();
        int startYear = startLocalDate.getYear();
        int startMonth = startLocalDate.getMonthValue();
        int endYear = endLocalDate.getYear();
        int endMonth = endLocalDate.getMonthValue();
        List<String> result = new ArrayList<>();
        int currentYear = startYear;
        int currentMonth = startMonth;
        int endYearMonthValue = getYearMonthValue(endYear, endMonth);
        while (getYearMonthValue(currentYear, currentMonth) <= endYearMonthValue) {
            result.add(String.format("%s_%d_%02d", logicTableName, currentYear, currentMonth));
            if (currentMonth == MONTHS_OF_YEAR) {
                currentMonth = 1;
                currentYear++;
            } else {
                currentMonth++;
            }
        }
        return result;
    }

    /**
     * 获取按日分片表名
     *
     * @param logicTableName 逻辑表名
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @return 分片表
     */
    public static List<String> getDayShardingTableNames(String logicTableName, java.util.Date startDate, java.util.Date endDate) {
        List<String> result = new ArrayList<>();
        LocalDate endLocalDate = new Date(endDate.getTime()).toLocalDate();
        LocalDate useStartLocalDate = new Date(startDate.getTime()).toLocalDate();
        while (useStartLocalDate.compareTo(endLocalDate) <= 0) {
            String actualTableName = String.format("%s_%d_%02d_%02d",
                    logicTableName,
                    useStartLocalDate.getYear(),
                    useStartLocalDate.getMonthValue(),
                    useStartLocalDate.getDayOfMonth());
            result.add(actualTableName);
            useStartLocalDate = useStartLocalDate.plusDays(1);
        }
        return result;
    }

    /**
     * 是否已经是最大的月份表
     *
     * @param startPk 开始分片主键
     * @return 是否已经是最大的月份表
     */
    public static Boolean isMaxMonth(Long startPk) {
        Date currentDate = getPkDate(startPk);
        Date maxDate = new Date(System.currentTimeMillis());
        LocalDate currentLocalDate = currentDate.toLocalDate();
        LocalDate maxLocalDate = maxDate.toLocalDate();
        int currentDateValue = getYearMonthValue(currentLocalDate.getYear(), currentLocalDate.getMonthValue());
        int maxDateValue = getYearMonthValue(maxLocalDate.getYear(), maxLocalDate.getMonthValue());
        return currentDateValue > maxDateValue;
    }

    /// endregion

    /// region year sharding

    /**
     * 判断两个日期是否是同一年
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 两个日期是否是同一年
     */
    public static boolean isSameYear(java.util.Date date1, java.util.Date date2) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            return false;
        }
        LocalDate useDate1 = new Date(date1.getTime()).toLocalDate();
        LocalDate useDate2 = new Date(date2.getTime()).toLocalDate();
        return useDate1.getYear() == useDate2.getYear();
    }

    /**
     * 获取pk 同年最大值
     *
     * @param pk 主键
     * @return 最大同月的pk
     */
    public static Long getCurrentYearMaxPk(Long pk) {
        Date currentPkDate = getPkDate(pk);
        LocalDate currentPkLocalDate = currentPkDate.toLocalDate();
        LocalDate endPkLocalDate = currentPkLocalDate.with(TemporalAdjusters.firstDayOfNextYear());
        long endDateValue = Date.valueOf(endPkLocalDate).getTime();
        return endDateValue * DATE_PAGE_WIGHT - 1;
    }

    /**
     * 获取pk 下月最大值
     *
     * @param pk 主键
     * @return 最小下个月pk
     */
    public static Long getNextYearMinPk(Long pk) {
        Date currentPkDate = getPkDate(pk);
        LocalDate currentPkLocalDate = currentPkDate.toLocalDate();
        LocalDate firstDayOfNextYear = currentPkLocalDate.with(TemporalAdjusters.firstDayOfNextYear());
        long firstDayOfNextYearValue = Date.valueOf(firstDayOfNextYear).getTime();
        return firstDayOfNextYearValue * DATE_PAGE_WIGHT;
    }

    /**
     * 获取按年分片表名
     *
     * @param logicTableName 逻辑表名
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @return 分片表
     */
    public static Collection<String> getYearShardingTableNames(String logicTableName, Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();
        int startYear = startLocalDate.getYear();
        int endYear = endLocalDate.getYear();
        int currentYear = startYear;
        List<String> result = new ArrayList<>();
        while (currentYear <= endYear) {
            result.add(String.format("%s_%d", logicTableName, currentYear));
            currentYear++;
        }
        return result;
    }

    /**
     * 是否已经是最大的年份表
     *
     * @param startPk 开始分片主键
     * @return 是否已经是最大的年份表
     */
    public static Boolean isMaxYear(Long startPk) {
        Date currentDate = getPkDate(startPk);
        Date maxDate = new Date(System.currentTimeMillis());
        LocalDate currentLocalDate = currentDate.toLocalDate();
        LocalDate maxLocalDate = maxDate.toLocalDate();
        int currentDateValue = currentLocalDate.getYear();
        int maxDateValue = maxLocalDate.getYear();
        return currentDateValue > maxDateValue;
    }

    /// endregion

    /**
     * get value of date format(yyyyMM)
     *
     * @param year  年
     * @param month 月
     * @return 年月时间
     */
    private static int getYearMonthValue(int year, int month) {
        return year * YEAR_MONTH_WIGHT + month;
    }

    /**
     * 获取pk 同月最小值
     *
     * @param currentDate 当前时间
     * @return 同月最小值pk
     */
    public static Long getCurrentMonthMinPk(Date currentDate) {
        LocalDate currentPkLocalDate = currentDate.toLocalDate();
        LocalDate endPkLocalDate = currentPkLocalDate.with(TemporalAdjusters.firstDayOfMonth());
        long endDateValue = Date.valueOf(endPkLocalDate).getTime();
        return endDateValue * DATE_PAGE_WIGHT;
    }

    /**
     * 获取上一月
     *
     * @param currentDate 当前时间
     * @return 上月时间
     */
    public static Date getLastMonth(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);
        java.util.Date utilDate = calendar.getTime();
        return new Date(utilDate.getTime());
    }

    /**
     * 比较开始pk和当月最小的pk大小
     *
     * @param currentDate 当前时间
     * @param startPk     开始pk
     * @return 最大值pk
     */
    public static Long getMaxPk(Date currentDate, long startPk) {
        long lastMonthMinPk = getCurrentMonthMinPk(currentDate);
        return Math.max(lastMonthMinPk, startPk);
    }
}
