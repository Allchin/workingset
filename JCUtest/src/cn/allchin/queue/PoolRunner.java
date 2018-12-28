package cn.allchin.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import cn.allchin.queue.withfixedpool.ArrayBlockingQueueWithFixedPool;

public class PoolRunner {
	static QueueThreadPoolCreater[] creaters = new QueueThreadPoolCreater[] { new ArrayBlockingQueueWithFixedPool() };
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
	public static void testOne(){
		CountDownLatch cdt = new CountDownLatch(1);
		ThreadPoolExecutor pool=new ArrayBlockingQueueWithFixedPool().createPool();
		new PoolTicker(pool, cdt);
		addJobToPool(pool);
	}

	public static void main(String[] args) {
		testOne();
		//testAll();
	}

	private static void addJobToPool(Executor pool) {
		for (int i = 0; i < 10; i++) {
			pool.execute(new LongLifeJob());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
