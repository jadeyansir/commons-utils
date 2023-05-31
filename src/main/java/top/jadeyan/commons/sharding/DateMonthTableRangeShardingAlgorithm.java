package top.jadeyan.commons.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Date;

/**
 * 月分片表范围分片逻辑
 *
 * @author yan
 * @date 2023/1/10
 **/
public class DateMonthTableRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> rangeShardingValue) {
        String tableName = rangeShardingValue.getLogicTableName();
        Range<Date> date = rangeShardingValue.getValueRange();
        boolean hasLowerBound = date.hasLowerBound();
        boolean hasUpperBound = date.hasUpperBound();
        Date minDate = hasLowerBound ? date.lowerEndpoint() : null;
        Date maxDate = hasUpperBound ? date.upperEndpoint() : null;
        return getTables(tableName, minDate, maxDate);
    }


    private Collection<String> getTables(String tableName, Date minDate, Date maxDate) {
        if (minDate == null) {
            throw new InvalidParameterException("minDate cannot be empty!");
        }
        Date endDate = maxDate == null ? new Date(System.currentTimeMillis()) : maxDate;
        return ShardingUtils.getMonthShardingTableNames(
                tableName, new java.sql.Date(minDate.getTime()), new java.sql.Date(endDate.getTime()));
    }
}
