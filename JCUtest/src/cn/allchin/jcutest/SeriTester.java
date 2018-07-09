package cn.allchin.jcutest;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	/**
	 * ����ִ�д���
	 */
	private static int dftCallTimes = 2;

	// ��ͬĿ��
	private final static int maxRound = Integer.MAX_VALUE / 2;
	private ExecutorService fixedTheadPool = null;
	/**
	 * ͬʱ��ʼ
	 */

	// ����ʱ

	private static ConcurrentHashMap<String, LongAdder> coustTimeMap = new ConcurrentHashMap<String, LongAdder>();
	private static ConcurrentHashMap<String, BigDecimal> tpsMap = new ConcurrentHashMap<String, BigDecimal>();
	private volatile static boolean allFinished = false;

	public SeriTester(Runnable[] works, int threads, int callTimes) {

		this(works, threads);
		this.dftCallTimes = callTimes;

	}

	public SeriTester(Runnable[] works, int threads) {
		this.threads = threads;
		CountDownLatch cd = new CountDownLatch(threads);
		serilizeWork = new SerilzeWorker(works, cd);

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

		private LongAdder process = new LongAdder();

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

			/**
			 * ÿ�ε��÷������ٴΣ� ����Ҫ����1 Millis,���ܲ���ʱ��Ҫ�������۲�during,callTimes����������
			 * 
			 * Ϊʲô�� 67108864 �� ����ÿ������ִ�ж���һ�������������ĳ���runִ�ж��ٴλᳬ��1000ms���Ƕ���
			 */
			long callTimes = dftCallTimes;
			while (process.intValue() < maxRound) {
				process.increment();

				boolean findZero = false;
				for (Runnable run : runs) {
					long start = System.currentTimeMillis();
					for (long i = 0; i < callTimes; i++) {
						run.run();
					}

					long during = System.currentTimeMillis() - start;
					
					if (during < 1000) {
						/**
						 * ��¼ִ��ʱ�䣬���ִ��callTime�˶����ĵ�ʱ���100 ms
						 * ʱ�䵥λС����ʵ�������Ƚϴ�,���ǼӴ�����
						 * 
						 * ΪʲôҪִ�г��ȳ���1000ms��ֵ�� ��Ҫ�����μ��㹫ʽ������tpsֵ�������������ǧ��֮һ
						 */
						 
						findZero = true;
					}
					else{
					 
						String key = makeKey(run);
						coustTimeMap.get(key).add(during);

						BigDecimal tpsBig = new BigDecimal(callTimes).multiply(BigDecimal.valueOf(1000)).setScale(4)
								.divide(new BigDecimal(during), RoundingMode.HALF_UP);

						 
						tpsMap.put(key, tpsBig); 
					} 

				}
				if (findZero) {
					// �´�ִ��2���Ĺ�����
					callTimes = callTimes * 2;
					System.out.println("����callTimes|" + callTimes);
				}
			}
			cd.countDown();
		}

	}

	public static class Ticker implements Runnable {
		private SerilzeWorker lastWorks;

		public Ticker(SerilzeWorker works) {

			this.lastWorks = works;
		}

		@Override
		public void run() {
			while (!allFinished) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}

				System.out.println("target|" + maxRound + "|" + makeKey(lastWorks) + "last work|����|"
						+ lastWorks.process.intValue());
				// System.out.println("ticker1|�����ʱ|" + "|" + coustTimeMap);
				System.out.println("ticker2|tps|" + "|" + tpsMap);

			}

		}

	}
}
