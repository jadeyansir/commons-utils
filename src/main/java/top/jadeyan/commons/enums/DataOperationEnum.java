package top.jadeyan.commons.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 数据操作枚举
 *
 * @author yan
 * @date 2021/7/9
 */
public enum DataOperationEnum implements ITextValueEnum {
    /**
     * 插入操作
     */
    INSERT(1, "INSERT"),
    /**
     * 更新操作
     */
    UPDATE(2, "UPDATE"),
    /**
     * 删除操作
     */
    DELETE(3, "DELETE"),
    /**
     * 数据定义改变
     */
    ALERT(4, "ALERT");

    private final int value;
    private final String text;

    DataOperationEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * 根据枚举中文获取数据操作枚举
     *
     * @param text 枚举中文
     * @return 数据操作枚举
     */
    public static Optional<DataOperationEnum> getDataOperationEnumByText(String text) {
        DataOperationEnum[] values = DataOperationEnum.values();
        for (DataOperationEnum operationEnum : values) {
            if (StringUtils.equalsIgnoreCase(operationEnum.getText(), text)) {
                return Optional.of(operationEnum);
            }
        }
        return Optional.empty();
    }

}
