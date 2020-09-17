package com.sixsixsix516.container;

import java.util.concurrent.SynchronousQueue;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

/**
 * @author sun 2020/9/17 17:06
 */
public class SynchronousQueueTest {

	public static void main(String[] args) {
		// 定义String类型的SynchronousQueue
		SynchronousQueue<String> queue = new SynchronousQueue<>();
		// 启动两个线程，向queue中写入数据
		IntStream.rangeClosed(0, 1).forEach(i ->
				new Thread(() -> {
					try {
						// 若没有对应的数据消费线程，则put方法将会导致当前线程进入阻塞
						queue.put(currentThread().getName());
						System.out.println(currentThread() + " put element " + currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start());

		// 启动两个线程从queue中消费数据
		IntStream.rangeClosed(0, 1).forEach(i ->
				new Thread(() -> {
					try {
						// 若没有对应的数据生产线程，则take方法将会导致当前线程进入阻塞
						String value = queue.take();
						System.out.println(currentThread() + " take " + value);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start()
		);
	}
}
