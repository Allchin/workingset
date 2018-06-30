package cn.allchin.os.mem.l3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.AlingmentAtomicInteger;
import cn.allchin.os.mem.l3.JCUToolsLayoutPrinter.ManualAlingmentAtomicInteger;

/**
 * <pre>
 * 问题：
 * 用pool 测试
 * 我们自己的补全AtomicInteger ,防止发生伪缓存现象，是不是更快点?
 * 
 * 
 *  
 * 第一次：
 * class java.util.concurrent.atomic.AtomicInteger|duration = 26058536422
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 26133754081
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 27514349055

    第二次：
     -XX:-RestrictContended
 
 class java.util.concurrent.atomic.AtomicInteger|duration = 27012768919
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$AlingmentAtomicInteger|duration = 25843730726
class cn.allchin.os.mem.l3.JCUToolsLayoutPrinter$ManualAlingmentAtomicInteger|duration = 26723147714

  结论:...没感觉到..
 * @author renxing.zhang
 *
 */
public class PoolFalseShareTester {
	private static ExecutorService pool=Executors.newFixedThreadPool(4);
	
	public static void runTest(AtomicInteger shared, int max) throws InterruptedException { 
		Thread.sleep(1000);
		
		
		int size=4;
		CountDownLatch cdl=new CountDownLatch(size);
		final long start = System.nanoTime();
		for(int i=0;i<size;i++){
			pool.submit(new T1(shared, max,cdl));
		} 
		cdl.await();
		 
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
		CountDownLatch cdl;
		T1(AtomicInteger shared, int max,CountDownLatch cdl) {
			ais[3] = shared;
			this.max = max;
			this.cdl=cdl;
		}

		AtomicInteger[] ais = new AtomicInteger[7];

		@Override
		public void run() {
			while (true) {
				for (AtomicInteger ai : ais) {
					if (ai != null) {
						if (ai.incrementAndGet() > max) {
							cdl.countDown();
							return;
						}
					}
				}
			}

		}
	}
}
