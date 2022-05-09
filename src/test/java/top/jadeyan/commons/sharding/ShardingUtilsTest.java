package top.jadeyan.commons.sharding;

import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("all")
public class ShardingUtilsTest {

    @Test
    public void testSameDaySharding() {
        LocalDate date1 = LocalDate.of(2020, 1, 1);
        LocalDate date2 = LocalDate.of(2020, 1, 1);
        String logicTableName = "test";
        String expectedActualTableName = "test_2020_01_01";

        List<String> results = ShardingUtils.getDayShardingTableNames(logicTableName, Date.valueOf(date1), Date.valueOf(date2));
        assertEquals(expectedActualTableName, results.get(0));
    }

    @Test
    public void testRangeDaySharding() {
        LocalDate date1 = LocalDate.of(2020, 1, 20);
        LocalDate date2 = LocalDate.of(2020, 2, 5);
        String logicTableName = "test";

        List<String> results = ShardingUtils.getDayShardingTableNames(logicTableName, Date.valueOf(date1), Date.valueOf(date2));
        assertEquals("test_2020_01_20", results.get(0));
        assertEquals("test_2020_02_05", results.get(results.size() - 1));
    }

    @Test
    public void testGetMinPkOfSecond() {
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 1, 1, 1);
        final Timestamp timestamp = Timestamp.valueOf(time);
        final Long minPkOfSecond = ShardingUtils.getMinPkOfSecond(timestamp);
        assertEquals(ShardingUtils.getPkValue(timestamp), minPkOfSecond);
    }

    @Test
    public void testGetMaxPkOfSecond() {
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 1, 1, 1);
        final Timestamp timestamp = Timestamp.valueOf(time);
        final long maxPkOfSecond = ShardingUtils.getMaxPkOfSecond(timestamp);

        final LocalDateTime addedTime = time.plusSeconds(1);
        final Timestamp timestamp1 = Timestamp.valueOf(addedTime);
        assertEquals(ShardingUtils.getPkValue(timestamp1) - 1, maxPkOfSecond);
    }
}
