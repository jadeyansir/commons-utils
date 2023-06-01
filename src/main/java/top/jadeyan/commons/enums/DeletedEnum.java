package top.jadeyan.commons.enums;

/**
 * 删除枚举
 *
 * @author yan
 * @Date 2022/08/16
 */
public enum DeletedEnum implements ITextValueEnum {

    /**
     * 未删除
     */
    NOT_DELETE(0, "未删除"),
    /**
     * 已删除
     */
    DELETE(1, "已删除");

    private final int value;
    private final String text;

    DeletedEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getValue() {
        return value;
    }
}
