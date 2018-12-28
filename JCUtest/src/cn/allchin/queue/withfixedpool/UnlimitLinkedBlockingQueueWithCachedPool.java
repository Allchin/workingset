package cn.allchin.queue.withfixedpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.PoolRunner;
import cn.allchin.queue.PoolTicker;
import cn.allchin.queue.QueueThreadPoolCreater;

/**
 * 
active|0|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|1|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|1|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
active|2|max|4|core|2|inqueue|-2147483647|queueSize|0|queueRemain|2147483647
//到达core ,开始向队列里面加
active|2|max|4|core|2|inqueue|-2147483645|queueSize|1|queueRemain|2147483646
active|2|max|4|core|2|inqueue|-2147483645|queueSize|1|queueRemain|2147483646
active|2|max|4|core|2|inqueue|-2147483643|queueSize|2|queueRemain|2147483645
active|2|max|4|core|2|inqueue|-2147483643|queueSize|2|queueRemain|2147483645
active|2|max|4|core|2|inqueue|-2147483641|queueSize|3|queueRemain|2147483644
active|2|max|4|core|2|inqueue|-2147483641|queueSize|3|queueRemain|2147483644
active|2|max|4|core|2|inqueue|-2147483639|queueSize|4|queueRemain|2147483643
active|2|max|4|core|2|inqueue|-2147483639|queueSize|4|queueRemain|2147483643
active|2|max|4|core|2|inqueue|-2147483637|queueSize|5|queueRemain|2147483642
active|2|max|4|core|2|inqueue|-2147483637|queueSize|5|queueRemain|2147483642
active|2|max|4|core|2|inqueue|-2147483635|queueSize|6|queueRemain|2147483641
active|2|max|4|core|2|inqueue|-2147483635|queueSize|6|queueRemain|2147483641
active|2|max|4|core|2|inqueue|-2147483633|queueSize|7|queueRemain|2147483640
active|2|max|4|core|2|inqueue|-2147483633|queueSize|7|queueRemain|2147483640
active|2|max|4|core|2|inqueue|-2147483631|queueSize|8|queueRemain|2147483639
//一直在向队列里面加，永远到不了maxCore
由ticker退出系统

 * 
 * @author renxing.zhang
 *
 */
public class UnlimitLinkedBlockingQueueWithCachedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads+2, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());
	//不设置LinkedBlockingQueue的size

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}
	
 
	public static void main(String[] args) {
		CountDownLatch cdt = new CountDownLatch(1);
		ThreadPoolExecutor pool=new UnlimitLinkedBlockingQueueWithCachedPool().createPool();
		new PoolTicker(pool, cdt);
		PoolRunner.addJobToPool(pool);
	}
} 
