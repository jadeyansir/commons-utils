package top.jadeyan.commons.skywalking;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SwCollectionWrapperTest {

    @Test
    public void testForEach() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
        SwCollectionWrapper.of(list).forEach(x -> {
            long threadId = Thread.currentThread().getId();
            queue.add(threadId);
        });
        Map<Long, List<Long>> collect = queue.stream().collect(Collectors.groupingBy(x -> x));
        assertEquals(list.size(), queue.size());
        // 单线程必须只能只有一个
        assertEquals(1, collect.size());
    }

    @Test
    public void testParallelForEach() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
        SwCollectionWrapper.of(list).parallel(forkJoinPool).forEach(x -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                long threadId = Thread.currentThread().getId();
                System.out.println("run threadName: " + Thread.currentThread().getName());
                queue.add(threadId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        forkJoinPool.shutdown();
        Map<Long, List<Long>> collect = queue.stream().collect(Collectors.groupingBy(x -> x));
        assertEquals(list.size(), queue.size());
        // 并行线程数大于1
        assertTrue(collect.size() > 1);
    }

    @Test
    public void testParallelForEach2() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
        SwCollectionWrapper.of(list).parallel(executorService).forEach(x -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                long threadId = Thread.currentThread().getId();
                System.out.println("run threadName: " + Thread.currentThread().getName());
                queue.add(threadId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Map<Long, List<Long>> collect = queue.stream().collect(Collectors.groupingBy(x -> x));
        assertEquals(list.size(), queue.size());
        // 并行线程数大于1
        assertTrue(collect.size() > 1);
    }

    @Test
    public void testMap() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        List<Long> threadIdList = SwCollectionWrapper.of(list).mapToList(x -> Thread.currentThread().getId());
        Map<Long, List<Long>> collect = threadIdList.stream().collect(Collectors.groupingBy(x -> x));
        assertEquals(list.size(), threadIdList.size());
        // 单线程必须只能只有一个
        assertEquals(1, collect.size());
    }

    @Test
    public void testParallelMap() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        List<Long> threadIdList = SwCollectionWrapper.of(list).parallel(forkJoinPool).mapToList(x -> {
            try {
                System.out.println("run threadName: " + Thread.currentThread().getName());
                TimeUnit.MILLISECONDS.sleep(300);
                return Thread.currentThread().getId();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0L;
        });
        forkJoinPool.shutdown();
        Map<Long, List<Long>> collect = threadIdList.stream().collect(Collectors.groupingBy(x -> x));
        assertEquals(list.size(), threadIdList.size());
        // 并行线程数大于1
        assertTrue(collect.size() > 1);
    }

    @Test
    public void testException() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        // 应该产生异常
        assertThrows(Exception.class, () -> SwCollectionWrapper.of(list).parallel(executorService).forEach(x -> {
            if (x == 9) {
                throw new RuntimeException("throw");
            }
        }));
    }

}
