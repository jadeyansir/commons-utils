package top.jadeyan.commons.skywalking;

import top.jadeyan.commons.exception.ThreadRuntimeException;
import org.apache.skywalking.apm.toolkit.trace.CallableWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * skywalking 线程池工作者
 *
 * @param <T> 类型
 * @author yan
 */
public final class SwThreadPoolWorker<T> {

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private final List<Supplier<? extends T>> supplierList = new ArrayList<>();

    /**
     * 构造器
     */
    public SwThreadPoolWorker() {
    }

    /**
     * 构造器
     *
     * @param executorService 线程池执行器
     */
    public SwThreadPoolWorker(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 创建对象
     *
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> SwThreadPoolWorker<T> of() {
        return new SwThreadPoolWorker<>();
    }

    /**
     * 创建对象
     *
     * @param executorService 自定义线程池
     * @param <T>             泛型
     * @return 对象
     */
    public static <T> SwThreadPoolWorker<T> of(ExecutorService executorService) {
        return new SwThreadPoolWorker<>(executorService);
    }


    /**
     * 添加工作
     *
     * @param supplier 工作
     * @return 当前对象
     */
    public SwThreadPoolWorker<T> addWork(Supplier<? extends T> supplier) {
        this.supplierList.add(supplier);
        return this;
    }

    /**
     * 添加工作
     *
     * @param runnable 工作
     * @return 当前对象
     */
    public SwThreadPoolWorker<T> addWork(Runnable runnable) {
        Supplier<T> supplier = () -> {
            runnable.run();
            return null;
        };
        this.supplierList.add(supplier);
        return this;
    }

    /**
     * 添加工作
     *
     * @param parameter 参数
     * @param func      工作
     * @param <P>       参数类型
     * @return 当前对象
     */
    public <P> SwThreadPoolWorker<T> addWork(P parameter, Function<P, T> func) {
        Supplier<T> supplier = () -> func.apply(parameter);
        this.supplierList.add(supplier);
        return this;
    }

    /**
     * 添加工作
     *
     * @param parameter 参数
     * @param consumer  工作
     * @param <P>       参数类型
     * @return 当前对象
     */
    public <P> SwThreadPoolWorker<T> addWork(P parameter, Consumer<P> consumer) {
        Supplier<T> supplier = () -> {
            consumer.accept(parameter);
            return null;
        };
        this.supplierList.add(supplier);
        return this;
    }

    /**
     * 提交工作
     *
     * @param parameter 参数
     * @param func      工作
     * @param <P>       工作输入参数类型
     * @param <R>       工作返回类型
     * @return 工作结果
     */
    public <P, R> CompletableFuture<R> submit(P parameter, Function<P, R> func) {
        Supplier<R> supplier = () -> func.apply(parameter);
        SupplierWrapper<R> supplierWrapper = new SupplierWrapper<>(supplier);
        return CompletableFuture.supplyAsync(supplierWrapper, executorService);
    }

    /**
     * 提交工作
     *
     * @param parameter 参数
     * @param consumer  工作
     * @param <P>       工作输入参数类型
     * @return 工作结果
     */
    public <P> CompletableFuture<Void> submit(P parameter, Consumer<P> consumer) {
        Supplier<Void> supplier = () -> {
            consumer.accept(parameter);
            return null;
        };
        SupplierWrapper<Void> supplierWrapper = new SupplierWrapper<>(supplier);
        return CompletableFuture.supplyAsync(supplierWrapper, executorService);
    }

    /**
     * 提交工作
     *
     * @param supplier 工作
     * @param <R>      工作返回类型
     * @return 工作结果
     */
    public <R> CompletableFuture<R> submit(Supplier<R> supplier) {
        SupplierWrapper<R> supplierWrapper = new SupplierWrapper<>(supplier);
        return CompletableFuture.supplyAsync(supplierWrapper, executorService);
    }

    /**
     * 提交工作
     *
     * @param runnable 工作
     * @return 工作结果
     */
    public CompletableFuture<Void> submit(Runnable runnable) {
        Supplier<Void> supplier = () -> {
            runnable.run();
            return null;
        };
        SupplierWrapper<Void> supplierWrapper = new SupplierWrapper<>(supplier);
        return CompletableFuture.supplyAsync(supplierWrapper, executorService);
    }

    /**
     * 执行工作
     *
     * @param cfs 参数
     */
    public void doWorks(CompletableFuture<?>... cfs) {
        CompletableFuture.allOf(cfs).join();
    }

    /**
     * 执行工作
     *
     * @return 各个工作的返回值
     */
    public List<T> doWorks() {
        try {
            List<Callable<T>> taskList = new ArrayList<>();
            for (Supplier<? extends T> supplier : supplierList) {
                CallableWrapper<T> callableWrapper = new CallableWrapper<>(supplier::get);
                taskList.add(callableWrapper);
            }
            List<Future<T>> futures = executorService.invokeAll(taskList);
            List<T> result = new ArrayList<>();
            for (Future<T> future : futures) {
                result.add(future.get());
            }
            return result;
        } catch (Exception ex) {
            throw new ThreadRuntimeException("doWorks error", ex);
        }
    }
}
