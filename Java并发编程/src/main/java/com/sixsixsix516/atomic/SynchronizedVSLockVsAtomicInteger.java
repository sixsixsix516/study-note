package com.sixsixsix516.atomic;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sun 2020/9/13 20:38
 */
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
public class SynchronizedVSLockVsAtomicInteger {

    /**
     * 线程组共享对象
     */
    @State(Scope.Group)
    public static class IntMonitor {
        private int x;
        private final Lock lock = new ReentrantLock();
        /**
         * 使用显示Lock进行共享资源同步
         */
        void lockInc() {
            lock.lock();
            try {
                x++;
            } finally {
                lock.unlock();
            }
        }
        /**
         * 使用synchronized关键字进行同步
         */
        void synInc() {
            synchronized (this) {
                x++;
            }
        }
    }
    @State(Scope.Group)
    public static class AtomicIntegerMonitor {
        private AtomicInteger x = new AtomicInteger();
        void inc() {
            x.incrementAndGet();
        }
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void syncInc(IntMonitor intMonitor) {
        intMonitor.synInc();
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void locInc(IntMonitor intMonitor) {
        intMonitor.lockInc();
    }

    @GroupThreads(10)
    @Group("atomic")
    @Benchmark
    public void atomicIntegerInc(AtomicIntegerMonitor atomicIntegerMonitor) {
        atomicIntegerMonitor.inc();
    }


    public static void main(String[] args) throws RunnerException {
        Options option = new OptionsBuilder()
                .include(SynchronizedVSLockVsAtomicInteger.class.getSimpleName())
                .timeout(TimeValue.seconds(10))
                .addProfiler(StackProfiler.class)
                .build();
        new Runner(option).run();
    }
}
