package top.jadeyan.commons.json;

import java.util.List;

/**
 * 复杂对象
 *
 * @author yan
 * @time 2021/2/20 13:57
 **/
public class ObjectInfo<T> {
    /**
     * 数据列表
     */
    private List<T> dataList;
    /**
     * 数据类型
     */
    private String dataType;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
