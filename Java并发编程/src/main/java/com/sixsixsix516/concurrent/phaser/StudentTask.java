package com.sixsixsix516.concurrent.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/16 11:11
 */
public class StudentTask implements Runnable {

	private Phaser phaser;

	public StudentTask(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "到达考试");
		phaser.arriveAndAwaitAdvance();

		System.out.println(Thread.currentThread().getName() + "做第1题时间...");
		doExercise1();
		System.out.println(Thread.currentThread().getName() + "做第1题完成...");
		phaser.arriveAndAwaitAdvance();

		System.out.println(Thread.currentThread().getName() + "做第2题时间...");
		doExercise2();
		System.out.println(Thread.currentThread().getName() + "做第2题完成...");
		phaser.arriveAndAwaitAdvance();

		System.out.println(Thread.currentThread().getName() + "做第3题时间...");
		doExercise3();
		System.out.println(Thread.currentThread().getName() + "做第3题完成...");
		phaser.arriveAndAwaitAdvance();

		// 测试
		int i = phaser.bulkRegister(2);

	}


	private void doExercise1() {
		long duration = (long) (Math.random() * 10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise2() {
		long duration = (long) (Math.random() * 10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise3() {
		long duration = (long) (Math.random() * 10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
