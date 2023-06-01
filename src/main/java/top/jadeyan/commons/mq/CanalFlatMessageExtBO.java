package top.jadeyan.commons.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Canal 平铺的 数据同步BO
 *
 * @param <T> 泛型数组
 */
public class CanalFlatMessageExtBO<T> {
    /**
     * 数据
     */
    private List<CanalDataBO<T>> data;
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


    public List<CanalDataBO<T>> getData() {
        return Objects.isNull(data) ? null : new ArrayList<>(data);
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

    public void setData(List<CanalDataBO<T>> data) {
        this.data = Objects.isNull(data) ? null : new ArrayList<>(data);
    }
}