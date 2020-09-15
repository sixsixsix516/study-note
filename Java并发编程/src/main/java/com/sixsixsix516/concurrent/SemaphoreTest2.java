package com.sixsixsix516.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/15 15:54
 */
public class SemaphoreTest2 {

	public static void main(String[] args) throws InterruptedException {
		// ====================== 初始化 ==============================
		// 1.指定许可证数量创建(不公平)
		Semaphore semaphore = new Semaphore(1);
		// 2.是否公平 true公平 这个信号量将保证争用许可证的先进先出授予
		Semaphore semaphore1 = new Semaphore(1,true);

		// ====================== 获取许可证 ==============================

		// 3.获取许可证 在Semaphore内部的可用许可证数量大于等于1的情况下，许可证将会获取成功，反之获取许可证则会失败，并且返回结果为false。
		boolean b = semaphore.tryAcquire();
		// 增加超时功能 如果超时时间内没有可用许可证,则进入阻塞状态等待许可证,如果还是没有可用,则退出返回失败 semaphore.tryAcquire(1, TimeUnit.SECONDS);
		// 4.获取指定数量的许可证
		boolean b1 = semaphore.tryAcquire(2);

		// 5. 获取一个许可证如果获取不到则阻塞一直等待着获取
		semaphore.acquire();
		// 6. 获取指定数量的许可证
		semaphore.acquire(2);


		// ====================== 释放许可证 ==============================
		// 7. 释放一个许可证
		semaphore.release();
		// 8. 释放指定数量的许可证
		semaphore.release(2);
	}
}
