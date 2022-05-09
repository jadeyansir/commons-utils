package top.jadeyan.commons.object;

import com.google.common.collect.Iterators;

import java.util.Objects;
import java.util.Optional;

/**
 * 集合扩展工具
 *
 * @author yan
 */
public final class CollectionExtUtils {

    private CollectionExtUtils() {
        // hide construct
    }

    /**
     * 获取集合第一个
     *
     * @param items 集合
     * @param <T>   对象泛型
     * @return 集合第一个
     */
    public static <T> Optional<T> getFirst(Iterable<T> items) {
        if (Objects.isNull(items)) {
            return Optional.empty();
        }

        final T next = Iterators.getNext(items.iterator(), null);
        return Optional.ofNullable(next);
    }
}
