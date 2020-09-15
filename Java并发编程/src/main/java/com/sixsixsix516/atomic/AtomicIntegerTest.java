package com.sixsixsix516.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sun 2020/9/14 10:41
 */
public class AtomicIntegerTest {
	public static void main(String[] args) {
		/// ============================ 创建 ================================
		// 初始值为0
		AtomicInteger atomicInteger = new AtomicInteger();
		// 指定初始值
		AtomicInteger atomicInteger1 = new AtomicInteger(10);
		/// ============================ 增加操作 ================================
		// 返回当前值, 然后自增
		int value = atomicInteger.getAndIncrement();
		// 先自增然后返回值
		int value2 = atomicInteger.incrementAndGet();
		// 返回当前值, 然后增加指定值
		int andAdd = atomicInteger.getAndAdd(5);
		//  atomicInteger.addAndGet()
		// 设置一个值
		atomicInteger.set(10);
		/// ============================ 减少操作 ================================
		// 返回当前值, 然后进行自减
		int value3 = atomicInteger.getAndDecrement();
		// 自减后 返回当前值
		int value4 = atomicInteger.decrementAndGet();
		/// ============================ 更新操作 ================================
		// expect为当前的值, update为更新后的值
		boolean b = atomicInteger.compareAndSet(0, 10);
		/// ============================ 函数式 ================================
		// 使用函数式
		int i1 = atomicInteger.updateAndGet((i) -> i + 100);
		// atomicInteger1.getAndUpdate()
		int i = atomicInteger.accumulateAndGet(20, Integer::sum);
		//atomicInteger.getAndAccumulate()

		AtomicInteger atomicInteger2 = new AtomicInteger(2);
		atomicInteger2.compareAndSet(atomicInteger2.get(), 10);

	}
}
