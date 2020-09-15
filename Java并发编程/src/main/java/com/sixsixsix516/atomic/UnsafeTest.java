package com.sixsixsix516.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author sun 2020/9/14 15:50
 */
public class UnsafeTest {


	public static class User {
		int age;

		public User() {
			System.out.println("构造函数");
		}

		@Override
		public String toString() {
			return "age: " + age;
		}
	}

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {

		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);

		// 绕过构造函数执行实例化对象
		User user = (User) unsafe.allocateInstance(User.class);
		unsafe.putInt(user, unsafe.objectFieldOffset(user.getClass().getDeclaredField("age")), 30);

		System.out.println(user);
	}
}
