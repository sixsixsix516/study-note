package com.sixsixsix516;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/15 12:58
 */
public class CyclicBarrierTest2 {

	public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
		CyclicBarrier barrier = new CyclicBarrier(11);
		for (int i = 0; i < 10; i++) {
			new Thread(new Tourist(i, barrier)).start();
		}
		barrier.await();
		System.out.println("乘客全部上车了");
		barrier.await();
		System.out.println("乘客全部下车了");

	}

	private static class Tourist implements Runnable {

		private final int touristId;
		private final CyclicBarrier cyclicBarrier;

		public Tourist(int touristId, CyclicBarrier cyclicBarrier) {
			this.touristId = touristId;
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {
			System.out.println("乘客" + touristId + "上车");
			// 模拟乘客上车时间开销
			spendSeveralSeconds();
			// 上车后等待其他同伴上车
			waitAndPrint("乘客" + touristId + "	等待其他人上车");
			// 模拟乘客下车的时间开销
			spendSeveralSeconds();
			// 下车后等待其他同伴下车
			waitAndPrint("乘客" + touristId + " 等待其他人下车");

		}

		private void waitAndPrint(String message) {
			System.out.println(message);
			try {
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}

		private void spendSeveralSeconds() {
			try {
				TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
