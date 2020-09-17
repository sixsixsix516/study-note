package com.sixsixsix516.container;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/17 17:53
 */
public class TransferQueueTest {

	public static void main(String[] args) throws InterruptedException {
		// 定义LinkedTransferQueue
		LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

		// 通过不同的方法在队列尾部插入三个数据元素
		queue.add("hello");
		queue.offer("world");
		queue.put("Java");

		// 此时该队列的数据元素为(队尾)Java->world->hello
		new Thread(() -> {
			try {
				// 创建匿名线程，并且执行transfer方法
				queue.transfer("Alex");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("current thread exit.");
		}).start();
		// 此刻队列的数据元素为（队尾）Alex->Java->world->hello
		TimeUnit.SECONDS.sleep(2);
		// 执行take方法从队列头部移除消费元素hello,但是匿名线程仍旧被阻塞
		System.out.println(queue.take());
		// 在队尾插入新的数据元素（队尾）Scala->Alex->Java->world
		queue.put("Scala");
		// 执行poll方法从队列头部移除消费元素world,匿名线程继续被阻塞
		System.out.println(queue.poll());
		// 执行take方法从队列头部移除消费元素Java,匿名线程继续阻塞中
		System.out.println(queue.take());
		// 执行take方法从队列头部移除消费元素Alex,匿名线程退出阻塞
		System.out.println(queue.take());
	}
}
