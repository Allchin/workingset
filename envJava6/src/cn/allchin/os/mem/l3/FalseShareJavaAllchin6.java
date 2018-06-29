package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * �����FalseShareJava6 �����ع�������Ϊɶ���������ô��
 * 
 * ���
 * 
cn.allchin.os.mem.l3.FalseShareJava6$Size80@100c62c8��������ʱ��|duration = 11699522647
cn.allchin.os.mem.l3.FalseShareJava6$Size72@2c79a2e7��������ʱ��|duration = 12731179490
cn.allchin.os.mem.l3.FalseShareJava6$Size64@68da4b71��������ʱ��|duration = 12366082633
cn.allchin.os.mem.l3.FalseShareJava6$Size56@6e811c88��������ʱ��|duration = 11527726797
 * @author citi0
 *
 */
public class FalseShareJavaAllchin6 implements Runnable {
	public final static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;

	private static VolatileType[] longs = new VolatileType[NUM_THREADS];

	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileType();
		}
	}

	public FalseShareJavaAllchin6(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	/**
	 * ������ì���ˣ� ����ִ����Ҫjdk 1.6
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		System.out.println(System.getProperties().get("java.version"));
		System.out.println(VM.current().details()); 
		{
			String layout = ClassLayout.parseClass(Size72.class).toPrintable();
			System.out.println(layout); 
		}
		{
			String layout = ClassLayout.parseClass(Size64.class).toPrintable();
			System.out.println(layout); 
		}
		{
			String layout = ClassLayout.parseClass(Size56.class).toPrintable();
			System.out.println(layout);  
		}
 
		String layout = ClassLayout.parseClass(Size80.class).toPrintable();
		System.out.println(layout);
		work(Size80.class);
		work(Size72.class);
		work(Size64.class);
		work(Size56.class);
	 

	}
	private static void work(Class<? extends VolatileType > clazz) throws InstantiationException, IllegalAccessException, InterruptedException{

 
		for (int i = 0; i < longs.length; i++) {
			longs[i] = clazz.newInstance();
		}

		final long start = System.nanoTime();
		runTest();
		System.out.println(longs[0] + "��������ʱ��|duration = " + (System.nanoTime() - start));

	
	}

	private static void runTest() throws InterruptedException {
		Thread[] threads = new Thread[NUM_THREADS];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseShareJavaAllchin6(i));
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
	}

	/**
	 * ����cpu�ķ����� ���鸳ֵ
	 */
	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}

	public static class VolatileType {
		public volatile long value = 0L;
	}

	/**
	 * ���������1.6
	 * 
	 * @author citi0 size 72
	 */
	public final static class Size72 extends VolatileType {

		public long p1, p2, p3, p4, p5, p6; // comment out

	}

	public final static class Size80 extends VolatileType {

		public long p0, p1, p2, p3, p4, p5, p6; // comment out

	}

	public final static class Size64 extends VolatileType {

		public long p2, p3, p4, p5, p6; // comment out
	}

	public final static class Size56 extends VolatileType {

		public long p3, p4, p5, p6; // comment out
	}
}