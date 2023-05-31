package top.jadeyan.commons.object;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * 数学计算Utils
 */
public final class MathExtUtils {

    private MathExtUtils() {

    }

    private static final BigDecimal DECIMAL_HUNDRED = new BigDecimal(100);
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 默认保留4位小数
     */
    public static final int DEFAULT_SCALE = 4;
    private static final int TWO_SCALE = 2;

    /**
     * 比较两个 BigDecimal大小 1=v1>v2,0=v1=v2,-1=v1<v2
     *
     * @param v1 值1
     * @param v2 值2
     * @return 比较大小
     */
    public static Integer compare(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1) && Objects.isNull(v2)) {
            return 0;
        } else if (Objects.nonNull(v1) && Objects.isNull(v2)) {
            return 1;
        } else if (Objects.isNull(v1)) {
            return -1;
        } else {
            return v1.compareTo(v2);
        }
    }

    /**
     * 计算两个整数相加
     *
     * @param v1 加数1
     * @param v2 加数1
     * @return v1+v2
     */
    public static Optional<Integer> add(Integer v1, Integer v2) {
        if (Objects.isNull(v1)) {
            return Optional.ofNullable(v2);
        } else if (Objects.isNull(v2)) {
            return Optional.ofNullable(v1);
        } else {
            return Optional.ofNullable(v1 + v2);
        }
    }

    /**
     * 计算两个BigDecimal 相加
     *
     * @param v1 加数1
     * @param v2 加数1
     * @return v1+v2
     */
    public static Optional<BigDecimal> add(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            return Optional.ofNullable(v2);
        } else if (Objects.isNull(v2)) {
            return Optional.ofNullable(v1);
        } else {
            return Optional.ofNullable(v1.add(v2));
        }
    }

    /**
     * 计算两个BigDecimal 相加
     *
     * @param v1 加数1
     * @param v2 加数1
     * @return v1+v2
     */
    public static Optional<Long> add(Long v1, Long v2) {
        if (Objects.isNull(v1)) {
            return Optional.ofNullable(v2);
        } else if (Objects.isNull(v2)) {
            return Optional.ofNullable(v1);
        } else {
            return Optional.ofNullable(v1 + v2);
        }
    }

    /**
     * 安全性减法
     *
     * @param minuend 被减数
     * @param subs    多个减数(可为空)
     * @return 运算结果
     */
    public static BigDecimal subtract(BigDecimal minuend, BigDecimal... subs) {
        if (null == minuend) {
            return null;
        }
        BigDecimal result = minuend;
        for (BigDecimal sub : subs) {
            if (null == sub) {
                continue;
            } else {
                result = result.subtract(sub);
            }
        }

        return result;
    }

    /**
     * 安全性除法
     *
     * @param dividend     被除数
     * @param divisor      除数
     * @param scale        精度
     * @param roundingMode 精度保留模式
     * @return 运算结果
     */
    public static Optional<BigDecimal> divide(BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return Objects.nonNull(dividend) && Objects.nonNull(divisor) && BigDecimal.ZERO.compareTo(divisor) != 0 ?
                Optional.of(dividend.divide(divisor, scale, roundingMode)) : Optional.empty();
    }

    /**
     * 计算百分比 结果*100
     *
     * @param v1 分子
     * @param v2 分母
     * @return 计算百分比
     */
    public static Optional<BigDecimal> percentage(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1) || Objects.isNull(v2) || v2.compareTo(BigDecimal.ZERO) == 0) {
            return Optional.empty();
        }
        return Optional.of(v1.multiply(DECIMAL_HUNDRED).divide(v2, DEFAULT_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * 获取百分比
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 百分比
     */
    public static Optional<BigDecimal> getPercentage(BigDecimal dividend, BigDecimal divisor) {
        if (Objects.nonNull(dividend) && Objects.nonNull(divisor) && BigDecimal.ZERO.compareTo(divisor) != 0) {
            return Optional.of(dividend.multiply(DECIMAL_HUNDRED).divide(divisor, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE));
        }
        return Optional.empty();
    }

    /**
     * 获取百分比
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 百分比
     */
    public static Optional<BigDecimal> getPercentage(Number dividend, Number divisor) {
        if (Objects.nonNull(dividend) && Objects.nonNull(divisor) && BigDecimal.ZERO.compareTo(new BigDecimal(divisor.toString())) != 0) {
            return Optional.of(new BigDecimal(dividend.toString()).multiply(DECIMAL_HUNDRED).divide(new BigDecimal(divisor.toString()), DEFAULT_SCALE, DEFAULT_ROUNDING_MODE));
        }
        return Optional.empty();
    }


    /**
     * 获取百分比
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    精度
     * @return 百分比
     */
    public static Optional<BigDecimal> getPercentage(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (Objects.nonNull(dividend) && Objects.nonNull(divisor) && BigDecimal.ZERO.compareTo(divisor) != 0) {
            return Optional.of(dividend.multiply(DECIMAL_HUNDRED).divide(divisor, scale, DEFAULT_ROUNDING_MODE));
        }
        return Optional.empty();
    }

    /**
     * 获取百分比
     *
     * @param dividend     被除数
     * @param divisor      除数
     * @param scale        精度
     * @param roundingMode 精度保留模式
     * @return 百分比
     */
    public static Optional<BigDecimal> getPercentage(BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMode) {
        if (Objects.nonNull(dividend) && Objects.nonNull(divisor) && BigDecimal.ZERO.compareTo(divisor) != 0) {
            return Optional.of(dividend.multiply(DECIMAL_HUNDRED).divide(divisor, scale, roundingMode));
        }
        return Optional.empty();
    }

    /**
     * 计算同比
     *
     * @param currNum 当前年数据
     * @param preNum  上一年数据
     * @return 计算结果 `
     */
    public static Optional<BigDecimal> calculateYoy(BigDecimal currNum, BigDecimal preNum) {
        if (Objects.nonNull(currNum) && Objects.nonNull(preNum) && preNum.compareTo(BigDecimal.ZERO) != 0) {
            return Optional.of(currNum.subtract(preNum).multiply(DECIMAL_HUNDRED).divide(preNum.abs(), TWO_SCALE, BigDecimal.ROUND_HALF_UP));
        }
        return Optional.empty();
    }

    /**
     * 辗转相减获取最大公约数
     *
     * @param a 获取公约数参数1
     * @param b 获取公约数参数2
     * @return 最大公约数
     */
    public static long getMaxCommonDivisor(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        if (a > b) {
            a = a + b;
            b = a - b;
            a = a - b;
        }
        long yu = 0;
        do {
            yu = b % a;
            b = a;
            a = yu;
        } while (yu != 0);
        return b;
    }

    /**
     * 求和
     *
     * @param number1 加数1
     * @param number2 加数2
     * @return 求和结果
     */
    public static Optional<Number> sum(Number number1, Number number2) {
        if (Objects.isNull(number1)) {
            return Optional.ofNullable(number2);
        } else if (Objects.isNull(number2)) {
            return Optional.of(number1);
        } else {
            return Optional.of(new BigDecimal(number1.toString()).add(new BigDecimal(number2.toString())));
        }
    }
}
