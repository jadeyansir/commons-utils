package top.jadeyan.commons.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.util.Collection;

/**
 * 月分片表范围分片逻辑
 *
 * @author yan
 * @date 2023/1/10
 **/
public class MonthTableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        String tableName = rangeShardingValue.getLogicTableName();
        Range<Long> idRange = rangeShardingValue.getValueRange();
        boolean hasLowerBound = idRange.hasLowerBound();
        boolean hasUpperBound = idRange.hasUpperBound();
        Long minPk = hasLowerBound ? idRange.lowerEndpoint() : null;
        Long maxPk = hasUpperBound ? idRange.upperEndpoint() : null;
        return getTables(tableName, minPk, maxPk);
    }


    private Collection<String> getTables(String tableName, Long minPk, Long maxPk) {
        if (minPk == null) {
            throw new InvalidParameterException("minPk cannot be empty!");
        }

        Date startDate = ShardingUtils.getPkDate(minPk);
        Date endDate = maxPk == null ? new Date(System.currentTimeMillis()) : ShardingUtils.getPkDate(maxPk);
        return ShardingUtils.getMonthShardingTableNames(tableName, startDate, endDate);
    }
}
