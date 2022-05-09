package top.jadeyan.commons.sharding;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author yan
 **/
public class UidGeneratorUtilsTest {

    @Test
    public void testGetTimestamp() throws ParseException {
        long uid = 6287122008631205888L;
        // {"UID":"6287122008631205888","timestamp":"2022-03-07 19:35:33","workerId":"7","sequence":"0"}
        final Timestamp timestamp = UidGeneratorUtils.getTimestamp(uid);
        System.out.println(timestamp);

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        final Date parse = sdf.parse("2022-03-07 19:35:33");
        assertEquals(parse.getTime(), timestamp.getTime());
    }

    @Test
    public void testGetMaxDateUid() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        final Date date1 = sdf.parse("2022-03-07 19:35:31");
        final Date date2 = sdf.parse("2022-03-07 19:35:32");

        final long uid1 = UidGeneratorUtils.getMaxDateUid(date1);
        final long uid2 = UidGeneratorUtils.getMaxDateUid(date2);
        // 因为是当天最大 所以天数一样，时候数据是一样的
        assertEquals(uid1, uid2);

        final Timestamp timestamp = UidGeneratorUtils.getTimestamp(uid1);
        assertTrue(DateUtils.isSameDay(date1, timestamp));
    }

    @Test
    public void testGetMaxDateUid2() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        final Date date1 = sdf.parse("2022-03-07 19:35:31");
        final Date date2 = sdf.parse("2022-03-08 19:35:32");

        final long uid1 = UidGeneratorUtils.getMaxDateUid(date1);
        final long uid2 = UidGeneratorUtils.getMaxDateUid(date2);

        assertTrue(uid2 > uid1);
    }

    @Test
    public void testGetMinUid() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        final Date date1 = sdf.parse("2022-03-07 19:35:31");
        final Date date2 = sdf.parse("2022-03-07 19:35:32");

        final long uid1 = UidGeneratorUtils.getMinDateUid(date1);
        final long uid2 = UidGeneratorUtils.getMinDateUid(date2);

        // 因为是当天最小 所以天数一样，时候数据是一样的
        assertEquals(uid1, uid2);

        final Timestamp timestamp = UidGeneratorUtils.getTimestamp(uid1);
        assertTrue(DateUtils.isSameDay(date1, timestamp));
    }
}
