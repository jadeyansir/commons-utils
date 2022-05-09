package top.jadeyan.commons.mq;

import top.jadeyan.commons.json.JSON;
import top.jadeyan.commons.json.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class MqMessageUtilsTest {

    @Test
    public void testParseMessage() {
        Student s1 = new Student();
        s1.setName("tom");
        s1.setAge(20);
        String json = JSON.toJSONString(s1);
        String messageId = "AAABBBCCC";
        Optional<MqMessage<Student>> studentMqMessageOptional = MqMessageUtils.parseMessage(messageId, json, Student.class);
        assertTrue(studentMqMessageOptional.isPresent());
        assertEquals(s1.getName(), studentMqMessageOptional.get().getMessageBody().getName());
        assertEquals(s1.getAge(), studentMqMessageOptional.get().getMessageBody().getAge());
    }

    @Test
    public void testParseMessageToBatch() {
        List<Student> studentList = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("tom");
        s1.setAge(20);
        Student s2 = new Student();
        s2.setName("tom_diff_name");
        s2.setAge(20);
        studentList.add(s1);
        studentList.add(s2);
        String json = JSON.toJSONString(studentList);
        String messageId = "AAABBBCCC";
        MqMessageBatch<Student> mqMessageBatch = MqMessageUtils.parseMessageToBatch(messageId, json, Student.class,
                (s) -> String.format("%s_%s_%d", messageId, s.getName(), s.getAge()));
        // 用第一条比
        MqMessage<Student> studentMqMessage = mqMessageBatch.get(0);
        Student student = studentMqMessage.getMessageBody();
        String expectedMessageId = String.format("%s_%s_%d", messageId, student.getName(), student.getAge());
        assertEquals(expectedMessageId, studentMqMessage.getMessageId());
        assertEquals("tom", student.getName());
        assertEquals(20, (int) student.getAge());
        assertEquals(messageId, mqMessageBatch.getMessageBatchId());
    }
}
