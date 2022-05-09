package top.jadeyan.commons.object;

import com.google.common.collect.Range;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class NumberExtensionUtilsTest {

    @Test
    public void testBigDecimalIsZeroOrNull() {
        BigDecimal nullItem = null;
        assertTrue(NumberExtensionUtils.isZeroOrNull(nullItem));
        BigDecimal zeroItem = BigDecimal.valueOf(0);
        assertTrue(NumberExtensionUtils.isZeroOrNull(zeroItem));
        BigDecimal notZeroItem = BigDecimal.valueOf(0.000001);
        assertFalse(NumberExtensionUtils.isZeroOrNull(notZeroItem));

        BigDecimal a = BigDecimal.valueOf(1.0001);
        BigDecimal b = BigDecimal.valueOf(1.0001);
        BigDecimal c = BigDecimal.valueOf(1.0002);
        assertTrue(NumberExtensionUtils.isZeroOrNull(a.subtract(b)));
        assertFalse(NumberExtensionUtils.isZeroOrNull(a.subtract(c)));
    }

    @Test
    public void testLongIsZeroOrNull() {
        Long nullItem = null;
        assertTrue(NumberExtensionUtils.isZeroOrNull(nullItem));
        Long zeroItem = 0L;
        assertTrue(NumberExtensionUtils.isZeroOrNull(zeroItem));
        Long notZeroItem = 1L;
        assertFalse(NumberExtensionUtils.isZeroOrNull(notZeroItem));
    }

    @Test
    public void testDoubleIsZeroOrNull() {
        double a = 1.01;
        double b = 1.01;
        double c = a - b;
        assertTrue(NumberExtensionUtils.isZeroOrNull(c));
    }

    @Test
    public void testDoubleIsAllZeroOrNull() {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        assertTrue(NumberExtensionUtils.isAllZeroOrNull(0.0, bigDecimal, 0.00, 0.0000, null));
    }

    @Test
    public void testParseInt() {
        assertEquals(Integer.valueOf(1), NumberExtensionUtils.parseInt("1").get());
        assertEquals(Integer.valueOf(1), NumberExtensionUtils.parseInt("1d").get());
        assertEquals(Integer.valueOf(1), NumberExtensionUtils.parseInt("1f").get());
        assertEquals(Integer.valueOf(1), NumberExtensionUtils.parseInt("1l").get());
        assertEquals(Integer.valueOf(1), NumberExtensionUtils.parseInt("1.0").get());
        assertEquals(null, NumberExtensionUtils.parseInt("aaa").orElse(null));
    }

    @Test
    public void testParseLong() {
        assertEquals(Long.valueOf(1), NumberExtensionUtils.parseLong("1").get());
        assertEquals(Long.valueOf(1), NumberExtensionUtils.parseLong("1d").get());
        assertEquals(Long.valueOf(1), NumberExtensionUtils.parseLong("1f").get());
        assertEquals(Long.valueOf(1), NumberExtensionUtils.parseLong("1l").get());
        assertEquals(Long.valueOf(1), NumberExtensionUtils.parseLong("1.0").get());
        assertEquals(null, NumberExtensionUtils.parseLong("aaa").orElse(null));
    }

    @Test
    public void testParseBigDecimal() {
        final Optional<BigDecimal> bigDecimal1 = NumberExtensionUtils.parseBigDecimal("1.1");

        assertTrue(NumberExtensionUtils.anyEqual(BigDecimal.valueOf(1.1), bigDecimal1.get()));
        assertTrue(NumberExtensionUtils.anyEqual(BigDecimal.valueOf(1.1), 1.1d));
        assertTrue(NumberExtensionUtils.anyEqual(BigDecimal.valueOf(1.1), 1.1f));
        assertTrue(NumberExtensionUtils.anyEqual(BigDecimal.valueOf(1.0001), 1.0001));
        assertEquals(null, NumberExtensionUtils.parseBigDecimal("aaa").orElse(null));
    }

    @Test
    public void testIntersect() {
        Range<Integer> range1 = Range.closed(3, 5);
        Range<Integer> range2 = Range.closed(2, 4);
        Optional<Range<Integer>> intersectOptional = NumberExtensionUtils.intersect(range1, range2);
        assertTrue(intersectOptional.isPresent());
        assertEquals(Integer.valueOf(3), intersectOptional.get().lowerEndpoint());
        assertEquals(Integer.valueOf(4), intersectOptional.get().upperEndpoint());

        range2 = Range.closed(1, 3);
        intersectOptional = NumberExtensionUtils.intersect(range1, range2);
        assertTrue(intersectOptional.isPresent());
        assertEquals(Integer.valueOf(3), intersectOptional.get().lowerEndpoint());
        assertEquals(Integer.valueOf(3), intersectOptional.get().upperEndpoint());

        range2 = Range.closed(1, 2);
        intersectOptional = NumberExtensionUtils.intersect(range1, range2);
        assertFalse(intersectOptional.isPresent());

    }

    @Test
    public void testUniqueId() {
        final long l = NumberExtensionUtils.uniqueLong(Integer.MAX_VALUE, 0);
        final long diff = Long.MAX_VALUE - l;
        // 保证不碰撞
        assertTrue(diff > Integer.MAX_VALUE);
        final long minValue = NumberExtensionUtils.uniqueLong(1, 0);
        // 保证不碰撞
        assertTrue(minValue > Integer.MAX_VALUE);
    }

    @Test
    public void testGetAnother() {
        final long l = NumberExtensionUtils.uniqueLong(456, 123);
        final Optional<Integer> another = NumberExtensionUtils.getAnother(l, 123);
        assertEquals(456, (int) another.get());
    }
}
