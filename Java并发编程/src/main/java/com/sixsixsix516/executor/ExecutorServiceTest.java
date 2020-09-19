package com.sixsixsix516.executor;

import java.util.concurrent.*;

/**
 * @author sun 2020/9/19 9:13
 */
public class ExecutorServiceTest {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                20,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 提交任务并执行
        threadPoolExecutor.execute(() -> System.out.println("Hello Concurrent!"));
        // 关闭线程池
        threadPoolExecutor.shutdown();
    }
}
