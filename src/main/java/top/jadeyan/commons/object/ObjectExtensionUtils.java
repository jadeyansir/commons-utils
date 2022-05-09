package top.jadeyan.commons.object;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 对象扩展工具类
 *
 * @author yan
 */
public final class ObjectExtensionUtils {

    private ObjectExtensionUtils() {
        // hide construct
    }

    /**
     * 属性相等
     *
     * @param object1              对象1
     * @param object2              对象2
     * @param getPropertyFunctions 获取属性方法
     * @param <T>                  对象泛型
     * @param <P>                  属性泛型
     * @return 属性是否相等
     */
    @SafeVarargs
    public static <T, P extends Comparable<?>> boolean propertyEquals(
            T object1, T object2, Function<T, P>... getPropertyFunctions) {
        for (Function<T, P> getPropertyFunction : getPropertyFunctions) {
            P propertyValue1 = getPropertyFunction.apply(object1);
            P propertyValue2 = getPropertyFunction.apply(object2);
            if (!internalEquals(propertyValue1, propertyValue2)) {
                // 只要有一个不一样就返回false
                return false;
            }
        }
        return true;
    }

    /**
     * 两个值相同
     *
     * @param item1 对象1
     * @param item2 对象2
     * @param <T>   泛型
     * @return 两个对象值相等
     */
    public static <T extends Comparable<T>> boolean equals(T item1, T item2) {
        if (Objects.nonNull(item1) && Objects.nonNull(item2)) {
            // 同属性一定同一个类型
            return item1.compareTo(item2) == 0;
        }
        // 两个都是null 也是相等
        return Objects.isNull(item1) && Objects.isNull(item2);
    }

    /**
     * 判断两个值是否不同
     *
     * @param item1 对象1
     * @param item2 对象2
     * @param <T>   泛型
     * @return 两个值是否不同
     */
    public static <T extends Comparable<T>> boolean notEqual(T item1, T item2) {
        return !equals(item1, item2);
    }

    /**
     * 穷举一个集合是否包含指定元素 是有顺序的
     *
     * @param source    需要穷举的集合 如果为空 返回空集合
     * @param candidate 包含指定的元素 如果为空 返回空集合
     * @param <T>       泛型
     * @return 返回包含指定元素的穷举集合
     */
    public static <T extends Comparable<T>> Collection<Collection<T>> combinationOrder(Collection<T> source, T candidate) {
        if (CollectionUtils.isEmpty(source) || Objects.isNull(candidate)) {
            return Collections.emptyList();
        }
        Collection<Collection<T>> combinationAllList = new ArrayList<>();
        for (int i = 1; i <= source.size(); i++) {
            combinationAllList.addAll(internalCombinationOrder(source, new ArrayList<>(), i, candidate));
        }
        return combinationAllList;
    }

    /**
     * 穷举集合的所有组合方式
     *
     * @param source         需要穷举的集合
     * @param tempCollection 用于存放临时的集合数据
     * @param fetchCount     遍历元素的个数
     * @param <T>            泛型
     * @return 穷举的所有组合
     */
    private static <T extends Comparable<T>> Collection<Collection<T>> internalCombinationOrder(
            Collection<T> source, Collection<T> tempCollection, int fetchCount, T candidate) {
        if (fetchCount == 1) {
            Collection<Collection<T>> eligibleCollections = new ArrayList<>();
            // 在只查一个元素的情况下，遍历剩余元素为每个临时集合生成多个满足条件的集合
            for (T ele : source) {
                List<T> collection = new ArrayList<>(tempCollection);
                collection.add(ele);
                for (T t : collection) {
                    //如果内循环里， 有一个相等的 就返回t
                    if (equals(t, candidate)) {
                        eligibleCollections.add(collection);
                    }
                }
            }
            return eligibleCollections;
        }
        fetchCount--;
        Collection<Collection<T>> result = new ArrayList<>();
        // 查多个元素时，从剩余元素中取出一个，产生多个临时集合，还需要取 count-- 个元素。
        for (int i = 0; i < source.size(); i++) {
            List<T> collection = new ArrayList<>(tempCollection);
            List<T> tempRemain = new ArrayList<>(source);
            collection.add(tempRemain.remove(i));
            result.addAll(internalCombinationOrder(tempRemain, collection, fetchCount, candidate));
        }
        return result;
    }


    private static <T extends Comparable> boolean internalEquals(T item1, T item2) {
        if (Objects.nonNull(item1) && Objects.nonNull(item2)) {
            // 同属性一定同一个类型
            return item1.compareTo(item2) == 0;
        }
        // 两个都是null 也是相等
        return Objects.isNull(item1) && Objects.isNull(item2);
    }

    /**
     * 获取属性
     *
     * @param object          对象
     * @param getPropertyFunc 获取对象属性方法
     * @param <T>             对象泛型
     * @param <P>             属性泛型
     * @return 获取属性
     */
    public static <T, P> P getProperty(T object, Function<T, P> getPropertyFunc) {
        if (Objects.isNull(object)) {
            return null;
        }
        return getPropertyFunc.apply(object);
    }

    /**
     * 获取属性
     *
     * @param object           对象
     * @param getProperty1Func 获取对象属性1方法
     * @param getProperty2Func 获取对象属性2方法
     * @param <T>              对象泛型
     * @param <P1>             第一个属性泛型
     * @param <P2>             属性泛型下的某个属性泛型
     * @return 获取属性
     */
    public static <T, P1, P2> P2 getProperty(
            T object,
            Function<T, P1> getProperty1Func,
            Function<P1, P2> getProperty2Func) {
        final P1 p1 = getProperty(object, getProperty1Func);
        return getProperty(p1, getProperty2Func);
    }

    /**
     * 获取属性
     *
     * @param object           对象
     * @param getProperty1Func 获取对象属性1方法
     * @param getProperty2Func 获取对象属性2方法
     * @param getProperty3Func 获取对象属性3方法
     * @param <T>              对象泛型
     * @param <P1>             第一个属性泛型
     * @param <P2>             属性泛型下的某个属性泛型
     * @param <P3>             属性泛型下的某个属性泛型
     * @return 获取属性
     */
    public static <T, P1, P2, P3> P3 getProperty(
            T object,
            Function<T, P1> getProperty1Func,
            Function<P1, P2> getProperty2Func,
            Function<P2, P3> getProperty3Func) {
        final P2 p2 = getProperty(object, getProperty1Func, getProperty2Func);
        return getProperty(p2, getProperty3Func);
    }

    /**
     * 如果value 有值就用value, value 为null 用 default value
     *
     * @param value        value
     * @param defaultValue default value if value is null
     * @param <T>          泛型
     * @return 如果value 有值就用value, value 为null 用 default value
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * 如果value 不为null， 则运行消费者
     *
     * @param value    值
     * @param consumer 消费者
     * @param <T>      泛型
     */
    public static <T> void ifNonNull(T value, Consumer<? super T> consumer) {
        if (Objects.nonNull(value)) {
            consumer.accept(value);
        }
    }
}
