package cn.allchin.queue.withfixedpool;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.allchin.queue.PoolRunner;
import cn.allchin.queue.QueueThreadPoolCreater;

/**
 *  �����޽��������е����ԣ�
 *  Ӧ�ú�LimitedPriorityBlockingQueueWithCachedPoolһ��
 * 
 * @author renxing.zhang
 *
 */
public class LimitedDealyQueueWithCachedPool implements QueueThreadPoolCreater {

	int nThreads = 2;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads + 2, 0L, TimeUnit.MILLISECONDS,
			new  DelayQueue());
	//���������κ�����

	@Override
	public ThreadPoolExecutor createPool() {
		return pool;
	}

	public static void main(String[] args) {
		PoolRunner.testOne(new LimitedDealyQueueWithCachedPool());
	}

}
