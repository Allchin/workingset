package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 这个例子在java 6上运行应该很好，
 * 但是在java 7 上应该运行不好。
 * 我们测试一下：
 * 
 *Instance size: 72 bytes
 *duration = 12364228644
 *
 * 64 bytes
 * 测试运行时间|duration = 11013883459
 * 
 * 56bytes
     测试运行时间|duration = 21582726922


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
	 
		System.out.println(System.getProperties().get("java.version"));
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
		public long   p1,p2,p3,   p6; // comment out
		
		
	}
}