package com.sixsixsix516.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author sun 2020/9/16 16:50
 */
public class ReentrantReadWriteLockTest {

	/**
	 * 定义读写锁
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/**
	 * 创建读锁
	 */
	private final Lock readLock = readWriteLock.readLock();
	/**
	 * 创建写锁
	 */
	private final Lock writeLock = readWriteLock.writeLock();

	private final List<String> list = new LinkedList<>();

	/**
	 * 使用写锁进行数据同步
	 */
	public void add(String element) {
		writeLock.lock();
		try {
			list.add(element);
		} finally {
			writeLock.unlock();
		}
	}
	/**
	 * 使用读写进行数据同步
	 */
	public String take(int index) {
		readLock.lock();
		try {
			return list.get(index);
		} finally {
			readLock.unlock();
		}
	}

}
