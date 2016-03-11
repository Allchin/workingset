package cn.allchin.jcutest.semaphore;

import java.util.concurrent.Semaphore;

import cn.allchin.jcutest.Features;

public class SemaphoreBaseMethodTest implements Features {
	public static void main(String[] args) {
		SemaphoreBaseMethodTest test = new SemaphoreBaseMethodTest();
		System.out.println(test.isReentrantable());

	}

	/* 
	 * 看来同一个线程不能重入，也会被阻塞
	 * (non-Javadoc)
	 * @see cn.allchin.jcutest.Features#isReentrantable()
	 */
	public boolean isReentrantable() {

		T t = new T();
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		boolean result = t.getCount() > 3;
		return result;
	}

	public static class T extends Thread {
		Semaphore semphore = new Semaphore(2);
		int count = 0;

		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				try {
					semphore.acquire();
					count++;
				} catch (InterruptedException e) {
				}
			}
		}

		public int getCount() {
			return count;
		}
	}
}
