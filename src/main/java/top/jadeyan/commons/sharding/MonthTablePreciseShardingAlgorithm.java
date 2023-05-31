package top.jadeyan.commons.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.sql.Date;
import java.util.Collection;

/**
 * 月份表精确分片逻辑
 *
 * @author yan
 * @date 2023/1/10
 */
public class MonthTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        Long pk = preciseShardingValue.getValue();
        String tableName = preciseShardingValue.getLogicTableName();
        Date pkDate = ShardingUtils.getPkDate(pk);
        return String.format("%s_%tY_%tm", tableName, pkDate, pkDate);
    }
}
