package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * JAVA 6�µķ��� ���α����İ취��ʹ�û�������䣬 
 * ʹһ������ռ�õ��ڴ��С�պ�Ϊ64bytes��������������
 * �����ͱ�֤��һ���������ﲻ���ж������ 
 * ������Disruptor:Ϊʲô����ô�죿(��)α�����ṩ�˻������������ӣ�
 * 
 * 
 * VolatileLong�����С�����ʱ�� ��
72bytes
��������ʱ��|duration = 17071217167
��������ʱ��|duration = 17728960207
��������ʱ��|duration = 11630214054

64 bytes
��������ʱ��|duration = 11315396975
��������ʱ��|duration = 11976784080

56 bytes
��������ʱ��|duration = 23586602852
��������ʱ��|duration = 21456538555

40 bytes
��������ʱ��|duration = 23775085072

32 bytes
��������ʱ��|duration = 26419435443
��������ʱ��|duration = 24616188303
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
	 * ������ì���ˣ�
	 * ����ִ����Ҫjdk 1.6
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
	 * 
	 *
	 */
	public final static class VolatileLong {
		public volatile long value = 0L;
		public long   p1,p2,p3,p4,p5, p6; // comment out
		
		
	}
}