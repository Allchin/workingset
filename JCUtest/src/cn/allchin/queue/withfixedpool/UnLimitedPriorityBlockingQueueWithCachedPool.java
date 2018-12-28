package cn.allchin.queue.withfixedpool;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.PoolRunner;
import cn.allchin.queue.QueueThreadPoolCreater;

/**
 * 
class cn.allchin.queue.withfixedpool.UnLimitedPriorityBlockingQueueWithCachedPool
active|0|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|1|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|1|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483646|queueSize|1|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483646|queueSize|1|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483645|queueSize|2|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483645|queueSize|2|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483644|queueSize|3|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483644|queueSize|3|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483643|queueSize|4|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483643|queueSize|4|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483642|queueSize|5|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483642|queueSize|5|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483641|queueSize|6|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483641|queueSize|6|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483640|queueSize|7|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483640|queueSize|7|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483639|queueSize|8|queueRemain|2147483647
由ticker退出系统
看起来 LimitedPriorityBlockingQueueWithCachedPool 一样，看起来优先级队列的size设置没用
 * 
 * @author renxing.zhang
 *
 */
public class UnLimitedPriorityBlockingQueueWithCachedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads + 2, 0L, TimeUnit.MILLISECONDS,
			new PriorityBlockingQueue<Runnable>());
	// 不限制优先级队列大小

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}

	public static void main(String[] args) {
		PoolRunner.testOne(new UnLimitedPriorityBlockingQueueWithCachedPool());
	}

}
