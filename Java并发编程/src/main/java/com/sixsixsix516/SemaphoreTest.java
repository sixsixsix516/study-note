package com.sixsixsix516;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author sun 2020/9/15 15:04
 */
public class SemaphoreTest {

	public static void main(String[] args) {
		// 最大允许同时在线人数
		final int MAX_PERSON_LOGIN_COUNT = 10;
		LoginService loginService = new LoginService(MAX_PERSON_LOGIN_COUNT);

		IntStream.rangeClosed(0, 200).forEach(i ->
			new Thread(() -> {
				boolean login = loginService.login();
				if (!login) {
					System.out.println("登录失败,超过最大同时登录人数");
					return;
				}
				try {
					// 模拟业务操作
					try {
						TimeUnit.MILLISECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} finally {
					loginService.logout();
				}
			}, "User-" + i).start()
		);
	}


	private static class LoginService {
		private final Semaphore semaphore;

		LoginService(int maxPersonLoginCount) {
			this.semaphore = new Semaphore(maxPersonLoginCount);
		}
		boolean login() {
			// 获取许可证
			// tryAcquire为非阻塞的方法,如果获取失败则直接返回
			boolean login = semaphore.tryAcquire();
			// semaphore.acquire(); 为阻塞方法,如果获取失败则阻塞, 直到获取成功
			if (login) {
				System.out.println(Thread.currentThread().getName() + "登录成功");
			}
			return login;
		}

		void logout() {
			// 释放许可证
			semaphore.release();
			System.out.println(Thread.currentThread().getName() + "退出登录");
		}
	}
}
