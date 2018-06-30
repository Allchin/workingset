package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * 这个是微博博文原本的测试方法。
 * JAVA 6下的方案 解决伪共享的办法是使用缓存行填充， 
 * 使一个对象占用的内存大小刚好为64bytes或它的整数倍，
 * 这样就保证了一个缓存行里不会有多个对象。 
 * 《剖析Disruptor:为什么会这么快？(三)伪共享》提供了缓存行填充的例子：
 * 
 * 
 * VolatileLong对象大小与测试时间 ：
72bytes
测试运行时间|duration = 17071217167
测试运行时间|duration = 17728960207
测试运行时间|duration = 11630214054

64 bytes
测试运行时间|duration = 11315396975
测试运行时间|duration = 11976784080

56 bytes
测试运行时间|duration = 23586602852
测试运行时间|duration = 21456538555

40 bytes
测试运行时间|duration = 23775085072

32 bytes
测试运行时间|duration = 26419435443
测试运行时间|duration = 24616188303
 * @author citi0
 *
 */
public class FalseShareJava6 implements   Runnable {
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

	/**
	 * 哈哈，矛盾了，
	 * 测试执行需要jdk 1.6
	 *  
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
 		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(VolatileLong.class).toPrintable();
		System.out.println(layout);
		 
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

	/**
	 * 填充适用于1.6
	 * @author citi0
	 * 
	 * 
	 *
	 */
	public final static class VolatileLong {
		public volatile long value = 0L;
		public long   p1,p2,p3,p4,p5, p6; // comment out
		
		
	}
}