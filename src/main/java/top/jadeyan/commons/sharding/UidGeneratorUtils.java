package top.jadeyan.commons.sharding;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Baidu uid 工具
 *
 * @author yan
 **/
public final class UidGeneratorUtils {

    private static final long EPOCH_SECONDS = TimeUnit.MILLISECONDS.toSeconds(1_463_673_600_000L);
    private static final int WORKER_BITS = 22;
    private static final int SEQ_BITS = 13;

    private UidGeneratorUtils() {
        // hide constructor
    }

    /**
     * 获取时间戳
     *
     * @param uid uid
     * @return 获取时间戳
     */
    public static Timestamp getTimestamp(long uid) {
        long deltaSeconds = uid >>> (WORKER_BITS + SEQ_BITS);
        return new Timestamp(TimeUnit.SECONDS.toMillis(EPOCH_SECONDS + deltaSeconds));
    }

    /**
     * 获取时间最大的uid
     *
     * @param date 时间
     * @return 时间最大的uid
     */
    public static long getMaxDateUid(java.util.Date date) {
        final java.util.Date currentDate = DateUtils.truncate(date, Calendar.DATE);
        final java.util.Date tomorrow = DateUtils.addDays(currentDate, 1);
        long maxTime = tomorrow.getTime() - 1;
        final long deltaSeconds = TimeUnit.MILLISECONDS.toSeconds(maxTime) - EPOCH_SECONDS;
        return deltaSeconds << (WORKER_BITS + SEQ_BITS);
    }

    /**
     * 获取时间最小的uid
     *
     * @param date 时间
     * @return 时间最小的uid
     */
    public static long getMinDateUid(java.util.Date date) {
        final java.util.Date currentDate = DateUtils.truncate(date, Calendar.DATE);
        final long deltaSeconds = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime()) - EPOCH_SECONDS;
        return deltaSeconds << (WORKER_BITS + SEQ_BITS);
    }
}
