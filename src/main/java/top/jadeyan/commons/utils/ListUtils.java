package top.jadeyan.commons.utils;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 集合操作
 *
 * @author yan
 * @date 2022-03-18
 */
public class ListUtils {

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
}
