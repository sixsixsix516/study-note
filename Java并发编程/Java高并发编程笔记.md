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

###### 基准性能测试

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
###### 基本用法

1. 创建 

```java
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
}
```

###### AtomicInteger原理

```java
// 源码

// Unsafe是由C++实现的, 内部存在大量汇编CPU指令等代码, JDK实现的lock free几乎完全依赖该类
private static final Unsafe unsafe = Unsafe.getUnsafe();
// 存放value内存地址偏移量
private static final long valueOffset;
static {
    try {
        valueOffset = unsafe.objectFieldOffset
            (AtomicInteger.class.getDeclaredField("value"));
    } catch (Exception ex) { throw new Error(ex); }
}
// 具体值
private volatile int value;
```

###### compareAndSwapInt源码分析 - 	CAS算法

CAS包含3个操作数: 内存值V, 旧的预期值A, 要修改的新值B. 当且仅当预期值A与内存值V相等时,将内存值V修改为B,否则什么都不需要做

compareAndSwapInt方法是一个native方法, 提供了CAS算法的实现, AtomicInteger类中的原子性方法几乎都借助该方法实现



**疑问** 既然可以直接获取当前值, 那为什么还存在当前值与期待值不一致情况

```java
AtomicInteger atomicInteger2 = new AtomicInteger(2);
atomicInteger2.compareAndSet(atomicInteger2.get(), 10);
```

**原因** 是相对于synchronized关键字, 显示锁lock, AtomicInteger所提供的方法不具备排他性, 当线程A通过get() 方法获取了AtomicInteger 的 value后, B线程对value的修改意见顺序完成, A线程试图再次修饰的时候就会出现exceptValue与value当前值不相等情况, 这种方法也被称为乐观锁. 对数据进行修改的时候, 首先需要进行比较



###### 自旋方法 addAndGet 

由于compareAndSwapInt乐观锁的特性 , 会存在数据修改失败的情况, 但是有些时候必须保证数据的更新是成功的,比如调用 incrementAndGet, addAndGet

```java

// value 需要加的值
public final int getAndAddInt(Object atomicInteger, long valueOffset, int value) {
    int nowValue;
    do {
        // 首先获取到当前的值
        nowValue = this.getIntVolatile(atomicInteger, valueOffset);
    } 
	// 不断尝试修改, 只有当前的值等于内存中的值时 才修改成功    
    while(!this.compareAndSwapInt(atomicInteger, valueOffset, nowValue, nowValue + value));
    
    // 返回修改后的值
    return nowValue;
}
```

![getAndAddInt方法流程图](..\img\getAndSet方法流程图.png)



##### AtomicBoolean

提供原子性的读写布尔类型变量的解决方案

内部使用int存储, true是1 false是0

```java
public static void main(String[] args) {
	// 创建(默认false)
	AtomicBoolean atomicBoolean = new AtomicBoolean();
	// 指定值
	AtomicBoolean atomicBoolean1 = new AtomicBoolean(true);
	// 其他方式与AtomicInteger类似
}
```

**一个可立即返回并且推出阻塞的显示锁lock**

```java
package com.sixsixsix516;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一个可立即返回并且推出阻塞的显示锁lock
 *
 * @author sun 2020/9/14 13:11
 */
public class TryLock {

	private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

	private final ThreadLocal<Boolean> threadLocal = ThreadLocal.withInitial(() -> false);

	private boolean tryLock() {
		boolean result = atomicBoolean.compareAndSet(false, true);
		if (result) {
			threadLocal.set(true);
		}
		return result;
	}

	/**
	 * 锁的释放
	 */
	private boolean release() {
		if (threadLocal.get()) {
			threadLocal.set(false);
			return atomicBoolean.compareAndSet(true, false);
		}
		return false;
	}


	private final static Object VAL_OBJ = new Object();

	public static void main(String[] args) {
		TryLock lock = new TryLock();
		List<Object> validation = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				while (true) {
					try {
						if (lock.tryLock()) {
							System.out.println(Thread.currentThread().getName() + ": get lock");
							if (validation.size() > 1) {
								throw new IllegalStateException("validation failed");
							}
							validation.add(VAL_OBJ);
							TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
						} else {
							TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						if (lock.release()) {
							System.out.println(Thread.currentThread().getName() + ": release the lock");
							validation.remove(VAL_OBJ);
						}
					}
				}
			}).start();
		}
	}
}
```



##### AtomicReference

对象的引用是一个4字节的数字, 代表着在JVM堆内存中的引用地址, 对一个4字节数字的读取操作和写入操作本身就是原子性的



- volatile 关键字保证了线程间的可见性,当某线程操作了被volatile关键字修饰的变量,其他线程可以立即看到该共享变量的变化
- CAS算法, 对比交换算法, 由UnSafe提供, 实质上是通过CPU指令来得到保证的, CAS算法提供了一种快速失败的方法,当某线程修改已经被改变数据时会快速失败
- 当CAS算法对共享数据操作失败时,因为有自旋算法的加持,我们对共享数据的更新终究会得到计算

原子类型用自旋+CAS的无锁操作保证了共享变量的线程安全性和原子性



##### AtomicStampedReference 

在AtomicReference的基础上通过增加版本号解决了ABA问题



##### Atomic数组

1. AtomicIntegerArray
2. AtomicLongArray
3. AtomicReferenceArray



##### AtomicFieldUpdater

原子性更新对象属性

> 1.  **未被volatile**关键字修饰的属性无法被原子性的更新
> 2. 类变量无法被原子性的更新
> 3. 无法直接访问到的变量不能被原子性的更新
> 4.  final修饰的无法被原子性更新
> 5.  父类成员无法被原子性更新

只有是自己的被volatile修饰且public 不被final修饰的变量



-  AtomicIntegerFieldUpdater：原子性地更新对象的int类型属性，该属性无须被声明成AtomicInteger。
- AtomicLongFieldUpdater：原子性地更新对象的long类型属性，该属性无须被声明成AtomicLong。
- AtomicReferenceFieldUpdater：原子性地更新对象的引用类型属性，该属性无须被声明成AtomicReference<T>。

```java
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
```



##### sun.misc.Unsafe

Unsafe可以直接操作内存, 甚至可以通过汇编指令直接进行CPU操作

```java
// 获取unsafe
Field f = Unsafe.class.getDeclaredField("theUnsafe");
f.setAccessible(true);
Unsafe unsafe = (Unsafe) f.get(null);
System.out.println(unsafe);
```

1. 绕过构造函数实例化对象

   ```java
   Object o = unsafe.allocateInstance(User.class);
   ```

2. 直接修改内存数据

   ```java
   User user = (User) unsafe.allocateInstance(User.class);
   unsafe.putInt(user, unsafe.objectFieldOffset(user.getClass().getDeclaredField("age")), 30);
   
   System.out.println(user);
   ```

   



