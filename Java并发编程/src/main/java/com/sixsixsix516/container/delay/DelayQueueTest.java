package com.sixsixsix516.container.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * @author sun 2020/9/17 16:34
 */
public class DelayQueueTest {

	public static void main(String[] args) throws InterruptedException {
		DelayQueue<MessageDelay> delayQueue = new DelayQueue<>();
		// 1秒后过期
		delayQueue.put(new MessageDelay("data1", 2));
		// 3秒后过期
		delayQueue.put(new MessageDelay("data2", 5));

		// 非阻塞读方法,时间未到则立即返回null
		MessageDelay poll = delayQueue.poll();

		// 阻塞读方法, 时间未到则一直阻塞
		MessageDelay take = delayQueue.take();

		// 其他方法与ArrayBlockingQueue类似
		System.out.println();
	}
}
