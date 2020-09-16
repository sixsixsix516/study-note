package com.sixsixsix516.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * @author sun 2020/9/16 17:48
 */
public class ConditionTest {

    /**
     * 共享数据
     */
    private static int shareData = 0;
    /**
     * 标识共享数据是否被使用
     */
    private static boolean dataUsed = false;

    /**
     * 创建显示锁
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 使用显示锁创建Condition
     */
    private static Condition condition = lock.newCondition();

    /**
     * 对数据的写操作
     */
    private static void change() {
        // 获取锁，如果当前锁被其他线程持有，则当前线程会进入阻塞
        lock.lock();
        try {
            // ②如果当前数据未被使用，则当前线程将进入wait队列，并且释放lock
            while (!dataUsed) {
                condition.await();
            }
            // 修改数据，并且将dataUsed状态标识为false
            TimeUnit.SECONDS.sleep(current().nextInt(5));
            shareData++;
            dataUsed = false;
            System.out.println("produce the new value: " + shareData);
            // ③ 通知并唤醒在wait队列中的其他线程——数据使用线程
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 对数据进行使用
    private static void use() {
        // 获取锁，如果当前锁被其他线程持有，则当前线程会进入阻塞
        lock.lock();
        try {
            // ④ 如果当前数据已经使用，则当前线程将进入wait队列，并且释放lock
            while (dataUsed) {
                condition.await();
            }
            // 使用数据，并且将dataUsed状态标识置为true
            TimeUnit.SECONDS.sleep(current().nextInt(5));
            dataUsed = true;
            System.out.println("the shared data changed: " + shareData);
            // ⑤通知并唤醒wait队列中的其他线程——数据修改线程
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建并启动两个匿名线程
        new Thread(() -> {
            while (true) {
                change();
            }
        }, "Producer").start();
        new Thread(() -> {
            while (true) {
                use();
            }
        }, "Consumer").start();


        // 当前线程进入阻塞状态直到有其他线程对其进行唤醒,或者对当前线程进行中断操作,进入阻塞队列,放弃锁的持有
        condition.await();
        // 与await方法类似,只不过忽略线程中断
        condition.awaitUninterruptibly();
        // 增加超时时间功能
        condition.awaitNanos(1);
        // 1.增加超时功能 2.返回值用于判断是否正常唤醒
		boolean await = condition.await(1, TimeUnit.HOURS);
		// 唤醒阻塞队列中的一个线程, 阻塞队列使用先进先出方式, 第一个阻塞的第一个唤醒
		condition.signal();
		// 唤醒全部线程
		condition.signalAll();
		// 查询与condition关联的wait队列数量
		new ReentrantLock().hasWaiters(condition);
		// 查询与condition关联 是否有线程进入它的阻塞队列
		new ReentrantLock().hasWaiters(condition);

	}


}
