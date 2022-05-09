package top.jadeyan.commons.object;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * BigDecimal类型处理工具类
 *
 * @author yan
 * @time 2020/11/5 16:52
 */
public final class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    private static final BigDecimal WAN_AND_BILLION_CONVERT_RATIO = BigDecimal.valueOf(10000);
    private static final BigDecimal YUAN_AND_WAN_CONVERT_RATIO = BigDecimal.valueOf(10000);
    private static final BigDecimal YUAN_AND_BILLION_CONVERT_RATIO = BigDecimal.valueOf(1_0000_0000);
    private static final int DEFAULT_SCALE = 2;

    /**
     * Big Decimal 计算精度 为4
     */
    private static final int BIG_DECIMAL_SCALE_FOUR = 4;

    /**
     * 万 --> 亿 转换， 默认两位精度
     *
     * @param sourceData 原始数据
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertWToBillion(BigDecimal sourceData) {
        return convertWToBillion(sourceData, DEFAULT_SCALE);
    }

    /**
     * 万 --> 亿 转换
     *
     * @param sourceData 原始数据
     * @param scale      保留精度
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertWToBillion(BigDecimal sourceData, int scale) {
        if (Objects.isNull(sourceData)) {
            return Optional.empty();
        }
        return Optional.of(sourceData.divide(WAN_AND_BILLION_CONVERT_RATIO, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 元 --> 亿 转换， 默认两位精度
     *
     * @param sourceData 原始数据
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertYuanToBillion(BigDecimal sourceData) {
        return convertYuanToBillion(sourceData, DEFAULT_SCALE);
    }

    /**
     * 元 --> 亿 转换， 默认两位精度
     *
     * @param sourceData 原始数据
     * @param scale      保留精度
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertYuanToBillion(BigDecimal sourceData, int scale) {
        if (Objects.isNull(sourceData)) {
            return Optional.empty();
        }
        return Optional.of(sourceData.divide(YUAN_AND_BILLION_CONVERT_RATIO, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 元 --> 万 转换, 默认两位精度
     *
     * @param sourceData 原始数据
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertYuanToW(BigDecimal sourceData) {
        return convertYuanToW(sourceData, DEFAULT_SCALE);
    }

    /**
     * 元 --> 万 转换
     *
     * @param sourceData 原始数据
     * @param scale      保留精度
     * @return 转换后数据
     */
    public static Optional<BigDecimal> convertYuanToW(BigDecimal sourceData, int scale) {
        if (Objects.isNull(sourceData)) {
            return Optional.empty();
        }
        return Optional.of(sourceData.divide(YUAN_AND_WAN_CONVERT_RATIO, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 处理数据精度
     *
     * @param sourceData 原始数据
     * @param scale      保留精度
     * @return 处理后数据
     */
    public static Optional<BigDecimal> handlerPrecision(BigDecimal sourceData, int scale) {
        if (Objects.isNull(sourceData)) {
            return Optional.empty();
        }
        return Optional.of(sourceData.setScale(scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 加法 过滤 为 null 的 数据 若所有数据为null 则 返回 Optional.empty()
     *
     * @param dataList 需要相加的数值
     * @return Optional<BigDecimal>
     */
    public static Optional<BigDecimal> safeAdd(Iterable<BigDecimal> dataList) {
        BigDecimal result = BigDecimal.ZERO;
        boolean allNullTag = true;
        for (BigDecimal calOne : dataList) {
            if (Objects.nonNull(calOne)) {
                result = result.add(calOne);
                allNullTag = false;
            }
        }
        if (allNullTag) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    /**
     * 加法 过滤 为 null 的 数据 若所有数据为null 则 返回 Optional.empty()
     *
     * @param number 传入的要叠加的数
     * @return Optional<BigDecimal>
     */
    public static Optional<BigDecimal> safeAdd(BigDecimal... number) {
        BigDecimal result = BigDecimal.ZERO;
        boolean allNullTag = true;
        for (BigDecimal calOne : number) {
            if (Objects.nonNull(calOne)) {
                result = result.add(calOne);
                allNullTag = false;
            }
        }
        if (allNullTag) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result);
        }
    }

    /**
     * 减法 若 两数中 有一个为 null 则返回 optional.empty
     *
     * @param minuend    被减数
     * @param subtracted 减数
     * @return Optional<BigDecimal>
     */
    public static Optional<BigDecimal> safeSubtract(BigDecimal minuend, BigDecimal subtracted) {
        if (Objects.isNull(minuend) || Objects.isNull(subtracted)) {
            return Optional.empty();
        }
        return Optional.of(minuend.subtract(subtracted));
    }

    /**
     * 除法 若两数中 任意一个为null 或者 除数为 0 则 返回 optional.empty
     *
     * @param dividend     被除数
     * @param divisor      除数
     * @param roundingMode 小数保留模式
     * @return Optional<BigDecimal>
     */
    public static Optional<BigDecimal> safeDivide(BigDecimal dividend, BigDecimal divisor, RoundingMode roundingMode) {
        if (Objects.nonNull(dividend) && Objects.nonNull(divisor) && (BigDecimal.ZERO.compareTo(divisor) != 0)) {
            return Optional.ofNullable(dividend.divide(divisor, BIG_DECIMAL_SCALE_FOUR, roundingMode));
        }
        return Optional.empty();
    }

    /**
     * 乘法 过滤 为 null 的 数据  若一个 为 null 则 返回 optional.empty
     *
     * @param number 要乘的数
     * @return Optional<BigDecimal>
     */
    public static Optional<BigDecimal> safeMultiply(BigDecimal... number) {
        BigDecimal result = BigDecimal.ONE;
        for (BigDecimal calOne : number) {
            if (Objects.isNull(calOne)) {
                return Optional.empty();
            } else {
                result = result.multiply(calOne);
            }
        }
        return Optional.of(result);
    }


}
