package com.sixsixsix516;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.ClassloaderProfiler;
import org.openjdk.jmh.profile.CompilerProfiler;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/12 21:03
 */
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
// 设置5个线程同时对共享资源操作
// 设置5个线程同时对共享资源操作
@Threads(5)
// 线程共享
@State(Scope.Benchmark)
public class ConcurrentHashMapBenchmark {

    private Map<Long, Long> currentMap;
    private Map<Long, Long> synchronizedMap;

    @Setup(Level.Invocation)
    public void setUp() {
        System.out.println("i am setUp");
        currentMap = new ConcurrentHashMap<>();
        synchronizedMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Benchmark
    public void testBlackHole() {
        Blackhole.consumeCPU(1 + 1);
    }


    @OperationsPerInvocation(10)
    @Benchmark
    public void testConcurrentMap() {
        this.currentMap.put(System.currentTimeMillis(), System.currentTimeMillis());
    }

    @Benchmark
    public void testSynchronizedMap() {
        this.synchronizedMap.put(System.currentTimeMillis(), System.currentTimeMillis());
    }

    @TearDown
    public void testTearDown() {
        System.out.println("i am tearDown");
    }

    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder()
                .include(ConcurrentHashMapBenchmark.class.getSimpleName())
                .addProfiler(CompilerProfiler.class)
                // 观察更详细的输出
                .verbosity(VerboseMode.EXTRA)
                .build();
        new Runner(build).run();
    }
}
