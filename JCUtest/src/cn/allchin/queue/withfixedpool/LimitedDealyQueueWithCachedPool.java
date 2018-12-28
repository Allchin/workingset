package cn.allchin.queue.withfixedpool;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.PoolRunner;
import cn.allchin.queue.QueueThreadPoolCreater;

/**
 *  按照无界阻塞队列的尿性，
 *  应该和LimitedPriorityBlockingQueueWithCachedPool一样
 * 
 * @author renxing.zhang
 *
 */
public class LimitedDealyQueueWithCachedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads + 2, 0L, TimeUnit.MILLISECONDS,
			new  DelayQueue());
	//并不能做任何设置

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}

	public static void main(String[] args) {
		PoolRunner.testOne(new LimitedDealyQueueWithCachedPool());
	}

}
