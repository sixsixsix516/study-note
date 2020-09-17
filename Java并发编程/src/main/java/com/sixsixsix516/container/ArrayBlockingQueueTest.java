package com.sixsixsix516.container;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/17 13:49
 */
public class ArrayBlockingQueueTest {

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(2);

		///================================== 阻塞 写 =========================================
		// 向队列尾部加入元素,如果满了则阻塞,知道有其他线程进行消费或对其进行中断
		arrayBlockingQueue.put(1);
		// 向尾部添加元素,如果容器已满,则在指定时间内阻塞,时间到后返回false代表元素加入失败
		boolean offer = arrayBlockingQueue.offer(2, 10, TimeUnit.SECONDS);
		///================================== 非阻塞 写 =========================================
		// 向尾部添加一个数据, 当容器满时抛出异常
		arrayBlockingQueue.add(1);
		// 向尾部添加一个数据, 当容器满时立即返回false
		arrayBlockingQueue.offer(1);

		///================================== 阻塞 读 =========================================
		// 从头部获取数据并移除,当容器空时阻塞,知道其他线程添加数据,或被中断
		Integer take = arrayBlockingQueue.take();
		// 从头部获取数据并移除,当容器为空时 指定时间内 如果还是获取不到则返回null
		arrayBlockingQueue.poll(1, TimeUnit.MILLISECONDS);
		///================================== 非阻塞 读 =========================================
		// 从头部获取数据并移除,当容器为空时 返回null
		Integer poll = arrayBlockingQueue.poll();
		// 从头部获取数据,如果容器为空则返回null
		Integer peek = arrayBlockingQueue.peek();
	}
}
