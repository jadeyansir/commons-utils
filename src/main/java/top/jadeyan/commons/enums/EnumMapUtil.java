package top.jadeyan.commons.enums;

import org.springframework.util.CollectionUtils;
import top.jadeyan.commons.exception.BusinessException;

import java.util.*;
import java.util.stream.Collectors;

import static top.jadeyan.commons.enums.ExceptionEnum.NON_ENUM_ERROR;

/**
 * 枚举集合
 *
 * @author yan
 */
public final class EnumMapUtil {

    /**
     * 根据枚举值的编号返回枚举
     *
     * @param value      枚举值的编号
     * @param targetEnum 想要转化的枚举的类型
     * @return T
     */
    public static <T extends ITextValueEnum> Optional<T> getEnumByValue(Integer value, Class<T> targetEnum) {
        for (int i = 0; i < targetEnum.getEnumConstants().length; i++) {
            if (Objects.equals(targetEnum.getEnumConstants()[i].getValue(), value)) {
                return Optional.of(targetEnum.getEnumConstants()[i]);
            }
        }
        return Optional.empty();
    }

    /**
     * 获取多个枚举
     *
     * @param values     枚举值的编号
     * @param targetEnum 想要转化的枚举的类型
     * @return Map<Integer, Optional < T>>
     */
    public static <T extends ITextValueEnum> Map<Integer, Optional<T>> getEnumByValueList(Collection<Integer> values, Class<T> targetEnum) {
        Map<Integer, Optional<T>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(values)) {
            return resultMap;
        }
        Map<Integer, T> map = Arrays.stream(targetEnum.getEnumConstants()).collect(Collectors.toMap(ITextValueEnum::getValue, x -> x));
        for (Integer value : values) {
            if (map.containsKey(value)) {
                resultMap.put(value, Optional.of(map.get(value)));
            } else {
                resultMap.put(value, Optional.empty());
            }
        }
        return resultMap;
    }

    /**
     * 根据枚举值的编号返回枚举。 枚举值无效抛出异常
     *
     * @param value      枚举值的编号
     * @param targetEnum 想要转化的枚举的类型
     * @return T 泛型
     */
    public static <T extends ITextValueEnum> T getEnumByValueWithAllValidValue(Integer value, Class<T> targetEnum) {
        for (int i = 0; i < targetEnum.getEnumConstants().length; i++) {
            if (Objects.equals(targetEnum.getEnumConstants()[i].getValue(), value)) {
                return targetEnum.getEnumConstants()[i];
            }
        }
        throw new BusinessException(NON_ENUM_ERROR);
    }

    /**
     * 获取多个枚举
     *
     * @param values     枚举值的编号
     * @param targetEnum 想要转化的枚举的类型
     * @return List<T>
     */
    public static <T extends ITextValueEnum> List<T> getEnumByValueListWithAllValidValue(Collection<Integer> values, Class<T> targetEnum) {
        List<T> enumList = new LinkedList<>();
        if (CollectionUtils.isEmpty(values)) {
            return enumList;
        }
        Map<Integer, T> map = Arrays.stream(targetEnum.getEnumConstants()).collect(Collectors.toMap(ITextValueEnum::getValue, x -> x));
        for (Integer value : values) {
            if (map.containsKey(value)) {
                enumList.add(map.get(value));
            } else {
                throw new BusinessException(NON_ENUM_ERROR);
            }
        }
        return enumList;
    }

    /**
     * 根据枚举值的内容返回枚举
     *
     * @param value      枚举值的内容
     * @param targetEnum 想要转化的枚举的类型
     * @return Optional<T>
     */
    public static <T extends ITextValueEnum> Optional<T> getEnumByTextValue(String value, Class<T> targetEnum) {
        for (int i = 0; i < targetEnum.getEnumConstants().length; i++) {
            if (targetEnum.getEnumConstants()[i].getText().equals(value)) {
                return Optional.of(targetEnum.getEnumConstants()[i]);
            }
        }
        return Optional.empty();
    }

    /**
     * 获取多个枚举
     *
     * @param values     枚举值的内容
     * @param targetEnum 想要转化的枚举的类型
     * @return Map<String, Optional < T>>
     */
    public static <T extends ITextValueEnum> Map<String, Optional<T>> getEnumByTextList(Collection<String> values, Class<T> targetEnum) {
        Map<String, Optional<T>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(values)) {
            return resultMap;
        }
        Map<String, T> map = Arrays.stream(targetEnum.getEnumConstants()).collect(Collectors.toMap(ITextValueEnum::getText, x -> x));
        for (String value : values) {
            if (map.containsKey(value)) {
                resultMap.put(value, Optional.of(map.get(value)));
            } else {
                resultMap.put(value, Optional.empty());
            }
        }
        return resultMap;
    }

    /**
     * 根据枚举值的内容返回枚举
     *
     * @param value      枚举值的内容
     * @param targetEnum 想要转化的枚举的类型
     * @return 枚举
     */
    public static <T extends ITextValueEnum> T getEnumByTextValueWithAllValidValue(String value, Class<T> targetEnum) {
        for (int i = 0; i < targetEnum.getEnumConstants().length; i++) {
            if (targetEnum.getEnumConstants()[i].getText().equals(value)) {
                return targetEnum.getEnumConstants()[i];
            }
        }
        throw new BusinessException(NON_ENUM_ERROR);
    }

    /**
     * 获取多个枚举
     *
     * @param values     枚举值的内容
     * @param targetEnum 想要转化的枚举的类型
     * @return List<T>
     */
    public static <T extends ITextValueEnum> List<T> getEnumByTextListWithAllValidValue(Collection<String> values, Class<T> targetEnum) {
        List<T> enumList = new LinkedList<>();
        if (CollectionUtils.isEmpty(values)) {
            return enumList;
        }
        Map<String, T> map = Arrays.stream(targetEnum.getEnumConstants()).collect(Collectors.toMap(ITextValueEnum::getText, x -> x));
        for (String value : values) {
            if (map.containsKey(value)) {
                enumList.add(map.get(value));
            } else {
                throw new BusinessException(NON_ENUM_ERROR);
            }
        }
        return enumList;
    }

    private EnumMapUtil() {
    }

}
