package top.jadeyan.commons.enums;

import java.util.Arrays;

/**
 * 异常接口
 *
 * @author yan
 */
public interface IExceptionEnum {
    /**
     * 获取code
     *
     * @return 返回值
     */
    int getCode();

    /**
     * 获取信息
     *
     * @return 返回信息
     */
    String getMessage();

    /**
     * 获取所有value值大于0的枚举值
     *
     * @param exceptionEums 枚举类型
     * @param values        值
     * @param <T>           返回类型
     * @return 返回所有value值大于0的枚举值
     */
    static <T extends ITextValueEnum> ITextValueEnum[] getEnums(Class<T> exceptionEums, int values) {
        return Arrays.stream(exceptionEums.getEnumConstants())
                .filter(x -> (values & x.getValue()) > 0).toArray(ITextValueEnum[]::new);
    }

    /**
     * 获取所有枚举值
     *
     * @param exceptionEnums 枚举类型
     * @param <T>            返回类型
     * @return 返回所有的枚举值
     */
    static <T extends ITextValueEnum> ITextValueEnum[] getAllEnums(Class<T> exceptionEnums) {
        return exceptionEnums.getEnumConstants();
    }
}
