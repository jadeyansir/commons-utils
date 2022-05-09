package top.jadeyan.commons.object;

import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.*;

public class DateExtensionUtilsTest {

    @Test
    public void testParseDate1() throws ParseException {
        Date date = DateExtensionUtils.parseDate("2010-01-01");
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        assertEquals(Date.valueOf(localDate).getTime(), date.getTime());
    }

    @Test
    public void testParseDate2() throws ParseException {
        Date date = DateExtensionUtils.parseDate("2010-1-1");
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        assertEquals(Date.valueOf(localDate).getTime(), date.getTime());
    }

    @Test
    public void testParseDate3() throws ParseException {
        // 这里我们可以去掉尾巴上的 小时分钟
        Date date = DateExtensionUtils.parseDate("2010-1-1 1:1:1");
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        assertEquals(Date.valueOf(localDate).getTime(), date.getTime());
    }

    @Test
    public void testParseDate4() throws ParseException {
        // 这里我们可以去掉尾巴上的 小时分钟
        Date date = DateExtensionUtils.parseDate("2010-1-1 1:1:1.111");
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        assertEquals(Date.valueOf(localDate).getTime(), date.getTime());
    }

    @Test
    public void testParseTimestamp() throws ParseException {
        Timestamp date = DateExtensionUtils.parseTimestamp("2010-1-1 1:1:1");
        LocalDateTime localDateTime = LocalDateTime.of(2010, 1, 1, 1, 1, 1);
        assertEquals(Timestamp.valueOf(localDateTime).getTime(), date.getTime());
    }

    @Test
    public void testFormat1() throws ParseException {
        Timestamp date = DateExtensionUtils.parseTimestamp("2010-1-1 1:1:1.111");
        String result = DateExtensionUtils.format(date, DateExtensionUtils.FORMAT_DATE_SIMPLE);
        assertEquals("2010-01-01", result);
    }

    @Test
    public void testFormat2() throws ParseException {
        Timestamp date = DateExtensionUtils.parseTimestamp("2010-1-1 1:1:1.111");
        String result = DateExtensionUtils.format(date, DateExtensionUtils.FORMAT_TIMESTAMP_SEC);
        assertEquals("2010-01-01 01:01:01", result);
    }

    @Test
    public void testFormat3() throws ParseException {
        Date date = DateExtensionUtils.parseDate("2010-1-1 1:1:1.111");
        String result = DateExtensionUtils.format(date, DateExtensionUtils.FORMAT_TIMESTAMP_SEC);
        assertEquals("2010-01-01 00:00:00", result);
    }

    @Test
    public void testFormat4() throws ParseException {
        Timestamp timestamp = DateExtensionUtils.parseTimestamp("20210308094330000");
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        assertEquals(2021, localDateTime.getYear());
        assertEquals(3, localDateTime.getMonthValue());
        assertEquals(8, localDateTime.getDayOfMonth());
        assertEquals(9, localDateTime.getHour());
        assertEquals(43, localDateTime.getMinute());
        assertEquals(30, localDateTime.getSecond());
    }

    @Test
    public void testFormat5() throws ParseException {
        Timestamp timestamp = DateExtensionUtils.parseTimestamp("20210308094330");
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        assertEquals(2021, localDateTime.getYear());
        assertEquals(3, localDateTime.getMonthValue());
        assertEquals(8, localDateTime.getDayOfMonth());
        assertEquals(9, localDateTime.getHour());
        assertEquals(43, localDateTime.getMinute());
        assertEquals(30, localDateTime.getSecond());
    }

    @Test
    public void testFormat6() throws ParseException {
        Timestamp timestamp = DateExtensionUtils.parseTimestamp("20210308");
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        assertEquals(2021, localDateTime.getYear());
        assertEquals(3, localDateTime.getMonthValue());
        assertEquals(8, localDateTime.getDayOfMonth());
    }

    @Test
    public void testFormat7() throws ParseException {
        Timestamp timestamp = DateExtensionUtils.parseTimestamp("202103");
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        assertEquals(2021, localDateTime.getYear());
        assertEquals(3, localDateTime.getMonthValue());
    }

    @Test
    public void testGetDifferenceDaysForDate() throws ParseException {
        Date timestamp1 = DateExtensionUtils.parseDate("2010-01-05");
        Date timestamp2 = DateExtensionUtils.parseDate("2010-01-01");
        long diffDays = DateExtensionUtils.getDiffDays(timestamp1, timestamp2);
        assertEquals(-4, diffDays);
    }

    @Test
    public void testGetDifferenceDaysForTimestamp() throws ParseException {
        Timestamp timestamp1 = DateExtensionUtils.parseTimestamp("2010-01-05 00:00:00");
        Timestamp timestamp2 = DateExtensionUtils.parseTimestamp("2010-01-01 01:00:00");
        long diffDays = DateExtensionUtils.getDiffDays(timestamp1, timestamp2);
        assertEquals(-3, diffDays);
    }

    @Test
    public void testIsSameDate() throws ParseException {
        Timestamp timestamp1 = DateExtensionUtils.parseTimestamp("2010-01-01");
        Timestamp timestamp2 = DateExtensionUtils.parseTimestamp("2010-01-01");
        Timestamp timestamp3 = DateExtensionUtils.parseTimestamp("2010-02-01");
        final boolean sameDate1 = DateExtensionUtils.isSameDay(timestamp1, timestamp2);
        final boolean sameDate2 = DateExtensionUtils.isSameDay(timestamp1, timestamp3);
        assertTrue(sameDate1);
        assertFalse(sameDate2);
    }

    @Test
    public void testIsSameMonth() throws ParseException {
        Timestamp timestamp1 = DateExtensionUtils.parseTimestamp("2010-01-05");
        Timestamp timestamp2 = DateExtensionUtils.parseTimestamp("2010-01-01");
        Timestamp timestamp3 = DateExtensionUtils.parseTimestamp("2010-02-01");
        final boolean sameMonth1 = DateExtensionUtils.isSameMonth(timestamp1, timestamp2);
        final boolean sameMonth2 = DateExtensionUtils.isSameMonth(timestamp1, timestamp3);
        assertTrue(sameMonth1);
        assertFalse(sameMonth2);
    }

    @Test
    public void testIsSameYear() throws ParseException {
        Timestamp timestamp1 = DateExtensionUtils.parseTimestamp("2010-01-05");
        Timestamp timestamp2 = DateExtensionUtils.parseTimestamp("2010-01-01");
        Timestamp timestamp3 = DateExtensionUtils.parseTimestamp("2011-01-01");
        final boolean sameYear1 = DateExtensionUtils.isSameYear(timestamp1, timestamp2);
        final boolean sameYear2 = DateExtensionUtils.isSameYear(timestamp1, timestamp3);
        assertTrue(sameYear1);
        assertFalse(sameYear2);
    }

    @Test
    public void testAddDays() throws ParseException {
        Date date = DateExtensionUtils.parseDate("2010-01-01");
        Date dateAddOne = DateExtensionUtils.addDays(date, 1);
        LocalDate localDate = LocalDate.of(2010, 1, 2);
        assertEquals(Date.valueOf(localDate).getTime(), dateAddOne.getTime());
    }

    @Test
    public void testAddDays2() throws ParseException {
        Timestamp date = DateExtensionUtils.parseTimestamp("2010-1-1 1:1:1.111");
        Timestamp dateAddOne = DateExtensionUtils.addDays(date, 1);
        String result = DateExtensionUtils.format(dateAddOne, DateExtensionUtils.FORMAT_TIMESTAMP_SEC);
        assertEquals("2010-01-02 01:01:01", result);
    }

    @Test
    public void testHourFormat() throws ParseException {
        Timestamp date = DateExtensionUtils.parseTimestamp("2010-1-1 1:1:1.111");
        String result = DateExtensionUtils.format(date, DateExtensionUtils.FORMAT_HOUR_COLON);
        assertEquals("01:01:01", result);
    }

    @Test
    public void testListRangeDate() throws ParseException {
        Date startDate = DateExtensionUtils.parseDate("2022-03-01");
        Date endDate = DateExtensionUtils.parseDate("2022-03-10");
        List<Date> dates = DateExtensionUtils.listRangeDate(startDate, endDate);
        assertEquals(dates.get(0), DateExtensionUtils.parseDate("2022-03-01"));
        assertEquals(dates.get(1), DateExtensionUtils.parseDate("2022-03-02"));
        assertEquals(dates.get(9), DateExtensionUtils.parseDate("2022-03-10"));
        assertEquals(dates.size(), 10);
    }

}
