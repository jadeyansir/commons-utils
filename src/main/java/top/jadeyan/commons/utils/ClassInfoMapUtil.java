package top.jadeyan.commons.utils;


import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 单例模式
 *
 * @author yan
 */
public final class ClassInfoMapUtil {
    /**
     * 单例模式
     */
    private ConcurrentHashMap<String, Map<String, Field>> classFieldMap;

    private ClassInfoMapUtil() {
        classFieldMap = new ConcurrentHashMap<>();
    }

    private static class ClassInfoMapUtilHolder {
        private static final ClassInfoMapUtil INSTANCE = new ClassInfoMapUtil();
    }

    public static ClassInfoMapUtil getInstance() {
        return ClassInfoMapUtilHolder.INSTANCE;
    }

    /**
     * 获取类的属性集合
     *
     * @param o class的对象
     * @return 属性集合
     */
    public Map<String, Field> getClassFieldMap(Class o) {
        Assert.notNull(o, "Object must not be null --getClassFieldMap");
        Map<String, Field> result = classFieldMap.get(o.getName());
        if (result == null) {
            result = new ConcurrentHashMap<>();
            getAllField(result, o);
        }
        return result;
    }

    private void getAllField(Map<String, Field> map, Class o) {
        Field[] sourceFields = o.getDeclaredFields();
        List<Field> sourceFieldList = Arrays.asList(sourceFields);
        Map<String, Field> results = sourceFieldList.stream().collect(Collectors.toMap(x -> x.getName().toUpperCase(Locale.CHINA), a -> a));
        map.putAll(results);
        Class parentClass = o.getSuperclass();
        if (Objects.nonNull(parentClass) && (!o.isAssignableFrom(parentClass))) {
            getAllField(map, parentClass);
        }
    }
}
