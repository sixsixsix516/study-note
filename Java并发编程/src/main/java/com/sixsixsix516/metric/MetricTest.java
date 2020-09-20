package com.sixsixsix516.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * @author sun 2020/9/20 12:12
 */
public class MetricTest {

    // 1. 定义MetricRegistry
    private final static MetricRegistry registry = new MetricRegistry();
    // 2. 定义名为tqs的Meter
    private final static Meter requestMeter = registry.meter("tqs");
    // 3. 定义名为volume的Meter
    private final static Meter sizeMeter = registry.meter("volume");

    public static void main(String[] args) {
        // 4. 定义ConsoleReporter并且设定相关的参数
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES).build();
        // 5. 启动Reporter，每隔10秒运行一次
        reporter.start(10, TimeUnit.SECONDS);
        // 6. 提供在线服务
        for (; ; ) {
            // 7. 上传数据
            upload(new byte[current().nextInt(1000)]);
            // 8. 随机休眠
            randomSleep();
        }
    }

    // 上传数据到服务器
    private static void upload(byte[] request) {
        // 9. 对每一次的update方法调用一次mark
        requestMeter.mark();
        // 10. 对上传的数据长度进行mark
        sizeMeter.mark(request.length);
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(10));
        } catch (InterruptedException ignored) {
        }
    }
}
