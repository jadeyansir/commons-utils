package top.jadeyan.commons.object;

import com.google.common.collect.Lists;
import top.jadeyan.commons.json.Student;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.*;

@SuppressWarnings("all")
public class ObjectExtensionUtilsTest {

    @Test
    public void testPropertyEquals1() {
        Student s1 = new Student();
        s1.setName("tom");
        s1.setAge(20);
        Student s2 = new Student();
        s2.setName("tom");
        s2.setAge(20);
        boolean result = ObjectExtensionUtils.propertyEquals(s1, s2, Student::getAge, Student::getName);
        assertTrue(result);
    }

    @Test
    public void testPropertyEquals2() {
        Student s1 = new Student();
        s1.setName("tom");
        s1.setAge(20);
        Student s2 = new Student();
        s2.setName("tom_diff_name");
        s2.setAge(20);
        boolean result = ObjectExtensionUtils.propertyEquals(s1, s2, Student::getAge, Student::getName);
        assertFalse(result);
    }

    @Test
    public void testGetProperty1() {
        Student student = new Student();
        final Integer property = ObjectExtensionUtils.getProperty(student, Student::getAge, Integer::intValue);
        assertNull(property);
    }

    @Test
    public void testGetProperty2() {
        Student student = new Student();
        student.setAge(10);
        final Integer property = ObjectExtensionUtils.getProperty(student, Student::getAge, Integer::intValue);
        assertEquals(10, property.intValue());
    }

    @Test
    public void testEquals() {
        Integer a = 1;
        Integer b = null;
        assertTrue(ObjectExtensionUtils.equals(a, 1));
        assertFalse(ObjectExtensionUtils.equals(b, 1));

        BigDecimal b1 = BigDecimal.valueOf(0.01);
        BigDecimal b2 = BigDecimal.valueOf(0.010);
        assertTrue(ObjectExtensionUtils.equals(b1, b2));
    }

    @Test
    public void testNotEqual() {
        Integer a = 1;
        Integer b = null;
        assertFalse(ObjectExtensionUtils.notEqual(a, 1));
        assertTrue(ObjectExtensionUtils.notEqual(b, 1));

        BigDecimal b1 = BigDecimal.valueOf(0.01);
        BigDecimal b2 = BigDecimal.valueOf(0.010);
        assertFalse(ObjectExtensionUtils.notEqual(b1, b2));
    }

    @Test
    public void testCombinationInteger() {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        List<List<Integer>> lists = new ArrayList<>();
        lists.add(Lists.newArrayList(1));
        lists.add(Lists.newArrayList(1, 2));
        lists.add(Lists.newArrayList(1, 3));
        lists.add(Lists.newArrayList(2, 1));
        lists.add(Lists.newArrayList(3, 1));
        lists.add(Lists.newArrayList(1, 2, 3));
        lists.add(Lists.newArrayList(1, 3, 2));
        lists.add(Lists.newArrayList(2, 1, 3));
        lists.add(Lists.newArrayList(2, 3, 1));
        lists.add(Lists.newArrayList(3, 1, 2));
        lists.add(Lists.newArrayList(3, 2, 1));
        Collection<Collection<Integer>> combination = ObjectExtensionUtils.combinationOrder(integers, 1);
        assertEquals(lists.size(), combination.size());
    }

    @Test
    public void testCombinationString() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        List<List<String>> lists = new ArrayList<>();
        lists.add(Lists.newArrayList("A"));
        lists.add(Lists.newArrayList("A", "B"));
        lists.add(Lists.newArrayList("A", "C"));
        lists.add(Lists.newArrayList("B", "A"));
        lists.add(Lists.newArrayList("C", "A"));
        lists.add(Lists.newArrayList("A", "B", "C"));
        lists.add(Lists.newArrayList("A", "C", "B"));
        lists.add(Lists.newArrayList("B", "A", "C"));
        lists.add(Lists.newArrayList("B", "C", "A"));
        lists.add(Lists.newArrayList("C", "A", "B"));
        lists.add(Lists.newArrayList("C", "B", "A"));
        Collection<Collection<String>> combination = ObjectExtensionUtils.combinationOrder(list, "A");
        assertEquals(lists.size(), combination.size());
    }

    @Test
    public void testCombinationBigDecimal() {
        List<BigDecimal> list = new ArrayList<>();
        list.add(new BigDecimal("1.0"));
        list.add(new BigDecimal("2.0"));
        list.add(new BigDecimal("3.0"));
        List<List<BigDecimal>> lists = new ArrayList<>();
        lists.add(Lists.newArrayList(new BigDecimal("2.0")));
        lists.add(Lists.newArrayList(new BigDecimal("1.0"), new BigDecimal("2.0")));
        lists.add(Lists.newArrayList(new BigDecimal("2.0"), new BigDecimal("1.0")));
        lists.add(Lists.newArrayList(new BigDecimal("2.0"), new BigDecimal("3.0")));
        lists.add(Lists.newArrayList(new BigDecimal("3.0"), new BigDecimal("2.0")));
        lists.add(Lists.newArrayList(new BigDecimal("1.0"), new BigDecimal("2.0"), new BigDecimal("3.0")));
        lists.add(Lists.newArrayList(new BigDecimal("1.0"), new BigDecimal("3.0"), new BigDecimal("2.0")));
        lists.add(Lists.newArrayList(new BigDecimal("2.0"), new BigDecimal("1.0"), new BigDecimal("3.0")));
        lists.add(Lists.newArrayList(new BigDecimal("2.0"), new BigDecimal("3.0"), new BigDecimal("1.0")));
        lists.add(Lists.newArrayList(new BigDecimal("3.0"), new BigDecimal("1.0"), new BigDecimal("2.0")));
        lists.add(Lists.newArrayList(new BigDecimal("3.0"), new BigDecimal("2.0"), new BigDecimal("1.0")));
        Collection<Collection<BigDecimal>> combination = ObjectExtensionUtils.combinationOrder(list, new BigDecimal("2.00"));
        System.out.println(combination);
        assertEquals(lists.size(), combination.size());
    }

    @Test
    public void testIfNonNull() {
        BigDecimal value = BigDecimal.valueOf(1);
        ObjectExtensionUtils.ifNonNull(value, (val) -> assertEquals(value, val));
        BigDecimal nullValue = null;
        ObjectExtensionUtils.ifNonNull(nullValue, (val) -> Assert.fail());
    }
}
