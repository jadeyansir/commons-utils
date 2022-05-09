package top.jadeyan.commons.stream;

import top.jadeyan.commons.json.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNull;

@SuppressWarnings("squid:S2698")
public class ExtCollectorsTest {

    private final List<Student> students = new ArrayList<>();

    public ExtCollectorsTest() {
        Student student1 = new Student();
        student1.setName("Marry");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setName("Marry");
        student2.setAge(null);

        Student student3 = new Student();
        student3.setName("Marry");
        student3.setAge(30);

        students.add(student1);
        students.add(student2);
        students.add(student3);
    }

    @Test
    public void testStreamMapToNullValue() {
        for (int i = 0; i < 100; i++) {
            final Map<String, Integer> collect = students.stream()
                    .collect(ExtCollectors.toMap(Student::getName, Student::getAge, (x1, x2) -> x1));
            assertEquals(1, collect.size());
            assertEquals(20, (int) collect.get("Marry"));
        }

        for (int i = 0; i < 100; i++) {
            final Map<String, Integer> collect = students.stream()
                    .collect(ExtCollectors.toMap(Student::getName, Student::getAge, (x1, x2) -> x1 == null ? x1 : x2));
            assertEquals(1, collect.size());
            assertNull(collect.get("Marry"));
        }
    }

    @Test
    public void testParallelMapToNullValue() {
        for (int i = 0; i < 100; i++) {
            final Map<String, Integer> collect = students.parallelStream()
                    .collect(ExtCollectors.toMap(Student::getName, Student::getAge, (x1, x2) -> {
                        int useX1 = x1 != null ? x1 : 0;
                        int useX2 = x2 != null ? x2 : 0;
                        return useX1 > useX2 ? x1 : x2;
                    }));
            assertEquals(1, collect.size());
            assertEquals(30, (int) collect.get("Marry"));
        }

        for (int i = 0; i < 100; i++) {
            final Map<String, Integer> collect = students.parallelStream()
                    .collect(ExtCollectors.toMap(Student::getName, Student::getAge, (x1, x2) -> x1 == null ? x1 : x2));
            assertEquals(1, collect.size());
            assertNull(collect.get("Marry"));
        }
    }
}
