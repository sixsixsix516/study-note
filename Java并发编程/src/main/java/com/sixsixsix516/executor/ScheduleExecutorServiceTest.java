package com.sixsixsix516.executor;

import java.util.concurrent.*;

/**
 * @author sun 2020/9/19 16:07
 */
public class ScheduleExecutorServiceTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10, Executors.defaultThreadFactory());

        // 在指定时间后执行一次
        ScheduledFuture<Integer> scheduledFuture = scheduledExecutorService.schedule(() -> {
            System.out.println("1秒后执行的任务");
            return 1;
        }, 1, TimeUnit.SECONDS);
        System.out.println("执行结果: " + scheduledFuture.get());

        // 以固定的速率执行: 例三秒后 每一秒执行一次
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("基金上涨中..."), 3, 1, TimeUnit.SECONDS);

        // 以固定的速率执行, 根据上一个线程的执行情况下延迟指定的时间执行
        scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("股票上涨中..."), 3, 1, TimeUnit.SECONDS);


        // 固定线程数量,任务队列是无边界的
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 创建只有一个工作线程的线程池
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();

        // 缓存线程池 通常用于提高执行量大, 耗时短, 异步任务程序
        ExecutorService executorService2 = Executors.newCachedThreadPool();

        // 创建调度线程
        ScheduledExecutorService scheduledExecutorService1 = Executors.newScheduledThreadPool(10);

        // 并发度等于CPU核数
        ExecutorService executorService3 = Executors.newWorkStealingPool();


    }
}
