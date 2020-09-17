package com.sixsixsix516.container;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sun 2020/9/17 16:20
 */
public class LinkedBlockingQueueTest {

	public static void main(String[] args) {
		// 默认无参构造器是无边界
		// 指定边界的方式创建
		LinkedBlockingQueue<Integer> integerLinkedBlockingQueue = new LinkedBlockingQueue<>(10);

		// 其他方法与ArrayBlokcingQueue类似
	}
}
