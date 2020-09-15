package com.sixsixsix516.concurrent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author sun 2020/9/15 10:07
 */
public class CyclicBarrierTest {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		// 1.获得商品列表
		List<Product> productList = IntStream.rangeClosed(1, 10).mapToObj(Product::new).collect(Collectors.toList());
		// 定义循环屏障 , 传入分片数量
		CyclicBarrier cyclicBarrier = new CyclicBarrier(productList.size());
		List<Thread> threadList = new ArrayList<>();

		productList.forEach(product -> {
			Thread thread = new Thread(() -> {
				try {
					// 模拟真正的业务操作
					TimeUnit.MILLISECONDS.sleep(100);
					product.setPrice(System.currentTimeMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 任务完成,使当前线程进入阻塞状态,直到其他所有的子线程都结束任务的运行之后,它们才退出阻塞
					try {
						int await = cyclicBarrier.await();
						System.out.println(Thread.currentThread().getName() + ": 第" + await + "个执行完");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			threadList.add(thread);
			thread.start();
		});

		threadList.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.out.println("计算完成");
		productList.forEach(System.out::println);
	}


	static class Product {
		private int id;
		private double price;

		Product(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public double getPrice() {
			return price;
		}

		void setPrice(double price) {
			this.price = price;
		}

		@Override
		public String toString() {
			return "Product{" +
					"id=" + id +
					", price=" + price +
					'}';
		}
	}
}
