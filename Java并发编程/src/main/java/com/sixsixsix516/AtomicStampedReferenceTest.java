package com.sixsixsix516;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author sun 2020/9/14 15:06
 */
public class AtomicStampedReferenceTest {

	public static void main(String[] args) {
		// 指定数据,指定版本号
		AtomicStampedReference<Object> objectAtomicStampedReference = new AtomicStampedReference<>(new Object(), 1);


		Object reference = objectAtomicStampedReference.getReference();
		int stamp = objectAtomicStampedReference.getStamp();

	}
}
