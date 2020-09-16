package com.sixsixsix516.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sun 2020/9/16 16:19
 */
public class LockTest {

	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		// 获取锁
		lock.lock();
		try {
			// 执行业务逻辑
		} finally {
			lock.unlock();
		}

	}
}
