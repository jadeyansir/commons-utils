package top.jadeyan.commons.stream;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 扩展 Collectors
 *
 * @author yan
 */
@SuppressWarnings({"squid:S1188", "squid:UndocumentedApi", "squid:S3776"})
public final class ExtCollectors {

    private ExtCollectors() {
    }

    /**
     * @param keyMapper   a mapping function to produce keys
     * @param valueMapper a mapping function to produce values
     * @param <T>         the type of the input elements
     * @param <K>         the output type of the key mapping function
     * @param <U>         the output type of the value mapping function
     * @param <M>         the type of map
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys are the result of applying a key mapping function to the input
     * elements, and whose values are the result of applying a value mapping
     * function to all input elements equal to the key and combining them
     * using the merge function
     */
    public static <T, K, U, M extends Map<K, U>> Collector<T, M, M> toMap(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        return toMap(keyMapper, valueMapper, null, HashMap::new);
    }

    /**
     * @param keyMapper     a mapping function to produce keys
     * @param valueMapper   a mapping function to produce values
     * @param mergeFunction a merge function, used to resolve collisions between
     *                      values associated with the same key, as supplied
     * @param <T>           the type of the input elements
     * @param <K>           the output type of the key mapping function
     * @param <U>           the output type of the value mapping function
     * @param <M>           the type of map
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys are the result of applying a key mapping function to the input
     * elements, and whose values are the result of applying a value mapping
     * function to all input elements equal to the key and combining them
     * using the merge function
     */
    public static <T, K, U, M extends Map<K, U>> Collector<T, M, M> toMap(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction) {
        return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
    }

    /**
     * @param keyMapper     a mapping function to produce keys
     * @param valueMapper   a mapping function to produce values
     * @param mergeFunction a merge function, used to resolve collisions between
     *                      values associated with the same key, as supplied
     * @param mapSupplier   a function which returns a new, empty {@code Map} into
     *                      which the results will be inserted
     * @param <T>           the type of the input elements
     * @param <K>           the output type of the key mapping function
     * @param <U>           the output type of the value mapping function
     * @param <M>           the type of map
     * @return a {@code Collector} which collects elements into a {@code Map}
     * * whose keys are the result of applying a key mapping function to the input
     * * elements, and whose values are the result of applying a value mapping
     * * function to all input elements equal to the key and combining them
     * * using the merge function
     */
    public static <T, K, U, M extends Map<K, U>> Collector<T, M, M> toMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper,
            BinaryOperator<U> mergeFunction, Supplier<Map<K, U>> mapSupplier) {
        return new Collector<T, M, M>() {
            @Override
            public Supplier<M> supplier() {
                return () -> {
                    @SuppressWarnings("unchecked")
                    M map = (M) mapSupplier.get();
                    return map;
                };
            }

            @Override
            public BiConsumer<M, T> accumulator() {
                return (map, element) -> {
                    K key = keyMapper.apply(element);
                    U value = valueMapper.apply(element);
                    if (map.containsKey(key)) {
                        if (Objects.nonNull(mergeFunction)) {
                            final U old = map.get(key);
                            value = mergeFunction.apply(old, value);
                        } else {
                            throw new IllegalStateException("Duplicate key " + key);
                        }
                    }
                    map.put(key, value);
                };
            }

            @Override
            public BinaryOperator<M> combiner() {
                return (left, right) -> {
                    int total = left.size() + right.size();
                    if (left.size() < total) {
                        if (Objects.nonNull(mergeFunction)) {
                            return merge(left, right, mergeFunction);
                        }
                        throw new IllegalStateException("Duplicate key(s)");
                    } else {
                        left.putAll(right);
                        return left;
                    }
                };
            }

            @Override
            public Function<M, M> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
            }

            private M merge(M left, M right, BinaryOperator<U> mergeFunction) {
                for (Map.Entry<K, U> rightEntry : right.entrySet()) {
                    if (left.containsKey(rightEntry.getKey())) {
                        final U leftValue = left.get(rightEntry.getKey());
                        final U value = mergeFunction.apply(leftValue, rightEntry.getValue());
                        left.put(rightEntry.getKey(), value);
                    } else {
                        left.put(rightEntry.getKey(), rightEntry.getValue());
                    }
                }
                return left;
            }
        };
    }

}
