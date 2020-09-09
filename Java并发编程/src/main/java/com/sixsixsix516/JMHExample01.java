package com.sixsixsix516;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sun 2020/9/7 16:39
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHExample01 {
	private final static String DATA = "DUMMY DATA";

	private List<String> arrayList;
	private List<String> linkedList;

	@Setup(Level.Iteration)
	public void setUp() {
		this.arrayList = new ArrayList<>();
		this.linkedList = new LinkedList<>();
	}

	/**
	 * @Benchmark 标记基准测试方法
	 */
	@Benchmark
	public List<String> arrayListAdd() {
		this.arrayList.add(DATA);
		return arrayList;
	}

	@Benchmark
	public List<String> linkedListAdd() {
		this.linkedList.add(DATA);
		return this.linkedList;
	}

	public static void main(String[] args) throws RunnerException {
		final Options opts = new
				OptionsBuilder().include(JMHExample01.class.getSimpleName())
				.forks(1)
				.measurementIterations(10)
				.warmupIterations(5)
				.build();
		new Runner(opts).run();
	}

}

