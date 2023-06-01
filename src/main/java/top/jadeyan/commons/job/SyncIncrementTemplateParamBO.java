package top.jadeyan.commons.job;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * 定时任务增量参数
 *
 * @author yan
 * @create 2023/2/23
 */
public class SyncIncrementTemplateParamBO {
    /**
     * 起始时间
     */
    private Timestamp updateTime;
    /**
     * 时间是否正序u
     */
    private Boolean updateTimeAsc;
    /**
     * 起始Id
     */
    private Long startId;
    /**
     * 是否count
     */
    private Boolean count;
    /**
     * 分页条数
     */
    private Integer pageSize;

    /**
     * 全参构造
     *
     * @param updateTime    起始时间
     * @param updateTimeAsc 时间是否倒叙
     * @param startId       起始Id
     * @param count         是否count
     * @param pageSize      无参构造
     */
    private SyncIncrementTemplateParamBO(Timestamp updateTime, Boolean updateTimeAsc, Long startId, Boolean count, Integer pageSize) {
        this.updateTime = new Timestamp(updateTime.getTime());
        this.updateTimeAsc = updateTimeAsc;
        this.startId = startId;
        this.count = count;
        this.pageSize = pageSize;
    }

    /**
     * 构造
     *
     * @param updateTime    起始时间
     * @param updateTimeAsc 时间是否倒叙
     * @param startId       起始Id
     * @param count         是否count
     * @param pageSize      无参构造
     * @return 查询对象
     */
    public static SyncIncrementTemplateParamBO build(
            Timestamp updateTime, Boolean updateTimeAsc, Long startId, Boolean count, Integer pageSize) {
        return new SyncIncrementTemplateParamBO(updateTime, updateTimeAsc, startId, count, pageSize);
    }

    public Timestamp getUpdateTime() {
        return Objects.isNull(this.updateTime) ? null : new Timestamp(this.updateTime.getTime());
    }

    public Boolean getUpdateTimeAsc() {
        return updateTimeAsc;
    }

    public Long getStartId() {
        return startId;
    }

    public Boolean getCount() {
        return count;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
