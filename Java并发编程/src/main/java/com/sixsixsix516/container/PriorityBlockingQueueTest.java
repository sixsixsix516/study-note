package com.sixsixsix516.container;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author sun 2020/9/17 14:38
 */
public class PriorityBlockingQueueTest {

	public static void main(String[] args) {
		// 指定初始容量,空构造器的默认初始容量是11,泛型必须是Comparable接口子类,否则抛出类型转换异常
		PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue(10);
		// 不存在阻塞写方法,因为priorityBlockingQueue是无边界的,添加方法都会自动排序一遍
		// add, offer, put
		// 不存在阻塞读方法 无边界
	}
}

