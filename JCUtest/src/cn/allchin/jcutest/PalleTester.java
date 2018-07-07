package cn.allchin.jcutest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多个work并发测试的工具类，更公平，同样的条件进行测试。
 * @author citi0
 *
 */
public class PalleTester {
	private int threads = 4;
	private Worker[] works;

	long start = 0;
	// 共同目标
	private final static int maxRound = Integer.MAX_VALUE / 2;
	private ExecutorService fixedTheadPool = null;
	/**
	 * 同时开始
	 */
	private volatile static boolean loaded = false;
	private volatile static boolean allFinished=false;
	// 运行时
	 
	private static ConcurrentHashMap<String, Long> coustTimeMap = new ConcurrentHashMap<String, Long>();
	
	public PalleTester(Worker[] works, int threads) {
		this.works = works;
		this.threads = threads;
		fixedTheadPool = Executors.newFixedThreadPool(threads + 1);
	}

	public void doTest() {
		int groups = works.length;
		int members = threads / groups;
		CountDownLatch cd=new CountDownLatch(groups*members);
		System.out.println("分组|groups|"+groups+"|thread on work|"+members);
		start = System.currentTimeMillis();
		//让任务开始执行，但是没有实际使用算法 
		
		for (Worker wk : works) { 
				wk.start=start;
				wk.cd=cd; 
		}
		int idx=0;
		for(int i=0;i<groups*members;i++) {
			fixedTheadPool.submit(works[idx]);
			idx++;
			if(idx>works.length-1) {
				idx=0;
			}
		}
		
		fixedTheadPool.submit(new Ticker(works,cd));
		//开始执行算法
		loaded = true;
		try {
			cd.await();
			allFinished=true;
			//执行完毕关闭ticket
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		
		//结束
		fixedTheadPool.shutdown();
	}

	public abstract static class Worker implements Runnable {
		private long start=0;
		private CountDownLatch cd;
		private AtomicInteger ai=new AtomicInteger(0);
		@Override
		public void run() {
			while (true) {
				if (loaded) {//有没有更好的同步方法 ？
					  workOnce();
					if (maxRound <= ai.incrementAndGet()) {
						String clazzName=this.getClass().getSimpleName();
						long theadId=Thread.currentThread().getId();
						long during =System.currentTimeMillis()-start;
						coustTimeMap.put(clazzName + "|" + theadId  ,during
								);
						System.out.println("thread|fin|"+clazzName  + "|t|" + theadId+"|time|"+during);
						cd.countDown();
						return;
					}
				}
			}

		}
 
		public abstract void workOnce();

	}

	public static class Ticker implements Runnable {
		private Worker[] works;
		private CountDownLatch cd;
		public Ticker(Worker[] works,CountDownLatch cd) {
			 this.cd=cd;
			 this.works=works;
		}

		@Override
		public void run() {
			while (!allFinished) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				for(Worker wk:works) {
					System.out.println("ticker0|"+wk.getClass().getSimpleName()+"work|进度|" +wk.ai.get());
				}  
			}
			System.out.println("ticker1|总体进度|" +   "|" + coustTimeMap);
			

		}

	}
}
