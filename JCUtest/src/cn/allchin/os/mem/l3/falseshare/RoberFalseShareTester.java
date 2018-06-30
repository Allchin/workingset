package cn.allchin.os.mem.l3.falseshare;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import cn.allchin.os.mem.l3.falseshare.JCUToolsLayoutPrinter.AlingmentAtomicInteger;
import cn.allchin.os.mem.l3.falseshare.JCUToolsLayoutPrinter.ManualAlingmentAtomicInteger;

/**
 * <pre>
 * ���⣺ �ù�����ȡ��ʽ ���� (�������ù����߳�ͬʱ����) �����Լ��Ĳ�ȫAtomicInteger ,��ֹ����α���������ǲ��Ǹ����?
 * 
 class java.util.concurrent.atomic.AtomicInteger|duration = 48619365836
class java.util.concurrent.atomic.AtomicInteger|duration = 48619749790
class java.util.concurrent.atomic.AtomicInteger|duration = 48619851960
class java.util.concurrent.atomic.AtomicInteger|duration = 48620063765
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 44755974614
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 44755974614
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 44755974614
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 44760806929
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 44446576278
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 44446622931
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 44446576745
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 44446599605


 * 
 * ���� :�ȵ����̡߳�������ʱ�����
 * �������ֹ�����Ļ������ġ�����
 * 
 * @author renxing.zhang
 *
 */
public class RoberFalseShareTester {
	static int parrlSize = 4;
	static ForkJoinPool pool = new ForkJoinPool(parrlSize);

	public static void runTest(AtomicInteger shared, int max) throws InterruptedException {
		Thread.sleep(1000);

		CountDownLatch cdl = new CountDownLatch(parrlSize);
		final long start = System.nanoTime();
		for (int i = 0; i < parrlSize; i++) {
			pool.submit(new T1(shared, max, cdl, start));
		}
		cdl.await();

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

	public static class T1 extends RecursiveAction {
		int max = 0;
		CountDownLatch cdl;
		AtomicInteger[] ais = new AtomicInteger[7];
		long start = 0;

		T1(AtomicInteger shared, int max, CountDownLatch cdl, long start) {
			ais[3] = shared;
			this.max = max;
			this.cdl = cdl;
			this.start = start;

		}

		@Override
		protected void compute() {
			for (AtomicInteger ai : ais) {
				if (ai != null) {
					if (ai.incrementAndGet() > max) {
						System.out.println(ais[3].getClass() + "|duration = " + (System.nanoTime() - start));
						cdl.countDown();
						return;
					} else {
						T1 left = new T1(ais[3], max, cdl, start);
						left.fork();

					}
				}
			}

		}
	}
}
