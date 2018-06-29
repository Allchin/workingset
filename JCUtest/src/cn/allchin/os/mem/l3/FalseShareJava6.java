package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * JAVA 6下的方案 解决伪共享的办法是使用缓存行填充， 使一个对象占用的内存大小刚好为64bytes或它的整数倍，
 * 这样就保证了一个缓存行里不会有多个对象。 《剖析Disruptor:为什么会这么快？(三)伪共享》提供了缓存行填充的例子：
 * 
 * 结论：
 * 运行环境jre 1.6 
！！！ 看起来  jol不能再java 6 上准确的计算出来内存布局。

 * java.util.ServiceConfigurationError: com.sun.tools.attach.spi.AttachProvider: Provider sun.tools.attach.WindowsAttachProvider could not be instantiated
# WARNING: Unable to get Instrumentation. Dynamic Attach failed. You may add this JAR as -javaagent manually, or supply -Djdk.attach.allowAttachSelf
# WARNING: Unable to attach Serviceability Agent. Unable to attach even with module exceptions: [org.openjdk.jol.vm.sa.SASupportException: Sense failed., org.openjdk.jol.vm.sa.SASupportException: Sense failed., org.openjdk.jol.vm.sa.SASupportException: Sense failed.]
# Running 64-bit HotSpot VM.
# Using compressed oop with 3-bit shift.
# Using compressed klass with 3-bit shift.
# WARNING | Compressed references base/shifts are guessed by the experiment!
# WARNING | Therefore, computed addresses are just guesses, and ARE NOT RELIABLE.
# WARNING | Make sure to attach Serviceability Agent to get the reliable addresses.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.os.mem.l3.FalseShareJava6$VolatileLong object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (alignment/padding gap)                  
     16     8   long VolatileLong.value                        N/A
     24     8   long VolatileLong.p1                           N/A
     32     8   long VolatileLong.p2                           N/A
     40     8   long VolatileLong.p3                           N/A
     48     8   long VolatileLong.p4                           N/A
     56     8   long VolatileLong.p5                           N/A
     64     8   long VolatileLong.p6                           N/A
Instance size: 72 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

测试运行时间|duration = 18352832376

 
 * @author citi0
 *
 */
public class FalseShareJava6 implements FalseShare, Runnable {
	public final static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;

	private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileLong();
		}
	}

	public FalseShareJava6(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public static void main(final String[] args) throws Exception {
		//话题讨论的对象VolatileLong
		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(VolatileLong.class).toPrintable();
		System.out.println(layout);
		//
		final long start = System.nanoTime();
		runTest();
		System.out.println("测试运行时间|duration = " + (System.nanoTime() - start));
		 

	}

	private static void runTest() throws InterruptedException {
		Thread[] threads = new Thread[NUM_THREADS];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseShareJava6(i));
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
	}

	/**
	 *   消耗cpu的方法，
	 * 数组赋值
	 */
	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}

	public final static class VolatileLong {
		public volatile long value = 0L;
		public long p1, p2, p3, p4, p5, p6; // comment out
		
		
	}
}