package com.sixsixsix516.concurrent;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author sun 2020/9/15 20:28
 */
public class PhaserTest2 {

    public static void main(String[] args) throws InterruptedException {
        // 定义一个分片为0的phaser
        MyPhaser phaser = new MyPhaser(() -> {
            System.out.println("全部元素已到达");
        });

        IntStream.range(0, 10).forEach(i ->
                new Thread(() -> {
                    // 注册后 parties+1
                    phaser.register();
                    try {
                        // 随机休眠20秒 模拟业务耗时
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                        // 等待所有线程 arrive后继续前行
                        phaser.arriveAndAwaitAdvance();
                        System.out.println(Thread.currentThread().getName() + "完成了工作");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, "T" + i).start());
    }

    static class MyPhaser extends Phaser {

        private Runnable runnable;

        MyPhaser(Runnable runnable) {
            super();
            this.runnable = runnable;
        }
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            this.runnable.run();
            return super.onAdvance(phase, registeredParties);
        }
    }
}
