package cn.allchin.queue;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ����ȥʵ��һ���̳߳�����е����
 * @author renxing.zhang
 *
 */
public interface QueueThreadPoolCreater {
	public ThreadPoolExecutor createPool();
}
