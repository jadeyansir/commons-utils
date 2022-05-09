package top.jadeyan.commons.skywalking;

import top.jadeyan.commons.exception.ThreadRuntimeException;
import org.apache.skywalking.apm.toolkit.trace.CallableWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * skywalking 集合封装
 *
 * @param <T> 泛型
 */
public final class SwCollectionWrapper<T> {

    private final Collection<T> collection;

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private boolean parallel;

    private SwCollectionWrapper(Collection<T> collection) {
        this.collection = new ArrayList<>(collection);
    }

    /**
     * 创建对象
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return 对象
     */
    public static <T> SwCollectionWrapper<T> of(Collection<T> collection) {
        return new SwCollectionWrapper<>(collection);
    }

    /**
     * 开启并行
     *
     * @return 当前对象
     */
    public SwCollectionWrapper<T> parallel() {
        this.parallel = true;
        return this;
    }

    /**
     * 开启并行
     *
     * @param executorService 执行器
     * @return 当前对象
     */
    public SwCollectionWrapper<T> parallel(ExecutorService executorService) {
        this.parallel = true;
        this.executorService = executorService;
        return this;
    }

    /**
     * 是否开启并行
     *
     * @return true 开启， false 未开启
     */
    public boolean isParallel() {
        return parallel;
    }

    /**
     * 迭代
     *
     * @param action 操作
     */
    public void forEach(Consumer<? super T> action) {
        if (!parallel) {
            collection.forEach(action);
            return;
        }
        try {
            List<Callable<Void>> taskList = new ArrayList<>();
            for (T t : collection) {
                // 多线程需要监控
                CallableWrapper<Void> callableWrapper = new CallableWrapper<>(() -> {
                    action.accept(t);
                    return null;
                });
                taskList.add(callableWrapper);
            }
            invokeAll(taskList);
        } catch (Exception e) {
            throw new ThreadRuntimeException("forEach error", e);
        }
    }

    /**
     * 映射到列表
     *
     * @param mapper 映射
     * @param <R>    泛型
     * @return 映射列表
     */
    public <R> List<R> mapToList(Function<? super T, ? extends R> mapper) {
        if (!parallel) {
            return collection.stream().map(mapper).collect(Collectors.toList());
        }
        try {
            List<Callable<R>> taskList = new ArrayList<>();
            for (T t : collection) {
                // 多线程需要监控
                CallableWrapper<R> callableWrapper = new CallableWrapper<>(() -> mapper.apply(t));
                taskList.add(callableWrapper);
            }
            return invokeAll(taskList);
        } catch (Exception e) {
            throw new ThreadRuntimeException("mapToListWithExecutor error", e);
        }
    }

    private <T> List<T> invokeAll(List<Callable<T>> taskList) throws InterruptedException, ExecutionException {
        List<Future<T>> futures = executorService.invokeAll(taskList);
        List<T> result = new ArrayList<>();
        for (Future<T> future : futures) {
            result.add(future.get());
        }
        return result;
    }
}
