package top.jadeyan.commons.object;

import com.fasterxml.jackson.core.type.TypeReference;
import top.jadeyan.commons.model.GenericWrapper;
import top.jadeyan.commons.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * BeanCopy 工具测试
 *
 * @author yan
 */
@SuppressWarnings("all")
public class BeanCopyUtilsTest {

    @Test
    public void testCopyProperties() {
        Student target = new Student();
        target.setName("yan");
        target.setAge(30);

        Student copy = BeanCopyUtils.copyProperties(target, Student.class);
        assertEquals(target.getName(), copy.getName());
        assertEquals(target.getAge(), copy.getAge());
    }

    @Test
    public void testCopyPropertiesWithTypeRef() {
        Student target = new Student();
        target.setName("yan");
        target.setAge(30);
        GenericWrapper<Student> studentGenericWrapper = new GenericWrapper<>();
        studentGenericWrapper.setId(1);
        studentGenericWrapper.setObject(target);
        final GenericWrapper<Student> studentGenericWrapperCopy =
                BeanCopyUtils.copyProperties(studentGenericWrapper, new TypeReference<GenericWrapper<Student>>() {
                });
        assertEquals(studentGenericWrapper.getId(), studentGenericWrapperCopy.getId());
        assertEquals(studentGenericWrapper.getObject(), studentGenericWrapperCopy.getObject());
    }
}
