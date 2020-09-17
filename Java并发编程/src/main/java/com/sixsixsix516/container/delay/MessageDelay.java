package com.sixsixsix516.container.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/17 16:28
 */
public class MessageDelay implements Delayed {

	/**
	 * 元素数据的内容
	 */
	private final String message;

	/**
	 * 用于计算失效的时间(单位秒)
	 */
	private final long time;

	public MessageDelay(String message, long second) {
		this.message = message;
		this.time = second + System.currentTimeMillis() / 1000;
		System.out.println(time);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long convert = unit.convert(time - System.currentTimeMillis() / 1000, TimeUnit.SECONDS);
		System.out.println("转换的时间: " + convert);
		return convert;
	}

	@Override
	public int compareTo(Delayed o) {
		return Long.compare(this.time, ((MessageDelay) o).getTime());
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "MessageDelay{" +
				"message='" + message + '\'' +
				", time=" + time +
				'}';
	}
}
