package com.sixsixsix516.executor;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sun 2020/9/19 15:51
 */
public class MyThreadFactory implements ThreadFactory {

    private final String PREFIX = "SUN";
    private final AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {

        ThreadGroup threadGroup = new ThreadGroup("order");
        Thread thread = new Thread(threadGroup, r, PREFIX + atomicInteger.incrementAndGet());
        // 线程优先级
        thread.setPriority(10);
        return thread;
    }
}
