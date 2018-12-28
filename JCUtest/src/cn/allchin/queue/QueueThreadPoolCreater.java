package cn.allchin.queue;

import java.util.concurrent.ThreadPoolExecutor;

public interface QueueThreadPoolCreater {
	public ThreadPoolExecutor createPool();
}
