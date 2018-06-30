package cn.allchin.os.mem.l3;

import java.util.concurrent.atomic.AtomicInteger;

import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.AlingmentAtomicInteger;
import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.ManualAlingmentAtomicInteger;

/**
 * <pre>
 * ���⣺
 * �ô����߳��������
 * �����Լ��Ĳ�ȫAtomicInteger ,��ֹ����α���������ǲ��Ǹ����?
 * 
 *  -XX:-RestrictContended
 *  
 *  
 * ���ۣ��ֹ���ȫ�ġ�����ò�ƿ�һ�㰡 ,������
 * 
  ��һ�Σ�
class java.util.concurrent.atomic.AtomicInteger|duration = 25267084553
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 24363670674

�ڶ��Σ�
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
		// TODO ����������atomic ��ʱ�򣬶��߳��£��ǲ���Contented֮�󣬸��ӿ�����?
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
