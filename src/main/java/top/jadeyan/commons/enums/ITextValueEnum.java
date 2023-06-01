package top.jadeyan.commons.enums;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文本信息接口
 *
 * @author yan
 */
public interface ITextValueEnum {

    /**
     * 获取枚举文本
     *
     * @return 获取枚举文本信息
     */
    String getText();

    /**
     * 获取枚举值
     *
     * @return 获取枚举值
     */
    int getValue();

    /**
     * 获取所有大于0的枚举值
     *
     * @param iTextValueEnumClass 枚举类型
     * @param values              枚举值
     * @param <T>                 返回类型
     * @return 返回所有大于0的枚举值
     */
    static <T extends ITextValueEnum> ITextValueEnum[] getEnums(Class<T> iTextValueEnumClass, int values) {
        return Arrays.stream(iTextValueEnumClass.getEnumConstants())
                .filter(x -> (values & x.getValue()) > 0).toArray(ITextValueEnum[]::new);
    }

    /**
     * 获取所有枚举值
     *
     * @param iTextValueEnumClass 枚举类型
     * @param <T>                 返回类型
     * @return 返回所有枚举值
     */
    static <T extends ITextValueEnum> T[] getAllEnums(Class<T> iTextValueEnumClass) {
        return iTextValueEnumClass.getEnumConstants();
    }

    /**
     * 获取所有大于0的枚举值
     *
     * @param iTextValueEnumClass 枚举类型
     * @param values              枚举值
     * @param <T>                 返回类型
     * @return 返回所有大于0的枚举值
     */
    static <T extends ITextValueEnum> List<T> listEnums(Class<T> iTextValueEnumClass, int values) {
        return Arrays.stream(iTextValueEnumClass.getEnumConstants())
                .filter(x -> (values & x.getValue()) > 0).collect(Collectors.toList());
    }

    /**
     * 获取所有枚举值
     *
     * @param iTextValueEnumClass 枚举类型
     * @param <T>                 返回类型
     * @return 返回所有枚举值
     */
    static <T extends ITextValueEnum> List<T> listAllEnums(Class<T> iTextValueEnumClass) {
        return Arrays.asList(iTextValueEnumClass.getEnumConstants());
    }


    /**
     * 获取对应的枚举值, 后续版本可能会移除
     * 推荐使用 {@link EnumUtils#of(Class, Integer)}
     * 推荐使用 {@link EnumUtils#ofNullable(Class, Integer)}
     *
     * @param clazz 类型
     * @param value 值
     * @param <T>   枚举类型
     * @return 对应的枚举
     */
    @Nonnull
    static <T extends ITextValueEnum> T getEnum(Class<T> clazz, int value) {
        // 此方法原来的逻辑获取不到枚举 则抛出异常，  现在保持和之前一样
        return EnumUtils.getEnum(clazz, value);
    }

    /**
     * 判断枚举值是否包含当前传入value
     *
     * @param iTextValueEnumClass iTextValueEnumClass
     * @param value               value
     * @return 返回所有的枚举值
     */
    static <T extends ITextValueEnum> boolean containsEnum(Class<T> iTextValueEnumClass, int value) {
        return Arrays.stream(iTextValueEnumClass.getEnumConstants()).
                anyMatch(iTextValueEnum -> Objects.equals(iTextValueEnum.getValue(), value));
    }

}
