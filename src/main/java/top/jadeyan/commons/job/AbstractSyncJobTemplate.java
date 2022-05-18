package top.jadeyan.commons.job;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/**
 * 刷数据的模板方法
 *
 * @author yan
 * @date 2022/01/19
 */
public abstract class AbstractSyncJobTemplate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //获取内部时间接口
    //@Resource
    //protected InternalKeyValueService internalKeyValueService;

    /**
     * 增量同步数据(数据刷取完之后更新一次增量刻度)
     *
     * @param lastSyncIncrementMark        最后一次同步成功的刻度
     * @param pageSize                     每次查询的页数
     * @param searchDataFunction           查询的function
     * @param searchDataSortFieldFunction1 查询时的排序字段1
     * @param searchDataSortFieldFunction2 查询时的排序字段1
     * @param dealingDataFunction          处理数据的function
     * @param <R>                          查询的返回参数
     * @return 影响的行数
     */
    public <R> int syncIncrementTemplate(String lastSyncIncrementMark, Integer pageSize,
                                         BiFunction<Timestamp, Long, List<R>> searchDataFunction,
                                         Function<R, Timestamp> searchDataSortFieldFunction1,
                                         ToLongFunction<R> searchDataSortFieldFunction2, Function<List<R>, Integer> dealingDataFunction) {
        return this.syncIncrementTemplate(lastSyncIncrementMark, pageSize, searchDataFunction, searchDataSortFieldFunction1,
                searchDataSortFieldFunction2, dealingDataFunction, Boolean.FALSE);
    }


    /**
     * 增量同步数据
     *
     * @param lastSyncIncrementMark                  最后一次同步成功的刻度
     * @param pageSize                               每次查询的页数
     * @param searchDataFunction                     查询的function
     * @param searchDataSortFieldFunction1           查询时的排序字段1
     * @param searchDataSortFieldFunction2           查询时的排序字段1
     * @param dealingDataFunction                    处理数据的function
     * @param saveSyncIncrementMarkForEveryRecursive 每批数据保存的时候是否更新增量刻度
     * @param <R>                                    查询的返回参数
     * @return 影响的行数
     */
    public <R> int syncIncrementTemplate(String lastSyncIncrementMark, Integer pageSize,
                                         BiFunction<Timestamp, Long, List<R>> searchDataFunction,
                                         Function<R, Timestamp> searchDataSortFieldFunction1,
                                         ToLongFunction<R> searchDataSortFieldFunction2, Function<List<R>, Integer> dealingDataFunction,
                                         boolean saveSyncIncrementMarkForEveryRecursive) {
        int effectRows = 0;
        Timestamp lastUpdateTime = new Timestamp(0);
        long lastUpdateTimeMaxId = 0L;
        //获取更新时间
        //String lastUpdateTimeStr = internalKeyValueService.getValue(lastSyncIncrementMark);
        String lastUpdateTimeStr = "1642585506000";
        if (StringUtils.isBlank(lastUpdateTimeStr)) {
            InternalKeyValueRequestDTO internalKeyValueRequestDTO = new InternalKeyValueRequestDTO();
            internalKeyValueRequestDTO.setKey(lastSyncIncrementMark);
            internalKeyValueRequestDTO.setValue(String.valueOf(lastUpdateTime.getTime()));
            //更新时间
            //internalKeyValueService.saveKeyValue(internalKeyValueRequestDTO);
        } else {
            lastUpdateTime = new Timestamp(Long.valueOf(lastUpdateTimeStr));
        }
        Integer fetchCount = null;
        while (Objects.isNull(fetchCount) || pageSize.equals(fetchCount)) {
            logger.info("lastUpdateTime: {}, lastUpdateTimeMaxId: {}", lastUpdateTime, lastUpdateTimeMaxId);
            List<R> dataList = searchDataFunction.apply(lastUpdateTime, lastUpdateTimeMaxId);
            fetchCount = dataList.size();
            Timestamp currentMaxUpdateTime = dataList.stream().map(searchDataSortFieldFunction1).max(Timestamp::compareTo)
                    .orElse(lastUpdateTime);
            lastUpdateTime = currentMaxUpdateTime;
            lastUpdateTimeMaxId = dataList.stream().filter(x -> searchDataSortFieldFunction1.apply(x).compareTo(currentMaxUpdateTime) == 0)
                    .mapToLong(searchDataSortFieldFunction2).max().orElse(lastUpdateTimeMaxId);
            effectRows += dealingDataFunction.apply(dataList);
            if (Objects.equals(saveSyncIncrementMarkForEveryRecursive, Boolean.TRUE)) {
                InternalKeyValueRequestDTO internalKeyValueRequestDTO = new InternalKeyValueRequestDTO();
                internalKeyValueRequestDTO.setKey(lastSyncIncrementMark);
                internalKeyValueRequestDTO.setValue(String.valueOf(lastUpdateTime.getTime()));
                //更新时间
                //internalKeyValueService.saveKeyValue(internalKeyValueRequestDTO);
            }
        }
        InternalKeyValueRequestDTO internalKeyValueRequestDTO = new InternalKeyValueRequestDTO();
        internalKeyValueRequestDTO.setKey(lastSyncIncrementMark);
        internalKeyValueRequestDTO.setValue(String.valueOf(lastUpdateTime.getTime()));
        //更新时间
        //internalKeyValueService.saveKeyValue(internalKeyValueRequestDTO);
        return effectRows;
    }


    /**
     * 全量同步数据
     *
     * @param pageSize                    每次查询的页数
     * @param searchDataFunction          查询的function
     * @param searchDataSortFieldFunction 查询时的排序字段
     * @param dealingDataFunction         处理数据的function
     * @param <R>                         查询的返回参数
     * @return 影响的行数
     */
    public <R> int syncTemplate(Integer pageSize, Function<Long, List<R>> searchDataFunction,
                                ToLongFunction<R> searchDataSortFieldFunction, Function<List<R>, Integer> dealingDataFunction) {
        int effectRows = 0;
        long lastSyncMarkCode = 0L;
        Integer fetchCount = null;
        while (Objects.isNull(fetchCount) || pageSize.equals(fetchCount)) {
            logger.info("lastSyncMarkCode: {}", lastSyncMarkCode);
            List<R> dataList = searchDataFunction.apply(lastSyncMarkCode);
            fetchCount = dataList.size();
            lastSyncMarkCode = dataList.stream().mapToLong(searchDataSortFieldFunction).max().orElse(lastSyncMarkCode);
            effectRows += dealingDataFunction.apply(dataList);
        }
        return effectRows;
    }

}
