package top.jadeyan.commons.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 前端 <Select/> 组件数据传输DTO
 */
@ApiModel(description = "前端 <Select/> 组件数据传输DTO")
public class SelectorDTO {
    @ApiModelProperty("标签名")
    private String label;
    @ApiModelProperty("标签code")
    private Integer value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
