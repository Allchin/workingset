package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

 

/**
 * <pre>
 * 因为JAVA 7会优化掉无用的字段，可以参考《False Sharing && Java 7》。
 * 
 * 因此，JAVA 7下做缓存行填充更麻烦了，需要使用继承的办法来避免填充被优化掉，
 * 
 * 
 * 《False Sharing && Java 7》
 * Java 7与伪共享的新仇旧恨
 * http://ifeve.com/false-shareing-java-7-cn/
 * 
 * 执行结果：
 * # Running 64-bit HotSpot VM.
# Using compressed oop with 3-bit shift.
# Using compressed klass with 3-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.os.mem.l3.FalseShareJava7$VolatileLongPadding object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (alignment/padding gap)                  
     16     8   long VolatileLongPadding.p1                    N/A
     24     8   long VolatileLongPadding.p2                    N/A
     32     8   long VolatileLongPadding.p3                    N/A
     40     8   long VolatileLongPadding.p4                    N/A
     48     8   long VolatileLongPadding.p5                    N/A
     56     8   long VolatileLongPadding.p6                    N/A
Instance size: 64 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

----------------
cn.allchin.os.mem.l3.FalseShareJava7$VolatileLong object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (alignment/padding gap)                  
     16     8   long VolatileLongPadding.p1                    N/A
     24     8   long VolatileLongPadding.p2                    N/A
     32     8   long VolatileLongPadding.p3                    N/A
     40     8   long VolatileLongPadding.p4                    N/A
     48     8   long VolatileLongPadding.p5                    N/A
     56     8   long VolatileLongPadding.p6                    N/A
     64     8   long VolatileLong.value                        N/A
Instance size: 72 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

starting....
对齐的  duration = 12851950981
不齐的 duration = 22892053878

 * 
 * @author citi0
 *
 */
public class FalseShareJava7 implements Runnable {
	public static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;
	private static VolatileLongPadding[] longs;

	public FalseShareJava7(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public static void main(final String[] args) throws Exception {
		System.out.println(System.getProperties().get("java.version"));
 		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(VolatileLongPadding.class).toPrintable();
		System.out.println(layout);
		
 		System.out.println("----------------");
		layout=ClassLayout.parseClass(VolatileLong.class).toPrintable();
		System.out.println(layout);
		
		
		Thread.sleep(2);
		System.out.println("starting....");
		if (args.length == 1) {
			NUM_THREADS = Integer.parseInt(args[0]);
		}
		{
			longs = new VolatileLongPadding[NUM_THREADS];
			for (int i = 0; i < longs.length; i++) {
				longs[i] = new VolatileLongPadding();
			}
			final long start = System.nanoTime();
			runTest();
			System.out.println("对齐的  duration = " + (System.nanoTime() - start));
		}
		{
			longs = new VolatileLong[NUM_THREADS];
			for (int i = 0; i < longs.length; i++) {
				longs[i] = new VolatileLong();
			}
			final long start = System.nanoTime();
			runTest();
			System.out.println("不齐的 duration = " + (System.nanoTime() - start));
		}
		
	}

	private static void runTest() throws InterruptedException {
		Thread[] threads = new Thread[NUM_THREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseShareJava7(i));
		}
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
	}

	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].p1 = i;
		}
	}

	/**
	 * 对齐的
	 * @author renxing.zhang
	 *
	 */
	public static class VolatileLongPadding {
		public volatile long p1, p2, p3, p4, p5, p6;
	}

	/**
	 * 不齐的
	 * @author renxing.zhang
	 *
	 */
	public static class VolatileLong extends VolatileLongPadding {
		public volatile long value = 0L;
	}
}