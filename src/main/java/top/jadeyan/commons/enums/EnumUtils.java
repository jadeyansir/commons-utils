package top.jadeyan.commons.enums;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 枚举处理工具类
 *
 * @author yan
 * @date 2020/9/25
 */
public final class EnumUtils {

    private static final String PACKAGE = "top.jadeyan.commmons.enums.";
    private static final String METHOD_NAME = "getText";
    private static final String VALUE_NAME = "getValue";

    private static final ConcurrentHashMap<Class<? extends ITextValueEnum>, Map<Integer, ? extends ITextValueEnum>> ENUM_CLASS_TO_VALUE_ENUM_MAP = new ConcurrentHashMap<>();

    private EnumUtils() {

    }

    /**
     * 根据枚举值的编号返回枚举 后续版本可能会移除
     *
     * @param value      枚举值的编号
     * @param targetEnum 想要转化的枚举的类型
     * @return T
     * @see #ofNullable(Class, Integer)
     * @see #of(Class, Integer)
     */
    @SuppressWarnings("squid:S1133")
    public static <T extends ITextValueEnum> Optional<T> getEnumByValue(Integer value, Class<T> targetEnum) {
        return EnumUtils.ofNullable(targetEnum, value);
    }

    /**
     * 根据枚举值的内容返回枚举 后续版本可能会移除
     *
     * @param text       枚举值的内容
     * @param targetEnum 想要转化的枚举的类型
     * @return Optional<T>
     * @see #ofNullable(Class, Integer)
     * @see #of(Class, Integer)
     */
    @SuppressWarnings("squid:S1133")
    public static <T extends ITextValueEnum> Optional<T> getEnumByTextValue(String text, Class<T> targetEnum) {
        for (int i = 0; i < targetEnum.getEnumConstants().length; i++) {
            if (targetEnum.getEnumConstants()[i].getText().equals(text)) {
                return Optional.of(targetEnum.getEnumConstants()[i]);
            }
        }
        return Optional.empty();
    }


    /**
     * 根据枚举名称获取枚举中的text和value（只支持text,value）
     *
     * @param enumName 枚举类名称 首字母大写
     * @return SelectorDTO
     * @throws ClassNotFoundException    类未发现异常
     * @throws NoSuchMethodException     没有此方法异常
     * @throws InvocationTargetException 执行异常
     * @throws IllegalAccessException    非法获取异常
     */
    @SuppressWarnings("squid:S2658")
    public static List<SelectorDTO> listSelectorsByEnumName(String enumName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<SelectorDTO> selectorDTOList = Collections.synchronizedList(new ArrayList<>());
        Class<?> curEnumClass = EnumUtils.class.getClassLoader().loadClass(PACKAGE + enumName);
        Method nameMethod = curEnumClass.getMethod(METHOD_NAME);
        Method codeMethod = curEnumClass.getMethod(VALUE_NAME);
        Object[] enumConstants = curEnumClass.getEnumConstants();
        for (Object em : enumConstants) {
            SelectorDTO selectorDTO = new SelectorDTO();
            selectorDTO.setValue((Integer) codeMethod.invoke(em));
            selectorDTO.setLabel((String) nameMethod.invoke(em));
            selectorDTOList.add(selectorDTO);
        }
        return selectorDTOList;
    }

    /**
     * 获取枚举值映射,如果不存在,则会自动初始化一次
     *
     * @param iTextValueEnumClazz 枚举class类
     * @param value               键值
     * @return T                  对应枚举
     * @author dengmeiluan
     * @date 2022/10/19 14:28
     * @see #ofNullable(Class, Integer)
     * @see #of(Class, Integer)
     * @deprecated 请使用getEnumNullable/getEnum
     */
    @Deprecated
    @Nullable
    @SuppressWarnings("squid:S1133")
    public static <T extends ITextValueEnum> T getEnumByEnumValueFromCache(final Class<T> iTextValueEnumClazz, @Nullable final Integer value) {
        return _getEnum(iTextValueEnumClazz, value);
    }


    /**
     * 获取枚举值映射,如果不存在,则会自动初始化一次
     *
     * @param iTextValueEnumClazz 枚举class类
     * @param value               键值
     * @return T                  对应枚举
     * @author dengmeiluan
     * @date 2022/10/19 14:28
     */
    @Nullable
    @SuppressWarnings({"squid:S2445", "squid:S00100", "unchecked"})
    private static <T extends ITextValueEnum> T _getEnum(@Nonnull final Class<T> iTextValueEnumClazz, @Nullable final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        //先get再compute,不然存在了也要加桶头结点锁,浪费
        Map<Integer, ? extends ITextValueEnum> iTextValueEnumMappingMap = ENUM_CLASS_TO_VALUE_ENUM_MAP.get(iTextValueEnumClazz);
        if (Objects.nonNull(iTextValueEnumMappingMap)) {
            return (T) iTextValueEnumMappingMap.get(value);
        }
        synchronized (iTextValueEnumClazz) {
            iTextValueEnumMappingMap = ENUM_CLASS_TO_VALUE_ENUM_MAP.get(iTextValueEnumClazz);
            if (Objects.nonNull(iTextValueEnumMappingMap)) {
                return (T) iTextValueEnumMappingMap.get(value);
            }
            iTextValueEnumMappingMap = Arrays.stream(iTextValueEnumClazz.getEnumConstants()).collect(Collectors.toMap(ITextValueEnum::getValue, Function.identity()));
            ENUM_CLASS_TO_VALUE_ENUM_MAP.put(iTextValueEnumClazz, iTextValueEnumMappingMap);
            return (T) iTextValueEnumMappingMap.get(value);
        }
    }

    /**
     * 非常高效的获取枚举，value可以为null，结果可能是null
     *
     * @param clazz clazz
     * @param value value
     * @param <T>   <T>
     * @return ITextValueEnum
     */
    @Nullable
    public static <T extends ITextValueEnum> T getEnumNullable(@Nonnull Class<T> clazz, @Nullable Integer value) {
        return EnumUtils._getEnum(clazz, value);
    }

    /**
     * 非常高效的获取枚举，value不可以为null，结果不可能是null
     *
     * @param clazz clazz
     * @param value value
     * @param <T>   <T>
     * @return ITextValueEnum
     */
    @Nonnull
    public static <T extends ITextValueEnum> T getEnum(@Nonnull Class<T> clazz, @Nonnull Integer value) {
        return Optional.ofNullable(EnumUtils._getEnum(clazz, value)).orElseThrow(() -> new IllegalArgumentException(
                "No enum constant found by value: " + value));
    }

    /**
     * 非常高效的获取枚举，value可以为null，Optional结果可能是null
     *
     * @param clazz clazz
     * @param value value
     * @param <T>   <T>
     * @return ITextValueEnum
     */
    public static <T extends ITextValueEnum> Optional<T> ofNullable(@Nonnull Class<T> clazz, @Nullable Integer value) {
        return Optional.ofNullable(EnumUtils._getEnum(clazz, value));
    }


    /**
     * 非常高效的获取枚举，value不可以为null，Optional结果不可能是null
     *
     * @param clazz clazz
     * @param value value
     * @param <T>   <T>
     * @return ITextValueEnum
     */
    public static <T extends ITextValueEnum> Optional<T> of(@Nonnull Class<T> clazz, @Nonnull Integer value) {
        return Optional.of(EnumUtils.getEnum(clazz, value));
    }
}
