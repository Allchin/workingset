package cn.allchin.os.mem.l3;

import java.util.concurrent.atomic.AtomicInteger;

import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.AlingmentAtomicInteger;
import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.ManualAlingmentAtomicInteger;

/**
 * <pre>
 * 问题：
 * 用创建线程数组测试
 * 我们自己的补全AtomicInteger ,防止发生伪缓存现象，是不是更快点?
 * 
 *  -XX:-RestrictContended
 *  
 *  
 * 结论：手工补全的。。。貌似快一点啊 ,但不多
 * 
  第一次：
class java.util.concurrent.atomic.AtomicInteger|duration = 25267084553
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 24363670674

第二次：
class java.util.concurrent.atomic.AtomicInteger|duration = 24764621742
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 25573047363
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 22086831918

 * @author renxing.zhang
 *
 */
public class ThreadsFalseShareTester {
	public static void runTest(AtomicInteger shared, int max) throws InterruptedException {

		Thread[] threads = new T1[4];
		for (int i = 0; i < threads.length; i++) {

			threads[i] = new T1(shared, max);
		}
		Thread.sleep(1000);
		final long start = System.nanoTime();
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
		System.out.println(shared.getClass() + "|duration = " + (System.nanoTime() - start));

	}

	public static void main(String[] args) throws InterruptedException {
		// TODO 那我们利用atomic 的时候，多线程下，是不是Contented之后，更加快了呢?
		int max = Integer.MAX_VALUE / 2;
		{
			AtomicInteger shared = new AtomicInteger(0);

			runTest(shared, max);
		}
		{
			AlingmentAtomicInteger shared = new AlingmentAtomicInteger(0);
			runTest(shared, max);
		}
		{
			ManualAlingmentAtomicInteger shared = new ManualAlingmentAtomicInteger(0);
			runTest(shared, max);
		}

	}

	public static class T1 extends Thread {
		int max = 0;

		T1(AtomicInteger shared, int max) {
			ais[3] = shared;
			this.max = max;
		}

		AtomicInteger[] ais = new AtomicInteger[7];

		@Override
		public void run() {
			while (true) {
				for (AtomicInteger ai : ais) {
					if (ai != null) {
						if (ai.incrementAndGet() > max) {
							return;
						}
					}
				}
			}

		}
	}
}
