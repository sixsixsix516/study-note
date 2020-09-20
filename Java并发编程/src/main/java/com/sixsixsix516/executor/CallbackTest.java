package com.sixsixsix516.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author sun 2020/9/20 8:55
 */
public class CallbackTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 提交一个任务,并传入 数据 ,可在future的get方法获取到 该数据
         Future<String> submit = executorService.submit(() -> {
            System.out.println("我是异步任务");
        }, "123");
        String s = submit.get();
        System.out.println(s);

        // 多个任务 返回执行最快的结果
        // invokeAny是一个阻塞方法，它会一直等待直到有一个任务完成，运行上面的程序会看到如下的结果。
        // invokeAll方法同样可用于异步处理批量的任务，但是该方法关心所有异步任务的运行，invokeAll方法同样也是阻塞方法，一直等待所有的异步任务执行结束并返回结果。
        List<Callable<Integer>> callableList = IntStream.range(0, 10).parallel().mapToObj(i ->
                (Callable<Integer>) () -> {
                    try {
                        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3));
                    } catch (InterruptedException ignored) { }
                    return i;
                }
        ).collect(Collectors.toList());

        Integer task = executorService.invokeAny(callableList);
        System.out.println("最快的任务是: " + task);
    }
}
