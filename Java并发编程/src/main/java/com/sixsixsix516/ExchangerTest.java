package com.sixsixsix516;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/15 14:40
 */
public class ExchangerTest {

	public static void main(String[] args) {
		// 要交换的数据是String
		Exchanger<String> exchanger = new Exchanger<>();

		new Thread(() -> {
			System.out.println("A线程启动");
			// 模拟业务的执行
			randomSleep();
			try {
				String receiveData = exchanger.exchange("我是A线程");
				System.out.println("A线程收到数据: " + receiveData);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "A").start();

		new Thread(() -> {
			System.out.println("B线程启动");
			// 模拟业务的执行
			randomSleep();
			try {
				String receiveData = exchanger.exchange("我是B线程");
				System.out.println("B线程启动收到数据: " + receiveData);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "B").start();
	}


	private static void randomSleep() {
		try {
			TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
