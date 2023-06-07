package top.jadeyan.commons.utils;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import top.jadeyan.commons.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.*;

import static java.lang.String.format;

/**
 * 集合操作
 *
 * @author yan
 * @date 2022-03-18
 */
public class ListUtils {

    private static final Log logger = LogFactory.getLog(ListUtils.class);
    private static final String MESSAGE = "comparator cannot be null";
    private static final List EMPTY_LIST = Collections.emptyList();

    /**
     * 根据常见业务场景提取的diff操作，
     * 将两个不同的全量集合进行diff操作，挑选出新增，更新和删除集合
     *
     * @param <T>        类型
     * @param oldList    旧集合数据
     * @param newList    新集合数据
     * @param comparator 比较器，判断新旧集合中的两个元素是否属于同一元素。<br/>
     *                   例如，根据元素的主键id判断两个元素是同一元素。
     * @return 类型
     */
    public static <T> ListDiffResult<T> diff(List<T> oldList, List<T> newList,
                                             Comparator<T> comparator) {

        // 如果oldList为空，则所有元素是新增的，没有删除和更新的
        if (CollectionUtils.isEmpty(oldList)) {
            return new ListDiffResult<>(newList, EMPTY_LIST, EMPTY_LIST);
        }
        // 如果newList为空，则所有元素是删除的，没有新增和更新元素
        if (CollectionUtils.isEmpty(newList)) {
            return new ListDiffResult<>(EMPTY_LIST, EMPTY_LIST, oldList);
        }

        Assert.notNull(comparator, MESSAGE);

        List<T> updatedList = new ArrayList<>(oldList.size());
        // 方便快速查询
        Set<T> updateSet = new HashSet<>(oldList.size());
        List<T> deletedList = new ArrayList<>(oldList.size());
        List<T> addedList = new ArrayList<>(oldList.size());

        // 获取更新元素
        for (T v : oldList) {
            // 如果找到相同的置为true,否则置为false
            boolean flag = false;
            for (T k : newList) {
                if (comparator.isSame(v, k)) {
                    updatedList.add(k);
                    updateSet.add(v);
                    updateSet.add(k);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                deletedList.add(v);
            }
        }
        // 过滤新增元素
        newList.forEach(v -> {
            if (!updateSet.contains(v)) {
                addedList.add(v);
            }
        });
        return new ListDiffResult<>(addedList, updatedList, deletedList);
    }


    /**
     * 获取集合src不在集合target中的元素。
     *
     * @param source     源集合
     * @param target     目标集合（被比较对象）
     * @param comparator 比较器，判断新旧集合中的两个元素是否属于同一元素。<br/>
     *                   例如，根据元素的主键id判断两个元素是同一元素。
     * @param <T>        类型
     * @return 类型
     */
    public static <T> List<T> notIn(List<T> source, List<T> target,
                                    Comparator<T> comparator) {
        if (CollectionUtils.isEmpty(target)) {
            return source;
        }
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        Assert.notNull(comparator, MESSAGE);

        List<T> result = new ArrayList<>(source.size());
        for (T v : source) {
            // 如果找到相同的置为true,否则置为false
            boolean flag = false;
            for (T k : target) {
                if (comparator.isSame(v, k)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                result.add(v);
            }
        }
        return result;
    }

    /**
     * 获取集合src和集合target交集。
     *
     * @param source     源集合
     * @param target     目标集合（被比较对象）
     * @param comparator 比较器，判断新旧集合中的两个元素是否属于同一元素。<br/>
     *                   例如，根据元素的主键id判断两个元素是同一元素。
     * @param <T>        类型
     * @return 类型
     */
    public static <T> List<T> intersection(List<T> source, List<T> target,
                                           Comparator<T> comparator) {
        if (CollectionUtils.isEmpty(target) || CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        Assert.notNull(comparator, MESSAGE);

        List<T> result = new ArrayList<>(source.size());
        for (T v : source) {
            for (T k : target) {
                if (comparator.isSame(v, k)) {
                    result.add(v);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 返回结果集格式
     *
     * @param <T> 类型
     */
    public static class ListDiffResult<T> {
        private List<T> addedList;
        private List<T> updatedList;
        private List<T> deletedList;

        /**
         * ListDiffResult
         *
         * @param addedList   新增集合
         * @param updatedList 更新集合
         * @param deletedList 删除集合
         */
        public ListDiffResult(List<T> addedList, List<T> updatedList, List<T> deletedList) {
            this.addedList = Collections.unmodifiableList(addedList);
            this.updatedList = Collections.unmodifiableList(updatedList);
            this.deletedList = Collections.unmodifiableList(deletedList);
        }

        public List<T> getAddedList() {
            return Collections.unmodifiableList(addedList);
        }

        public List<T> getUpdatedList() {
            return Collections.unmodifiableList(updatedList);
        }

        public List<T> getDeletedList() {
            return Collections.unmodifiableList(deletedList);
        }
    }

    /**
     * 比较两个集合元素是否相同
     *
     * @param <T> 类型
     */
    public interface Comparator<T> {
        /**
         * 比较两个集合元素是否相同
         *
         * @param t1 target
         * @param t2 source
         * @return bool
         */
        boolean isSame(T t1, T t2);
    }

    /**
     * 对list的元素按照多个属性名称排序
     *
     * @param list        排序列表
     * @param isAsc       是否升序 true:升序 false:降序
     * @param sortnameArr 排序字段
     * @param <E>         列表对象
     */
    public static <E> void sort(List<E> list, final Boolean isAsc, final String... sortnameArr) {
        list.sort((a, b) -> {
            int ret = 0;
            for (String s : sortnameArr) {
                ret = ListUtils.compareObject(s, isAsc, a, b);
                if (0 != ret) {
                    break;
                }
            }
            return ret;
        });
    }


    /**
     * 给list的每个属性都指定是升序还是降序
     *
     * @param list        排序列表
     * @param sortnameArr 排序字段名
     * @param typeArr     升序类型
     * @param <E>         列表对象
     */
    public static <E> void sort(List<E> list, final String[] sortnameArr, final Boolean[] typeArr) {
        if (sortnameArr.length != typeArr.length) {
            throw new BusinessException("属性数组元素个数和升降序数组元素个数不相等");
        }
        list.sort((a, b) -> {
            int ret = 0;
            for (int i = 0; i < sortnameArr.length; i++) {
                ret = ListUtils.compareObject(sortnameArr[i], typeArr[i], a, b);
                if (0 != ret) {
                    break;
                }
            }
            return ret;
        });
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     *
     * @param sortname 排序字段名
     * @param isAsc    是否升序
     * @param a        类型a
     * @param b        类型b
     * @param <E>      比较对象
     * @return 是否大于
     */
    private static <E> int compareObject(final String sortname, final Boolean isAsc, E a, E b) {
        int ret;
        Comparable value1 = ListUtils.getFieldValueByFieldName(a, sortname);
        Comparable value2 = ListUtils.getFieldValueByFieldName(b, sortname);
        if (value1 == null && value2 == null) {
            return 0;
        }
        if (value1 == null) {
            return 1;
        }
        if (value2 == null) {
            return -1;
        }
        if (isAsc) { //负值比较
            return value1.compareTo(value2);
        } else {
            return value2.compareTo(value1);
        }
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     *
     * @param numObj 数字对象
     * @param length 指定的长度
     * @return 字符
     */
    public static String addZero2Str(Number numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        nf.setMinimumFractionDigits(length);
        return nf.format(numObj);
    }

    /**
     * 获取指定对象的指定属性值(优先获取子类的值，子类找不到，到父类去找)
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 属性值
     */
    private static Object forceGetFieldValue(Object obj, String fieldName) {
        Object object = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();
            if (!accessible) {
                // 如果是private,protected修饰的属性，需要修改为可以访问的
                field.setAccessible(Boolean.TRUE);
                object = field.get(obj);
                // 还原private,protected属性的访问性质
                field.setAccessible(accessible);
                return object;
            }
            object = field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.info("setAccessible and getfailed:{}", e);
            throw new RuntimeException(format("setAccessible and get field %s failed: %s", object, e.getMessage()));
        }
        return object;
    }

    public static Comparable getFieldValueByFieldName(Object obj, String fieldName) {
        Comparable object = null;
        Class<?> aClass = obj.getClass();
        while (aClass != null) {
            Method method = getMethod(aClass, fieldName);
            if (Objects.nonNull(method)) {
                try {
                    object = (Comparable) method.invoke(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                aClass = aClass.getSuperclass();
            }
        }
        return object;
    }

    private static Method getMethod(Class<?> clazz, String fieldName) {
        Method method = null;
        if (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (("get" + fieldName).toLowerCase().equals(m.getName().toLowerCase())) {
                    return m;
                }
            }
        }
        return method;
    }
}
