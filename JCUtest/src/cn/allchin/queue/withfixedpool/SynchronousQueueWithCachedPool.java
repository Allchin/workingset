package cn.allchin.queue.withfixedpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.PoolRunner;
import cn.allchin.queue.PoolTicker;
import cn.allchin.queue.QueueThreadPoolCreater;

/**
 *  
active|0|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|1|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|1|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|2|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|2|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
coresize满了，队列不会堆积，直接申请maxSize
active|3|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|3|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
maxSize满了，不使用队列，直接reject
Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task Thread[Thread-5,5,main] rejected from java.util.concurrent.ThreadPoolExecutor@55f96302[Running, pool size = 4, active threads = 4, queued tasks = 0, completed tasks = 0]
	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2047)
	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:823)
	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1369)
	at cn.allchin.queue.PoolRunner.addJobToPool(PoolRunner.java:70)
	at cn.allchin.queue.withfixedpool.SynchronousQueueWithCachedPool.main(SynchronousQueueWithCachedPool.java:35)
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
active|4|max|4|core|2|inqueue|0|queueSize|0|queueRemain|0
由ticker退出系统

 * @author renxing.zhang
 *
 */
public class SynchronousQueueWithCachedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads+2, 0L, TimeUnit.MILLISECONDS,
			new  SynchronousQueue<Runnable>());
	 

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}
	
 
 
	public static void main(String[] args) {
		PoolRunner.testOne(new SynchronousQueueWithCachedPool());
	}
 
	
} 
