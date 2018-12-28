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
	 * ִ�����еı����Գ��ӣ����Ƚϲ���
	 */
	public static void testAll() {

		CountDownLatch cdt = new CountDownLatch(pools.length);
		for (ThreadPoolExecutor pool : pools) {
			new PoolTicker(pool, cdt);
			addJobToPool(pool);
		}

	}

	/**
	 * �ֹ��ı����Ե���ɣ�һ������
	 */
	public static void testOne() {
		CountDownLatch cdt = new CountDownLatch(1);
		ThreadPoolExecutor pool = new LimitedLinkedBlockingQueueWithCachedPool().createPool();
		new PoolTicker(pool, cdt);
		addJobToPool(pool);
	}

	/**
	 * ����һ���̳߳�����е���ϴ���
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
	 * �ύ10������������
	 * @param pool
	 */
	public static void addJobToPool(Executor pool) {
		System.out.println("�ύ10������������ÿ���ύ1��");
		for (int i = 0; i < 10; i++) {
			// System.out.println("׼�������һ�������̳߳�");
			pool.execute(new LongLifeJob());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
