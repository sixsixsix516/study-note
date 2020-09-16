package com.sixsixsix516.concurrent.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.locks.Lock;

/**
 * @author sun 2020/9/16 11:12
 */
public class Main {

	public static void main(String[] args) {
		SimplePhaser phaser = new SimplePhaser();
		StudentTask[] studentTask = new StudentTask[5];
		for (int i = 0; i < studentTask.length; i++) {
			studentTask[i] = new StudentTask(phaser);
			//注册一次表示phaser维护的线程个数
			int no = phaser.register();
			System.out.println("阶段编号是: " + no);

		}

		Thread[] threads = new Thread[studentTask.length];
		for (int i = 0; i < studentTask.length; i++) {
			threads[i] = new Thread(studentTask[i], "Student " + i);
			threads[i].start();
		}

		//等待所有线程执行结束
		for (int i = 0; i < studentTask.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Phaser has finished:" + phaser.isTerminated());
	}

}
