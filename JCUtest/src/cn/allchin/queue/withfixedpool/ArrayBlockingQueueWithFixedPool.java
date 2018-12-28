package cn.allchin.queue.withfixedpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.QueueThreadPoolCreater;

/**
 *
 ����core�߳�ȥ����
active|0|max|4|core|2|inqueue|-3|queueSize|0|queueRemain|3
active|1|max|4|core|2|inqueue|-3|queueSize|0|queueRemain|3
active|1|max|4|core|2|inqueue|-3|queueSize|0|queueRemain|3
active|2|max|4|core|2|inqueue|-3|queueSize|0|queueRemain|3
active|2|max|4|core|2|inqueue|-3|queueSize|0|queueRemain|3
����core֮�󣬿�ʼ����в���Ԫ��
active|2|max|4|core|2|inqueue|-1|queueSize|1|queueRemain|2
active|2|max|4|core|2|inqueue|-1|queueSize|1|queueRemain|2
active|2|max|4|core|2|inqueue|1|queueSize|2|queueRemain|1
active|2|max|4|core|2|inqueue|1|queueSize|2|queueRemain|1
active|2|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|2|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
��������֮�󣬿�ʼ�������̴߳���,ֱ��maxSize
active|3|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|3|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
��������maxSizeҲ����reject
Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task Thread[Thread-8,5,main] rejected from java.util.concurrent.ThreadPoolExecutor@33909752[Running, pool size = 4, active threads = 4, queued tasks = 3, completed tasks = 0]
	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2047)
	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:823)
	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1369)
	at cn.allchin.queue.PoolRunner.addJobToPool(PoolRunner.java:48)
	at cn.allchin.queue.PoolRunner.testOne(PoolRunner.java:38)
	at cn.allchin.queue.PoolRunner.main(PoolRunner.java:42)
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
active|4|max|4|core|2|inqueue|3|queueSize|3|queueRemain|0
��ticker�˳�ϵͳ



 * 
 * @author renxing.zhang
 *
 */
public class ArrayBlockingQueueWithFixedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads+2, 0L, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(3));

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}
}
