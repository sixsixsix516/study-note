package com.sixsixsix516.concurrent;

import java.util.concurrent.locks.StampedLock;

/**
 * 替代ReentrantLock
 *
 * @author sun 2020/9/17 10:08
 */
public class StampedLockTest {

	/**
	 * 共享数据
	 */
	private static int shareData = 0;

	/**
	 * 定义锁
	 */
	private static StampedLock stampedLock = new StampedLock();

	/**
	 * 修改数据
	 */
	public static void inc() {
		// 获得锁并记录时间戳
		long stamp = stampedLock.writeLock();
		try {
			// 模拟修改共享数据
			shareData++;
		} finally {
			// 释放锁
			stampedLock.unlockWrite(stamp);
		}
	}

	/**
	 * 读取数据
	 */
	public static int get() {
		long stamp = stampedLock.readLock();
		try {
			return shareData;
		} finally {
			stampedLock.unlockRead(stamp);
		}
	}


	/**
	 * 乐观读
	 */
	public static int optimisticRead() {
		// 首先认为数据未被修改,非阻塞方法
		long stamp = stampedLock.tryOptimisticRead();
		if (!stampedLock.validate(stamp)) {
			// 如果数据被修改过,才加锁读
			stamp = stampedLock.readLock();
			try {
				return shareData;
			} finally {
				stampedLock.unlockRead(stamp);
			}
		}
		return shareData;
	}

}
