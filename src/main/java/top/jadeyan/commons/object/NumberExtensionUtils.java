package top.jadeyan.commons.object;

import com.google.common.collect.Range;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 数字扩展工具
 *
 * @author yan
 */
public final class NumberExtensionUtils {

    /**
     * 误差， java 数字计算是有误差的
     */
    private static final double ERROR_ACCURACY = 0.00000005;
    private static final String EMPTY_PLACE_HOLDER = "--";
    private static final double EPSILON = 0.00000001;
    private NumberExtensionUtils() {
    }

    /**
     * 任何一个相等， 即任意compare = 0 相等
     *
     * @param item  目标
     * @param array 数据组
     * @param <T>   泛型
     * @return 任何一个相等
     */
    @SafeVarargs
    public static <T extends Number & Comparable<?>> boolean anyEqual(T item, T... array) {
        if (Objects.isNull(item) || ArrayUtils.isEmpty(array)) {
            return false;
        }
        for (T t : array) {
            if (Objects.nonNull(t) && isZeroOrNull(t.doubleValue() - item.doubleValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是0或者空
     *
     * @param number 数字
     * @param <T>    泛型
     * @return 是否是0或者空
     */
    public static <T extends Number & Comparable<?>> boolean isZeroOrNull(T number) {
        if (Objects.isNull(number)) {
            return true;
        }
        // java 可能会失精度
        return Math.abs(number.doubleValue()) < ERROR_ACCURACY;
    }

    /**
     * 是否所有未0或者空
     *
     * @param numbers 数字
     * @param <T>     泛型
     * @return 是否所有未0或者空
     */
    @SafeVarargs
    public static <T extends Number & Comparable<?>> boolean isAllZeroOrNull(T... numbers) {
        if (ArrayUtils.isEmpty(numbers)) {
            return false;
        }
        for (T number : numbers) {
            if (!isZeroOrNull(number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转为int 数据
     *
     * @param numStr 数字字符串
     * @return int 数据
     */
    public static Optional<Integer> parseInt(String numStr) {
        return parseNumber(numStr).map(Number::intValue);
    }

    /**
     * 转化为 long 数据
     *
     * @param numStr 数字字符串
     * @return long 数据
     */
    public static Optional<Long> parseLong(String numStr) {
        return parseNumber(numStr).map(Number::longValue);
    }

    /**
     * 转化为 BigDecimal 数据
     *
     * @param numStr 数字字符串
     * @return BigDecimal 数据
     */
    public static Optional<BigDecimal> parseBigDecimal(String numStr) {
        if (StringUtils.isBlank(numStr) || !NumberUtils.isCreatable(numStr)) {
            return Optional.empty();
        }
        String useNumStr = numStr.replace("f", "").replace("F", "")
                .replace("d", "").replace("D", "")
                .replace("l", "").replace("L", "");
        return Optional.of(new BigDecimal(useNumStr));
    }

    /**
     * 转化为 数字
     *
     * @param numStr 数字字符串
     * @return 数字
     */
    public static Optional<Number> parseNumber(String numStr) {
        if (StringUtils.isBlank(numStr) || !NumberUtils.isCreatable(numStr)) {
            return Optional.empty();
        }
        Number number = NumberUtils.createNumber(numStr);
        return Optional.of(number);
    }

    /**
     * 取范围的交集
     *
     * @param range1 范围1
     * @param range2 范围2
     * @param <T>    数字泛型
     * @return 交集
     */
    public static <T extends Number & Comparable<T>> Optional<Range<T>> intersect(Range<T> range1, Range<T> range2) {
        if (range1.upperEndpoint().compareTo(range2.lowerEndpoint()) < 0
                || range2.upperEndpoint().compareTo(range1.lowerEndpoint()) < 0) {
            return Optional.empty();
        }
        T min = range1.lowerEndpoint().compareTo(range2.lowerEndpoint()) > 0 ? range1.lowerEndpoint() : range2.lowerEndpoint();
        T max = range1.upperEndpoint().compareTo(range2.upperEndpoint()) > 0 ? range2.upperEndpoint() : range1.upperEndpoint();
        return Optional.of(Range.closed(min, max));
    }

    /**
     * 获取唯一id 通过两个int
     *
     * @param left  左边
     * @param right 右边
     * @return 唯一id
     */
    @SuppressWarnings("squid:S109")
    public static long uniqueLong(int left, int right) {
        long uniqueId = left;
        uniqueId = uniqueId << 32;
        uniqueId += right;
        return uniqueId;
    }

    /**
     * 获取另外一个生成唯一数
     *
     * @param uniqueLong 唯一long
     * @param one        其中一个生成数字
     * @return 获取另外一个生成唯一数
     */
    @SuppressWarnings("squid:S109")
    public static Optional<Integer> getAnother(long uniqueLong, int one) {
        long o1 = uniqueLong >> 32;
        long o2 = uniqueLong - (o1 << 32);
        if (o1 != one && o2 != one) {
            return Optional.empty();
        }
        return o1 == one ? Optional.of((int) o2) : Optional.of((int) o1);
    }

    /**
     * 判断数字是否是空
     *
     * @param number 价格
     * @return 价格数据是否为空
     */
    public static boolean isNullOrZero(final Number number) {
        if (Objects.isNull(number)) {
            return true;
        }
        if (number instanceof BigDecimal) {
            return BigDecimal.ZERO.compareTo((BigDecimal) number) == 0;
        }
        if (number instanceof Integer) {
            return Integer.valueOf(0).compareTo((Integer) number) == 0;
        }
        if (number instanceof Long) {
            return Long.valueOf(0).compareTo((Long) number) == 0;
        }
        if (number instanceof Short) {
            return number.shortValue() == 0;
        }
        if (number instanceof Double) {
            return Math.abs(number.doubleValue()) < EPSILON;
        }
        if (number instanceof Float) {
            return Math.abs(number.floatValue()) < EPSILON;
        }
        return false;
    }

    /**
     * 获取值
     *
     * @param valueStr 值字符串
     * @return 值
     */
    @SuppressWarnings("squid:S1166")
    public static Optional<BigDecimal> getValue(String valueStr) {
        try {
            if (StringUtils.isBlank(valueStr)) {
                return Optional.empty();
            }
            // 防止出现空格导致转化失败
            String useValue = valueStr.trim();
            String emptyPlaceHolder1 = "-";
            String emptyPlaceHolder3 = "---";
            if (emptyPlaceHolder1.equals(valueStr)
                    || EMPTY_PLACE_HOLDER.equals(valueStr)
                    || emptyPlaceHolder3.equals(valueStr)) {
                return Optional.empty();
            }
            BigDecimal value = new BigDecimal(useValue);
            return Optional.of(value);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
