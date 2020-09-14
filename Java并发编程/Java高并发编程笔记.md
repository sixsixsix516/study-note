#### JMH是专门用于代码微基准测试的工具集

warmup可直译为预热的意思, 在JMH中, Warmup所做的就是在基准测试代码正式度量之前,先对其进行预热,使得代码的执行时经历了类的早期优化, JVM运行期编译,JIT优化之后的最终状态. 从而能够获得代码真实的性能数据.

measurement则是真正的度量工作,在每一轮的度量中,所有的度量数据会被纳入统计中(预热数据不会纳入统计之中)

所有基准测试方法都会被JMH根据方法名的字典顺序执行



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
 // 最小,平均,最大 以及标准误差     
  (min, avg, max) = (0.010, 0.013, 0.020), stdev = 0.004
  CI (99.9%): [0.008, 0.019] (assumes normal distribution) 
 
```



#### 四大BenchmarkMode

JMH使用`@BeanchmarkMode`这个注解来声明使用哪些模式(Model.All全部模式)运行

`Mode.AverageTime` 平均响应时间 用于输出基准测试方法每调一次所耗费的时间 也就是elapsed time/operation

`Mode.Throughput` 方法吞吐量 用于输出在单位时间内可以对该方法调用多少次

`Mode.SampleTime` 时间采样 以抽样的方式来统计基准测试方法的性能结果, 它会手机所有的性能数据,并且将其分布在不同区间内

`Mode.SingleShotTime` 用于冷测试  不论是Warmup还是Measurement 在每一个批次中基准测试方法只会被执行一次, 一般情况下,我们会将warmup的批次设置为0

> BenchmarkMode既可以在class上进行注解设置，也可以在基准方法上进行注解设置，方法中设置的模式将会覆盖class注解上的设置，同样，在Options中也可以进行设置，它将会覆盖所有基准方法上的设置。



#### OutputTimeUnit

提供统计结果输出时的单位

可在方法,类上,Options中进行设置



#### 三大State

1. Thread独享State 每一个运行基准测试方法的线程都会持有一个独立的对象实例,该实例既可能是作为基准测试方法参数传入的,也可能是运行基准方法所在的宿主class,将state设置为Scope.Thread一般主要是针对非线程安全的类

```java
// 每一个运行基准方法的线程都会有独立的对象实例
@State(Scope.Thread)
// 设置5个线程运行
@Threads(5)	
```

2. 多线程共享

`@State(Scope.Benchmark)`

3. 线程组共享

`@State(Scope.Group)`



#### JMH的测试套件

1. @SetUp 在每一个基准测试方法执行前被调用, 常用于资源的初始化 , @TearDown 在基准测试方法执行之后调用,通常用于资源回收

   `@Setup(Level.Trial)` 默认配置 在所有批次前调用一次

   `@Setup(Level.Iteration)` 每一个批次前执行

   `@Setup(Level.Invocation)` 每一次度量调用一次

   



#### 禁止JVM优化

`@CompilerControl(CompilerControl.Mode.EXCLUDE)`





#### 编写正确的微基准测试

避免JVM优化 , 因为JVM优化后可能不是按照我们预想的真正代码执行,从而造成不准测量

1. 避免DCE(Dead Code Elimination )

   JVM擦去了一些上下文无关, 甚至经过计算确定压根不用到的代码, 最好每个基准方法都有返回值

2. 使用black hole

   将结果放入black hole中, 避免dead code的发生

   ```java
   @Benchmark
   public void testBlackHole() {
   	Blackhole.consumeCPU(1 + 1);
   }
   ```

3. 避免变量的折叠 Constant Floding

   以下代码会在JVM编译期间优化成一个值, 在运行期间不会计算

   ```java
   @Benchmark
   public double testConstantFloding(){
   	return x1 * x2;
   }
   ```

4. 避免循环展开

   避免或减少在基准测试方法中出现循环

5. fork用于避免profile-guided optimizations 

   将fork设置为1,代表每一次运行基准测试时都会开辟一个全新的JVM进程对齐进行测试, 多个基准测试之间则不再受干扰

   

```java
    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder()
                .include(ConcurrentHashMapBenchmark.class.getSimpleName())
                // 设置每个批次的超时时间
                .timeout(TimeValue.seconds(10))
                .build();
        new Runner(build).run();
    }
```



#### Profiler

1. StackProfiler

   可以输出线程堆栈信息, 统计程序在执行过程中的线程状态

```java
public static void main(String[] args) throws RunnerException {
    Options build = new OptionsBuilder()
            .include(ConcurrentHashMapBenchmark.class.getSimpleName())
            .addProfiler(StackProfiler.class)
            .build();
    new Runner(build).run();
}
```

2. GcProfiler

   可用于分析出基准方法中垃圾回收器在JVM每个内存空间所花费的时间

```java
public static void main(String[] args) throws RunnerException {
    Options build = new OptionsBuilder()
            .include(ConcurrentHashMapBenchmark.class.getSimpleName())
            .addProfiler(GCProfiler.class)
            .jvmArgsAppend("-Xmx128M")
            .build();
    new Runner(build).run();
}
```

3. ClassLoaderProfiler

   可以看到基准方法的执行过程中有多少类被加载和卸载, 但是考虑到在一个类加载器中同一个类只会被加载一次的情况,因此我们需要将warmup设置为0,以避免在热身阶段就已经加载了基准测试方法所需的所有类

```java
public static void main(String[] args) throws RunnerException {
	Options build = new OptionsBuilder()
		.include(ConcurrentHashMapBenchmark.class.getSimpleName())
		.addProfiler(ClassloaderProfiler.class)
		.warmupIterations(0)                
		.build();
	new Runner(build).run();
}
```

   4. CompilerProfiler

      显示在代码执行过程中JIT编译器所花费的优化时间

 ```java
      public static void main(String[] args) throws RunnerException {
          Options build = new OptionsBuilder()
                  .include(ConcurrentHashMapBenchmark.class.getSimpleName())
                  .addProfiler(CompilerProfiler.class)
                  // 观察更详细的输出
                  .verbosity(VerboseMode.EXTRA)
                  .build();
          new Runner(build).run();
      }
 ```

​      

### 并发原子类型

##### AtomicInteger

> 原子类型是无锁的,线程安全的

基准性能测试

```java

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sun 2020/9/13 20:38
 */
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
public class SynchronizedVSLockVsAtomicInteger {

    /**
     * 线程组共享对象
     */
    @State(Scope.Group)
    public static class IntMonitor {
        private int x;
        private final Lock lock = new ReentrantLock();
        /**
         * 使用显示Lock进行共享资源同步
         */
        void lockInc() {
            lock.lock();
            try {
                x++;
            } finally {
                lock.unlock();
            }
        }
        /**
         * 使用synchronized关键字进行同步
         */
        void synInc() {
            synchronized (this) {
                x++;
            }
        }
    }
    @State(Scope.Group)
    public static class AtomicIntegerMonitor {
        private AtomicInteger x = new AtomicInteger();
        void inc() {
            x.incrementAndGet();
        }
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void syncInc(IntMonitor intMonitor) {
        intMonitor.synInc();
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void locInc(IntMonitor intMonitor) {
        intMonitor.lockInc();
    }

    @GroupThreads(10)
    @Group("atomic")
    @Benchmark
    public void atomicIntegerInc(AtomicIntegerMonitor atomicIntegerMonitor) {
        atomicIntegerMonitor.inc();
    }


    public static void main(String[] args) throws RunnerException {
        Options option = new OptionsBuilder()
                .include(SynchronizedVSLockVsAtomicInteger.class.getSimpleName())
                .timeout(TimeValue.seconds(10))
                .addProfiler(StackProfiler.class)
                .build();
        new Runner(option).run();
    }
}

```

