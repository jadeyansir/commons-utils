package top.jadeyan.commons.job;

import io.swagger.annotations.ApiModelProperty;

/**
 * 内部KeyValue
 * @author yan
 * @date 2022/01/19
 */
public class InternalKeyValueRequestDTO {
    @ApiModelProperty("键")
    private String key;
    @ApiModelProperty("值")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
