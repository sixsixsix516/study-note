package com.sixsixsix516.executor;

import java.util.concurrent.*;

/**
 * @author sun 2020/9/19 19:20
 */
public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> submit = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "result";
        });

        System.out.println("todo something");
        System.out.println("返回值: " + submit.get());
        System.out.println("todo something2");
    }
}
