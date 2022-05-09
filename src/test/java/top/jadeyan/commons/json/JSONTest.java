package top.jadeyan.commons.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONTest {

    @Test
    public void testToJSONString() {
        Student student = new Student();
        student.setName("jack");
        student.setAge(20);
        String expectedValue = "{\"name\":\"jack\",\"age\":20}";
        String result = JSON.toJSONString(student);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testParseObject() {
        String expectedValue = "{\"name\":\"jack\",\"age\":20}";
        Student student = JSON.parseObject(expectedValue, Student.class);
        assertEquals("jack", student.getName());
        assertEquals(Integer.valueOf(20), student.getAge());
    }

    @Test
    public void testParseTypeReferenceObject() {
        String inputJson = "[{\"dataList\":[{\"name\":\"jack\",\"age\":20}],\"dataType\":\"学生\"}," +
                "{\"dataList\":[{\"name\":\"Mary\",\"age\":31}],\"dataType\":\"老师\"}]";
        List<ObjectInfo<Student>> objectInfoList = JSON.parseObject(inputJson, new TypeReference<List<ObjectInfo<Student>>>() {
        });
        assertEquals(2, objectInfoList.size());
        assertEquals("学生", objectInfoList.get(0).getDataType());
        assertEquals("jack", objectInfoList.get(0).getDataList().get(0).getName());
        assertEquals(Integer.valueOf(20), objectInfoList.get(0).getDataList().get(0).getAge());
        assertEquals("老师", objectInfoList.get(1).getDataType());
        assertEquals("Mary", objectInfoList.get(1).getDataList().get(0).getName());
        assertEquals(Integer.valueOf(31), objectInfoList.get(1).getDataList().get(0).getAge());
    }

    @Test
    public void testParseByteArrayTypeReferenceObject() {
        String inputJson = "[{\"dataList\":[{\"name\":\"jack\",\"age\":20, \"sex\": \"男\"}],\"dataType\":\"学生\"}," +
                "{\"dataList\":[{\"name\":\"Mary\",\"age\":31}],\"dataType\":\"老师\"}]";
        byte[] bytes = inputJson.getBytes(StandardCharsets.UTF_8);
        List<ObjectInfo<Student>> objectInfoList =
                JSON.parseObject(bytes, StandardCharsets.UTF_8, new TypeReference<List<ObjectInfo<Student>>>() {
                });
        assertEquals(2, objectInfoList.size());
        assertEquals("学生", objectInfoList.get(0).getDataType());
        assertEquals("jack", objectInfoList.get(0).getDataList().get(0).getName());
        assertEquals(Integer.valueOf(20), objectInfoList.get(0).getDataList().get(0).getAge());
        assertEquals("老师", objectInfoList.get(1).getDataType());
        assertEquals("Mary", objectInfoList.get(1).getDataList().get(0).getName());
        assertEquals(Integer.valueOf(31), objectInfoList.get(1).getDataList().get(0).getAge());
    }

    @Test
    public void testParseArray() {
        String expectedValue = "[{\"name\":\"jack\",\"age\":20}]";
        List<Student> students = JSON.parseArray(expectedValue, Student.class);
        assertEquals("jack", students.get(0).getName());
        assertEquals(Integer.valueOf(20), students.get(0).getAge());
    }

    @Test
    public void testToJsonBytes() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Student student = new Student();
            student.setAge(20);
            student.setName("name" + i);
            students.add(student);
        }
        byte[] bytes = JSON.toJSONBytes(students, false);
        List<Student> result = JSON.parseArray(bytes, Student.class, false);
        assertEquals(students.size(), result.size());
        assertEquals(students.get(0).getName(), result.get(0).getName());
        assertEquals(students.get(0).getAge(), result.get(0).getAge());
    }

    @Test
    public void testToJsonBytesWithCompress() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Student student = new Student();
            student.setAge(20);
            student.setName("name" + i);
            students.add(student);
        }
        byte[] unCompressBytes = JSON.toJSONBytes(students, false);
        byte[] bytes = JSON.toJSONBytes(students, true);
        System.out.println("origin size: " + unCompressBytes.length + " compress size: " + bytes.length);
        List<Student> result = JSON.parseArray(bytes, Student.class, true);
        // 没压缩的长度 要比压缩的长度大
        assertTrue(unCompressBytes.length > bytes.length);
        assertEquals(students.size(), result.size());
        assertEquals(students.get(0).getName(), result.get(0).getName());
        assertEquals(students.get(0).getAge(), result.get(0).getAge());
    }

    @Test
    public void convertValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "jack");
        map.put("age", "20");
        Student student = JSON.convertValue(map, Student.class);
        System.out.println(student);
        assertTrue(student.getName().equals(map.get("name")));
    }

    @Test
    public void readTree() {
        String expectedValue = "{\"name\":\"jack\",\"age\":20}";
        JsonNode jsonNode = JSON.readTree(expectedValue);
        assertTrue(jsonNode.get("name").asText().equals("jack"));
    }
}
