package top.jadeyan.commons.stream;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 扩展 Collectors
 *
 * @author yan
 */
public final class StreamUtils {

    private StreamUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 按某个指定字段去重
     *
     * @param keyExtractor 需要去重的字段
     * @return java.util.function.Predicate<T>
     */
    @SafeVarargs
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?>... keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(getValue(t, keyExtractor), Boolean.TRUE) == null;
    }

    private static <T> String getValue(T t, Function<? super T, ?>... keyExtractor) {
        StringBuilder str = new StringBuilder();
        for (Function<? super T, ?> function : keyExtractor) {
            str.append(function.apply(t)).append(";");
        }
        return str.toString();
    }
}
