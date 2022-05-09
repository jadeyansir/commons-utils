package top.jadeyan.commons.skywalking;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;

public class SwThreadPoolWorkerTest {

    @Test
    public void test() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        List<Object> result = SwThreadPoolWorker.of(forkJoinPool)
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return "b";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(100);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return Thread.currentThread().getId();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return "a";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).doWorks();
        System.out.println(result);
    }

    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Object> result = SwThreadPoolWorker.of(executorService)
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return "b";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(100);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return Thread.currentThread().getId();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());
                        return "a";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .addWork(() -> {
                    try {
                        Thread.sleep(300);
                        System.out.println("thread" + Thread.currentThread().getName());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).doWorks();
        System.out.println(result);
    }

    @Test
    public void test3() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Object> result = SwThreadPoolWorker.of(executorService)
                .addWork("hello world", (input) -> {
                    System.out.println("get input: " + input + " threadId: " + Thread.currentThread().getName());
                    assertEquals("hello world", input);
                })
                .addWork(1, (input) -> input + 1)
                .doWorks();
        System.out.println(result);
    }

    @Test
    public void test4() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        SwThreadPoolWorker<Object> of = SwThreadPoolWorker.of(executorService);
        long startTime = System.currentTimeMillis();
        CompletableFuture<Integer> submit1 = of
                .submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 1;
                });
        CompletableFuture<Integer> submit2 = of
                .submit(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 2;
                });
        CompletableFuture<Integer> submit3 = of
                .submit(3, (input) -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return input;
                });
        of.doWorks(submit1, submit2, submit3);
        Integer join = submit1.join();
        Integer join2 = submit2.join();
        Integer join3 = submit3.join();
        long endTime = System.currentTimeMillis();
        long takeTime = endTime - startTime;
        System.out.println("take time: " + takeTime);
        assertEquals(1, (int) join);
        assertEquals(2, (int) join2);
        assertEquals(3, (int) join3);
        assertTrue(takeTime < 3500);
    }

    @Test
    public void test5() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        SwThreadPoolWorker<Object> of = SwThreadPoolWorker.of(executorService);
        long startTime = System.currentTimeMillis();
        CompletableFuture<Integer> submit1 = of
                .submit(() -> {
                    try {
                        System.out.println("submit1");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 1;
                });
        CompletableFuture<Integer> submit2 = of
                .submit(() -> {
                    try {
                        System.out.println("submit2");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 2;
                });
        CompletableFuture<Integer> submit3 = of
                .submit(() -> {
                    System.out.println("submit3");
                    throw new RuntimeException("test exception");
                });
        boolean hasException = false;
        try {
            of.doWorks(submit1, submit2, submit3);
            Integer join = submit1.join();
            Integer join2 = submit2.join();
            Integer join3 = submit3.join();
            long endTime = System.currentTimeMillis();
            long takeTime = endTime - startTime;
            System.out.println("take time: " + takeTime);
            assertEquals(1, (int) join);
            assertEquals(2, (int) join2);
            assertEquals(3, (int) join3);
            assertTrue(takeTime < 3500);
        } catch (Exception ex) {
            hasException = true;
        }
        if (!hasException) {
            fail();
        }

    }

    @Test
    public void testReturnSameType() {
        List<Integer> result = SwThreadPoolWorker.<List<Integer>>of()
                .addWork(() -> Arrays.asList(1, 2))
                .addWork(() -> Arrays.asList(3, 4, 5))
                .doWorks()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Assert.assertEquals(5, result.size());
    }
}
