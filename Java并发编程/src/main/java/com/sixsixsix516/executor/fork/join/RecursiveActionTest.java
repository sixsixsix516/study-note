package com.sixsixsix516.executor.fork.join;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * @author sun 2020/9/20 11:21
 */
public class RecursiveActionTest extends RecursiveAction {

    private List<Integer> numbers;
    // 每个任务最多进行10个元素的计算
    private static final int THRESHOLD = 10;
    private int start;
    private int end;
    private int factor;

    private RecursiveActionTest(List<Integer> numbers, int start, int end, int factor) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
        this.factor = factor;
    }

    @Override
    protected void compute() {
        // 直接计算
        if (end - start < THRESHOLD) {
            computeDirectly();
        } else {
            // 拆分
            int middle = (end + start) / 2;
            RecursiveActionTest taskOne =
                    new RecursiveActionTest(numbers, start, middle, factor);
            RecursiveActionTest taskTwo =
                    new RecursiveActionTest(numbers, middle, end, factor);

            invokeAll(taskOne, taskTwo);
        }
    }

    private void computeDirectly() {
        for (int i = start; i < end; i++) {
            numbers.set(i, numbers.get(i) * factor);
        }
    }

    public static void main(String[] args) {
        // 随机生成数字并且存入list中
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(current().nextInt(1_000));
        }
        // 输出原始数据
        System.out.println(list);
        // 定义 ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 定义RecursiveAction
        RecursiveActionTest forkJoinTask = new RecursiveActionTest(list, 0, 10, 10);

        // 将forkJoinTask提交至ForkJoinPool
        forkJoinPool.invoke(forkJoinTask);
        System.out.println(list);
    }
}
