package com.sixsixsix516.executor.fork.join;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author sun 2020/9/20 11:09
 */
public class RecursiveTest extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int startIndex;
    private final int endIndex;

    /**
     * 每个子任务运算的最多元素数量
     */
    private static final long THRESHOLD = 10_000L;

    private RecursiveTest(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private RecursiveTest(long[] numbers, int startIndex,
                          int endIndex) {
        this.numbers = numbers;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Long compute() {
        int length = endIndex - startIndex;
        // 当元素数量少于等于 THRESHOLD时，任务将不必再拆分
        if (length <= THRESHOLD) {
            // 直接计算
            long result = 0L;
            for (int i = startIndex; i < endIndex; i++) {
                result += numbers[i];
            }
            return result;
        }

        // 拆分任务（一分为二，被拆分后的任务有可能还会被拆分：递归）
        int tempEndIndex = startIndex + length / 2;
        // 第一个子任务
        RecursiveTest firstTask = new RecursiveTest(numbers, startIndex, tempEndIndex);
        // 异步执行第一个被拆分的子任务（子任务有可能还会被拆，这将取决于元素数量）
        firstTask.fork();
        // 拆分第二个子任务
        RecursiveTest secondTask = new RecursiveTest(numbers, tempEndIndex, endIndex);

        // 异步执行第二个被拆分的子任务（子任务有可能还会被拆，这将取决于元素数量）
        secondTask.fork();

        // join等待子任务的运算结果
        Long secondTaskResult = secondTask.join();
        Long firstTaskResult = firstTask.join();

        // 将子任务的结果相加然后返回
        return (secondTaskResult + firstTaskResult);
    }

    public static void main(String[] args) {
        // 创建一个数组
        long[] numbers = LongStream.rangeClosed(1, 9_000_000).toArray();
        // 定义RecursiveTask
        RecursiveTest forkJoinSum = new RecursiveTest(numbers);

        // 创建ForkJoinPool并提交执行RecursiveTask
        Long sum = ForkJoinPool.commonPool().invoke(forkJoinSum);

        // 输出结果
        System.out.println(sum);

        // validation result验证结果的正确性
        assert sum == LongStream.rangeClosed(1, 9_000_000).sum();
    }
}
