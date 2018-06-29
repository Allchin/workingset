package cn.allchin.os.mem.l3;

/**
 * <pre>
 * JAVA 6�µķ��� ���α����İ취��ʹ�û�������䣬 
 * ʹһ������ռ�õ��ڴ��С�պ�Ϊ64bytes��������������
 * �����ͱ�֤��һ���������ﲻ���ж������ 
 * ������Disruptor:Ϊʲô����ô�죿(��)α�����ṩ�˻������������ӣ�
 

 
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
	 * ������ì���ˣ�
	 * ����ִ����Ҫjdk 1.6
	 * JOL��1.8 �ż���ġ�
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
 
		final long start = System.nanoTime();
		runTest();
		System.out.println("��������ʱ��|duration = " + (System.nanoTime() - start));
		 

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
	 *   ����cpu�ķ�����
	 * ���鸳ֵ
	 */
	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}

	/**
	 * ���������1.6
	 * @author citi0
	 *
	 */
	public final static class VolatileLong {
		public volatile long value = 0L;
		public long p1, p2, p3, p4, p5, p6; // comment out
		
		
	}
}