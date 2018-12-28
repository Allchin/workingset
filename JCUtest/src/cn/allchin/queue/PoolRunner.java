package cn.allchin.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import cn.allchin.queue.withfixedpool.ArrayBlockingQueueWithCachedPool;
import cn.allchin.queue.withfixedpool.LimitedLinkedBlockingQueueWithCachedPool;
import cn.allchin.queue.withfixedpool.UnlimitLinkedBlockingQueueWithCachedPool;

public class PoolRunner {
	static QueueThreadPoolCreater[] creaters = new QueueThreadPoolCreater[] { new ArrayBlockingQueueWithCachedPool(),
			new LimitedLinkedBlockingQueueWithCachedPool() };
	static ThreadPoolExecutor[] pools = new ThreadPoolExecutor[creaters.length];

	static {
		for (int i = 0; i < creaters.length; i++) {
			pools[i] = creaters[i].createPool();
		}
	}

	/**
	 * 执行所有的被测试池子，来比较差异
	 */
	public static void testAll() {

		CountDownLatch cdt = new CountDownLatch(pools.length);
		for (ThreadPoolExecutor pool : pools) {
			new PoolTicker(pool, cdt);
			addJobToPool(pool);
		}

	}

	/**
	 * 手工改被测试的类吧，一个运行
	 */
	public static void testOne() {
		CountDownLatch cdt = new CountDownLatch(1);
		ThreadPoolExecutor pool = new LimitedLinkedBlockingQueueWithCachedPool().createPool();
		new PoolTicker(pool, cdt);
		addJobToPool(pool);
	}

	/**
	 * 测试一种线程池与队列的组合搭配
	 * @param creater
	 */
	public static void testOne(QueueThreadPoolCreater creater) {
		System.out.println(creater.getClass());
		CountDownLatch cdt = new CountDownLatch(1);
		ThreadPoolExecutor pool = creater.createPool();
		new PoolTicker(pool, cdt);
		PoolRunner.addJobToPool(pool);

	}

	public static void main(String[] args) {
		testOne();
		// testAll();
	}

	/**
	 * 提交10个长寿命任务
	 * @param pool
	 */
	public static void addJobToPool(Executor pool) {
		System.out.println("提交10个长寿命任务，每秒提交1个");
		for (int i = 0; i < 10; i++) {
			// System.out.println("准备添加了一个任务到线程池");
			pool.execute(new LongLifeJob());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
