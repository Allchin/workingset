package cn.allchin.os.mem.l3;

/**
 * <pre>
 * 因为JAVA 7会优化掉无用的字段，可以参考《False Sharing && Java 7》。
 * 
 * 因此，JAVA 7下做缓存行填充更麻烦了，需要使用继承的办法来避免填充被优化掉，
 * 
 * @author citi0
 *
 */
public class FalseShareJava7 implements Runnable {
	public static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;
	private static VolatileLong[] longs;

	public FalseShareJava7(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public static void main(final String[] args) throws Exception {
		Thread.sleep(10000);
		System.out.println("starting....");
		if (args.length == 1) {
			NUM_THREADS = Integer.parseInt(args[0]);
		}

		longs = new VolatileLong[NUM_THREADS];
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileLong();
		}
		final long start = System.nanoTime();
		runTest();
		System.out.println("duration = " + (System.nanoTime() - start));
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
			longs[arrayIndex].value = i;
		}
	}

	public static class VolatileLongPadding {
		public volatile long p1, p2, p3, p4, p5, p6;
	}

	public static class VolatileLong extends VolatileLongPadding {
		public volatile long value = 0L;
	}
}