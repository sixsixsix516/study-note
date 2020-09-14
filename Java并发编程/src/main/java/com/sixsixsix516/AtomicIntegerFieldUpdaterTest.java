package com.sixsixsix516;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author sun 2020/9/14 15:33
 */
public class AtomicIntegerFieldUpdaterTest {

	public static class User {
		volatile int age;

		public int getAge() {
			return age;
		}
	}

	public static void main(String[] args) {
		AtomicIntegerFieldUpdater<User> objectAtomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
		User user = new User();
		objectAtomicIntegerFieldUpdater.addAndGet(user, 20);
		System.out.println(user.getAge());
	}
}
