package com.sixsixsix516.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author sun 2020/9/15 10:07
 */
public class CountDownLatchTest {

	public static void main(String[] args) throws InterruptedException {
		// 1.获得商品列表
		List<Product> productList = IntStream.rangeClosed(1, 10).mapToObj(Product::new).collect(Collectors.toList());
		// 2.分别进行计算
		CountDownLatch countDownLatch = new CountDownLatch(productList.size());
		productList.forEach(product ->
				new Thread(() -> {
					try {
						// 模拟真正的业务操作
						TimeUnit.MILLISECONDS.sleep(100);
						product.setPrice(System.currentTimeMillis());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						// 任务完成,计数器减1
						countDownLatch.countDown();
					}
				}).start()
		);
		// 3.返回结果
		// 主线程等待 直至任务全部完成
		countDownLatch.await();
		System.out.println(productList);

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
