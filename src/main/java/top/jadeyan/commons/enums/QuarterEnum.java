package top.jadeyan.commons.enums;

/**
 * 季度枚举
 *
 * @author yan
 **/
public enum QuarterEnum implements ITextValueEnum {

    /**
     * 一季度
     */
    Q1(1, "Q1"),

    /**
     * 二季度
     */
    Q2(2, "Q2"),

    /**
     * 三季度
     */
    Q3(3, "Q3"),

    /**
     * 四季度
     */
    Q4(4, "Q4"),

    ;

    QuarterEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    private final int value;
    private final String text;

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


}
