package top.jadeyan.commons.mq;

import org.apache.commons.collections.CollectionUtils;
import top.jadeyan.commons.object.BeanCopyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Canal 平铺的 数据同步BO
 *
 * @param <T> 泛型数组
 */
public class CanalFlatMessageBO<T> {
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 操作类型INSERT、UPDATE、DELETE
     */
    private String type;
    /**
     * 数据库
     */
    private String database;
    /**
     * 表名
     */
    private String table;
    /**
     * 更新前的旧值
     */
    private List<T> old;

    /**
     * 转换为 canal 消息模型扩展类型
     *
     * @return canal 消息模型扩展类型
     */
    public CanalFlatMessageExtBO<T> convertToCanalFlatMessageExtBO() {
        boolean hasOld = CollectionUtils.isNotEmpty(old);
        CanalFlatMessageExtBO<T> canalFlatMessageExtBO = new CanalFlatMessageExtBO<>();
        BeanCopyUtils.copyProperties(this, canalFlatMessageExtBO);
        List<CanalDataBO<T>> dataDTOS = data.stream().map(t -> {
            CanalDataBO<T> canalDataBO = new CanalDataBO<>();
            canalDataBO.setCurrent(t);
            //防止空指针异常
            if (hasOld) {
                canalDataBO.setOld((old.get(data.indexOf(t))));
            }
            return canalDataBO;
        }).collect(Collectors.toList());
        canalFlatMessageExtBO.setData(dataDTOS);
        return canalFlatMessageExtBO;
    }

    public List<T> getData() {
        return Objects.isNull(data) ? null : new ArrayList<>(data);
    }

    public void setData(List<T> data) {
        this.data = Objects.isNull(data) ? null : new ArrayList<>(data);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<T> getOld() {
        return Objects.isNull(old) ? null : new ArrayList<>(old);
    }

    public void setOld(List<T> old) {
        this.old = Objects.isNull(old) ? null : new ArrayList<>(old);
    }

}
