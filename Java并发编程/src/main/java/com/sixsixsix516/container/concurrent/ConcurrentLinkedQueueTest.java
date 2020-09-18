package com.sixsixsix516.container.concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author sun 2020/9/18 10:55
 */
public class ConcurrentLinkedQueueTest {

	public static void main(String[] args) {

		// 无锁,线程安全的, 性能高效的, 基于链表结构实现的FIFO 单向队列
		ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
		// 无锁,线程安全的, 性能高效的, 基于链表结构实现的FIFO 双向队列
		ConcurrentLinkedDeque<Integer> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();

	}
}
