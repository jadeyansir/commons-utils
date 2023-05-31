package top.jadeyan.commons.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Date;

/**
 * 月份表精确分片逻辑
 *
 * @author yan
 * @date 2023/1/10
 */
public class DateMonthTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> preciseShardingValue) {
        final Date value = preciseShardingValue.getValue();
        String tableName = preciseShardingValue.getLogicTableName();
        return String.format("%s_%tY_%tm", tableName, value, value);
    }
}
