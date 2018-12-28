package cn.allchin.queue;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 子类去实现一种线程池与队列的组合
 * @author renxing.zhang
 *
 */
public interface QueueThreadPoolCreater {
	public ThreadPoolExecutor createPool();
}
