package cn.allchin.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

public class PoolTicker {
	private ThreadPoolExecutor executor = null;

	/**
	 * ��������������ɺ����ֹӦ��
	 * @param executor
	 * @param cdt
	 */
	public PoolTicker(ThreadPoolExecutor executor,CountDownLatch cdt) {
		this.executor = executor;
		Thread t = new Thread() {
			public void run() {
				for (int i = 0; i < 20; i++) {
					try {
						tick();
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				cdt.countDown(); 
				try {
					cdt.await();
				} catch (InterruptedException e) { } 
				System.out.println("��ticker�˳�ϵͳ");
				System.exit(0);
			};
		};
		t.start();

	}

	public void tick() {
		System.out.println("active|" + executor.getActiveCount() + "|max|" + executor.getMaximumPoolSize() + "|core|"
				+ executor.getCorePoolSize() + "|inqueue|"
				+ (executor.getQueue().size() - executor.getQueue().remainingCapacity())
				+"|queueSize|"+executor.getQueue().size()
				+"|queueRemain|"+executor.getQueue().remainingCapacity()
				);
	}

}
