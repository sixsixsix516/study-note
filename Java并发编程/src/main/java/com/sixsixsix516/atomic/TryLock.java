package com.sixsixsix516.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一个可立即返回并且推出阻塞的显示锁lock
 *
 * @author sun 2020/9/14 13:11
 */
public class TryLock {

	private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

	private final ThreadLocal<Boolean> threadLocal = ThreadLocal.withInitial(() -> false);

	private boolean tryLock() {
		boolean result = atomicBoolean.compareAndSet(false, true);
		if (result) {
			threadLocal.set(true);
		}
		return result;
	}

	/**
	 * 锁的释放
	 */
	private boolean release() {
		if (threadLocal.get()) {
			threadLocal.set(false);
			return atomicBoolean.compareAndSet(true, false);
		}
		return false;
	}


	private final static Object VAL_OBJ = new Object();

	public static void main(String[] args) {
		TryLock lock = new TryLock();
		List<Object> validation = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				while (true) {
					try {
						if (lock.tryLock()) {
							System.out.println(Thread.currentThread().getName() + ": get lock");
							if (validation.size() > 1) {
								throw new IllegalStateException("validation failed");
							}
							validation.add(VAL_OBJ);
							TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
						} else {
							TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						if (lock.release()) {
							System.out.println(Thread.currentThread().getName() + ": release the lock");
							validation.remove(VAL_OBJ);
						}
					}
				}
			}).start();
		}
	}


}
