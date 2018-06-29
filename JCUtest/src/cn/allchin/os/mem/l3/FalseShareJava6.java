package cn.allchin.os.mem.l3;

/**
 * <pre>
 * JAVA 6下的方案 解决伪共享的办法是使用缓存行填充， 
 * 使一个对象占用的内存大小刚好为64bytes或它的整数倍，
 * 这样就保证了一个缓存行里不会有多个对象。 
 * 《剖析Disruptor:为什么会这么快？(三)伪共享》提供了缓存行填充的例子：
 

 
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

	/**
	 * 哈哈，矛盾了，
	 * 测试执行需要jdk 1.6
	 * JOL是1.8 才加入的。
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
 
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
	 */
	public final static class VolatileLong {
		public volatile long value = 0L;
		public long p1, p2, p3, p4, p5, p6; // comment out
		
		
	}
}