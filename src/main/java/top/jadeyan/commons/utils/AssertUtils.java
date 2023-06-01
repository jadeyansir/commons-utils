package top.jadeyan.commons.utils;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import top.jadeyan.commons.enums.IExceptionEnum;
import top.jadeyan.commons.exception.BusinessException;

import java.util.Objects;

/**
 * 断言工具类,继承org.springframework.util.Assert
 * 可简化代码逻辑. 增加易读性
 * 支持{@link BusinessException}
 * 支持{@link IExceptionEnum}
 *
 * @author yan
 */
public class AssertUtils extends Assert {

    /**
     * 断言一个布尔表达式，如果表达式的计算结果为false，则抛出BusinessException。
     *
     * @param expression        a boolean expression
     * @param businessException businessException
     */
    public static void isTrue(boolean expression, BusinessException businessException) {
        if (!expression) {
            throw businessException;
        }
    }

    /**
     * 断言一个布尔表达式，如果表达式的计算结果为false，则抛出BusinessException。
     *
     * @param expression    a boolean expression
     * @param exceptionEnum exceptionEnum
     */
    public static void isTrue(boolean expression, IExceptionEnum exceptionEnum) {
        if (!expression) {
            throw new BusinessException(exceptionEnum);
        }
    }

    /**
     * 断言一个对象不为空。
     *
     * @param object            a object
     * @param businessException businessException
     */
    public static void notNull(Object object, BusinessException businessException) {
        if (Objects.isNull(object)) {
            throw businessException;
        }
    }

    /**
     * 断言一个对象不为空。
     *
     * @param object        a object
     * @param exceptionEnum exceptionEnum
     */
    public static void notNull(Object object, IExceptionEnum exceptionEnum) {
        if (Objects.isNull(object)) {
            throw new BusinessException(exceptionEnum);
        }
    }

    /**
     * 非空
     * 断言数组非空
     *
     * @param array         array
     * @param exceptionEnum exceptionEnum
     */
    public static void notEmpty(Object[] array, IExceptionEnum exceptionEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throw new BusinessException(exceptionEnum);
        }
    }

}
