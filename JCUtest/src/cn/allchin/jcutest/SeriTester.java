package cn.allchin.jcutest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * ���work�������ԵĹ����࣬����ƽ��ͬ�����������в��ԡ�
 * 
 * @author citi0
 *
 */
public class SeriTester {
	private int threads = 4;
	private SerilzeWorker serilizeWork;

	 
	// ��ͬĿ��
	private final static int maxRound = Integer.MAX_VALUE / 2;
	private ExecutorService fixedTheadPool = null;
	/**
	 * ͬʱ��ʼ
	 */

	// ����ʱ

	private static ConcurrentHashMap<String, LongAdder> coustTimeMap = new ConcurrentHashMap<String, LongAdder>();
	private volatile static boolean allFinished = false;

	public SeriTester(Runnable[] works, int threads) {
		this.threads = threads;
		CountDownLatch cd = new CountDownLatch(threads);
		serilizeWork=new SerilzeWorker(works, cd); 
	
		fixedTheadPool = Executors.newFixedThreadPool(threads + 1);
	}

	public void doTest() {
		
		for (int i = 0; i < threads; i++) {
			fixedTheadPool.submit(serilizeWork);
		}

		fixedTheadPool.submit(new Ticker(serilizeWork));

		try {
			serilizeWork.cd.await();
			allFinished = true;
			// ִ����Ϲر�ticket
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// ����
		fixedTheadPool.shutdown();
	}

	public static String makeKey(Runnable run) {
		String key = run.getClass().getSimpleName() + "|addr|" + run.hashCode();
		return key;
	}

	public static class SerilzeWorker implements Runnable {
		Runnable[] runs = null;
		private CountDownLatch cd;
	 
		private   LongAdder  process = new LongAdder();
		
		SerilzeWorker(Runnable[] runs, CountDownLatch cd) {
			this.runs = runs;
			for (Runnable run : runs) {
				String key = makeKey(run);
				if (coustTimeMap.get(key) == null) {
					coustTimeMap.putIfAbsent(key, new LongAdder());
				}
			}
			this.cd = cd;
		}

		@Override
		public void run() { 
			 
			while(process.intValue()<maxRound) {
				process.increment(); 
				for (Runnable run : runs) {
					long start = System.currentTimeMillis();
					for( int i=0;i<100000;i++) {
						run.run();
					}
					
					long during = System.currentTimeMillis() - start;
					coustTimeMap.get(makeKey(run)).add(during);
					//
					if(during==0) {
						System.out.println(during);
					}
					
				}
			}
			cd.countDown();
		}

	}

	public static class Ticker implements Runnable {
		private SerilzeWorker lastWorks;
		 

		public Ticker(SerilzeWorker works ) {
			 
			this.lastWorks = works;
		}

		@Override
		public void run() {
			while (!allFinished) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}

				System.out.println("target|"+maxRound+"|" + makeKey(lastWorks) + "last work|����|" + lastWorks.process.intValue());
				System.out.println("ticker1|�����ʱ|" + "|" + coustTimeMap);
				

			}
			

		}

	}
}
