package top.jadeyan.commons.job;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;
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
     * 分页增量同步数据（通过分页处理，避免重复操作，避免数据丢失）
     *
     * @param lastSyncIncrementMark                  最后一次同步成功的刻度
     * @param pageSize                               每次查询的页数
     * @param searchDataFunction                     查询的function 参数：定时任务增量参数 返回值：分页数据
     * @param searchUpdateTimeFieldFunction          获取时间字段值的函数
     * @param searchIdFieldFunction                  获取增量id字段值的函数
     * @param dealingDataFunction                    处理数据的function
     * @param saveSyncIncrementMarkForEveryRecursive 每批数据保存的时候是否更新增量刻度
     * @param <R>                                    查询的返回参数
     * @return 影响的行数
     */
    public <R> int syncIncrementTemplateByPage(String lastSyncIncrementMark, Integer pageSize,
                                               Function<SyncIncrementTemplateParamBO, PageInfo<R>> searchDataFunction,
                                               Function<R, Timestamp> searchUpdateTimeFieldFunction,
                                               ToLongFunction<R> searchIdFieldFunction, ToIntFunction<List<R>> dealingDataFunction,
                                               boolean saveSyncIncrementMarkForEveryRecursive) {
        int effectRows = 0;
        Timestamp lastUpdateTime = new Timestamp(0);
        long lastUpdateTimeMaxId = 0L;
        long lastUpdateTimeSize = 0L;
        //String lastUpdateTimeStr = internalKeyValueService.getValue(lastSyncIncrementMark);
        String lastUpdateTimeStr = "";
        // 处理库里面的值
        if (StringUtils.isBlank(lastUpdateTimeStr)) {
            saveKeyAndValue(lastSyncIncrementMark, lastUpdateTime, lastUpdateTimeSize);
        } else {
            String[] updateInfo = lastUpdateTimeStr.split("-");
            lastUpdateTime = new Timestamp(Long.parseLong(Optional.of(updateInfo).filter(x -> x.length > 0)
                    .map(x -> updateInfo[0]).orElse(lastUpdateTimeStr)));
            lastUpdateTimeSize = Optional.of(updateInfo).filter(x -> x.length > 1).map(x -> x[1])
                    .filter(StringUtils::isNotBlank).map(Long::parseLong).orElse(0L);
        }
        // 最新时间的总条数
        PageInfo<R> dataPage = searchDataFunction.apply(SyncIncrementTemplateParamBO.build(lastUpdateTime,
                false, lastUpdateTimeMaxId, true, 1));
        long lastUpdateSize = dataPage.getTotal();
        Timestamp selectLastUpdateTime = Optional.ofNullable(dataPage.getList()).filter(CollectionUtils::isNotEmpty).map(x -> x.get(0))
                .map(searchUpdateTimeFieldFunction).orElse(lastUpdateTime);
        // 如果最新时间的数据不变不进行再次处理
        if (Objects.equals(lastUpdateTimeSize, lastUpdateSize) && Objects.equals(lastUpdateTime, selectLastUpdateTime)) {
            logger.info("{} 最后一次更新：{} 数据为：{} 没有变化，不进行操作", lastSyncIncrementMark, lastUpdateTime, lastUpdateTimeSize);
            return 0;
        }
        lastUpdateTimeSize = 0L;
        Integer fetchCount = null;
        while (Objects.isNull(fetchCount) || pageSize.equals(fetchCount)) {
            logger.info("key:{}, lastUpdateTime: {}, lastUpdateTimeMaxId: {}", lastSyncIncrementMark, lastUpdateTime, lastUpdateTimeMaxId);
            List<R> dataList = searchDataFunction.apply(SyncIncrementTemplateParamBO.build(lastUpdateTime,
                    true, lastUpdateTimeMaxId, false, pageSize)).getList();
            fetchCount = dataList.size();
            Timestamp currentMaxUpdateTime = dataList.stream().map(searchUpdateTimeFieldFunction).max(Timestamp::compareTo)
                    .orElse(lastUpdateTime);
            int lastUpdateTimeCount = (int) dataList.stream().map(searchUpdateTimeFieldFunction)
                    .filter(x -> Objects.equals(x, currentMaxUpdateTime)).count();
            if (Objects.equals(lastUpdateTime, currentMaxUpdateTime)) {
                lastUpdateTimeSize += lastUpdateTimeCount;
            } else {
                lastUpdateTimeSize = lastUpdateTimeCount;
            }
            lastUpdateTime = currentMaxUpdateTime;
            lastUpdateTimeMaxId = dataList.stream().filter(x -> searchUpdateTimeFieldFunction.apply(x).compareTo(currentMaxUpdateTime) == 0)
                    .mapToLong(searchIdFieldFunction).max().orElse(lastUpdateTimeMaxId);
            effectRows += dealingDataFunction.applyAsInt(dataList);
            if (Objects.equals(saveSyncIncrementMarkForEveryRecursive, Boolean.TRUE)) {
                saveKeyAndValue(lastSyncIncrementMark, lastUpdateTime, 0L);
            }
        }
        saveKeyAndValue(lastSyncIncrementMark, lastUpdateTime, lastUpdateTimeSize);
        return effectRows;
    }

    /**
     * 保存最新更新记录
     *
     * @param key           key
     * @param time          更新时间
     * @param lastUpdateNum 最新数据条数
     */
    private void saveKeyAndValue(String key, Timestamp time, Long lastUpdateNum) {
        InternalKeyValueRequestDTO internalKeyValueRequestDTO = new InternalKeyValueRequestDTO();
        internalKeyValueRequestDTO.setKey(key);
        internalKeyValueRequestDTO.setValue(String.format("%s-%s", time.getTime(),
                ObjectUtils.defaultIfNull(lastUpdateNum, "")));
        //internalKeyValueService.saveKeyValue(internalKeyValueRequestDTO);
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
