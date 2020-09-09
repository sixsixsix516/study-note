#### JMH是专门用于代码微基准测试的工具集

warmup可直译为预热的意思, 在JMH中, Warmup所做的就是在基准测试代码正式度量之前,先对其进行预热,使得代码的执行时经历了类的早期优化, JVM运行期编译,JIT优化之后的最终状态. 从而能够获得代码真实的性能数据.

measurement则是真正的度量工作,在每一轮的度量中,所有的度量数据会被纳入统计中(预热数据不会纳入统计之中)



- 设计全局的warmup和measurement

  ```java
  new OptionsBuilder()
  				// 在真正的度量之前,首先对代码进行3个批次的热身,使代码的运行达到JVM已经优化的效果
  				.warmupIterations(3)
  				// 度量的次数是5(这5次会纳入统计)
  				.measurementIterations(5);
  ```

- 在类或方法上使用@Measurement和@Warmup注解

  ```java
  @Measurement(iterations = 5) // 度量5个批次
  @Warmup(iterations = 3) // 预热3个批次
  ```

  代码编程方式配置优先于注解方式

  

#### 输出解释



```java
// JVM的版本是1.93
# JMH 1.9.3 (released 1942 days ago, please consider updating!)
// 调用的虚拟机
# VM invoker: D:\app\jdk\jdk8\jre\bin\java.exe
// 虚拟机参数
# VM options: -javaagent:D:\app\IntelliJ IDEA 2019.2.4\lib\idea_rt.jar=61077:D:\app\IntelliJ IDEA 2019.2.4\bin -Dfile.encoding=UTF-8
// 热身5个批次 每一个批次都不断调用基准测试方法, 一个批次的执行时间是1秒    
# Warmup: 5 iterations, 1 s each
// 度量10个批次 一次度量时长是1秒    
# Measurement: 10 iterations, 1 s each
// 每一个批次超时时间
# Timeout: 10 min per iteration
// 执行基准测试的线程数量    
# Threads: 1 thread, will synchronize iterations
// 基准测试的模式: 平均值时间 , 统计的是调用一次耗费的单位时间
# Benchmark mode: Average time, time/op
// 基准测试方法的绝对路径    
# Benchmark: com.sixsixsix516.JMHExample01.arrayListAdd
// 执行进度
# Run progress: 0.00% complete, ETA 00:00:30
# Fork: 1 of 1
// 执行预热的5个批次 每批次平均执行了多少微秒
# Warmup Iteration   1: 0.015 us/op
# Warmup Iteration   2: 0.016 us/op
# Warmup Iteration   3: 0.015 us/op
# Warmup Iteration   4: 0.012 us/op
# Warmup Iteration   5: 0.022 us/op
// 10次度量的情况
Iteration   1: 0.020 us/op
Iteration   2: 0.014 us/op
Iteration   3: 0.012 us/op
Iteration   4: 0.011 us/op
Iteration   5: 0.012 us/op
Iteration   6: 0.019 us/op
Iteration   7: 0.012 us/op
Iteration   8: 0.010 us/op
Iteration   9: 0.011 us/op
Iteration  10: 0.010 us/op

// 最终统计结果    
Result "arrayListAdd":
  0.013 ��(99.9%) 0.005 us/op [Average]
  (min, avg, max) = (0.010, 0.013, 0.020), stdev = 0.004
  CI (99.9%): [0.008, 0.019] (assumes normal distribution)


# JMH 1.9.3 (released 1942 days ago, please consider updating!)
# VM invoker: D:\app\jdk\jdk8\jre\bin\java.exe
# VM options: -javaagent:D:\app\IntelliJ IDEA 2019.2.4\lib\idea_rt.jar=61077:D:\app\IntelliJ IDEA 2019.2.4\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.sixsixsix516.JMHExample01.linkedListAdd

# Run progress: 50.00% complete, ETA 00:00:18
# Fork: 1 of 1
# Warmup Iteration   1: 0.390 us/op
# Warmup Iteration   2: 0.125 us/op
# Warmup Iteration   3: 0.176 us/op
# Warmup Iteration   4: 0.190 us/op
# Warmup Iteration   5: 0.065 us/op
Iteration   1: 0.181 us/op
Iteration   2: 0.052 us/op
Iteration   3: 0.170 us/op
Iteration   4: 0.048 us/op
Iteration   5: 0.173 us/op
Iteration   6: 0.047 us/op
Iteration   7: 0.162 us/op
Iteration   8: 0.047 us/op
Iteration   9: 0.171 us/op
Iteration  10: 0.047 us/op


Result "linkedListAdd":
  0.110 ��(99.9%) 0.098 us/op [Average]
  (min, avg, max) = (0.047, 0.110, 0.181), stdev = 0.065
  CI (99.9%): [0.011, 0.208] (assumes normal distribution)


# Run complete. Total time: 00:00:47

Benchmark                   Mode  Cnt  Score   Error  Units
JMHExample01.arrayListAdd   avgt   10  0.013 �� 0.005  us/op
JMHExample01.linkedListAdd  avgt   10  0.110 �� 0.098  us/op
```

