package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * <pre>
 * MyCoundDownLatch 
 * https://www.cnblogs.com/daydaynobug/p/6752837.html
 * 
 * @author renxing.zhang
 *
 */

public class MyCoundDownLatchTester {

	/**
	 * 自己实现的countDownLatch
	 * 
	 * @author renxing.zhang
	 *
	 */
	public static class MyCoundDownLatch {
		private MyCoundDownSync sync = null;

		MyCoundDownLatch(int size) {
			/**
			 * countdown latch 还是比较简单，就是初始化n个资源，逐个释放就行
			 * */
			sync = new MyCoundDownSync(size);
		}

		public void countDown() {
			/**
			 * 每次countdown 就释放一个
			 * */
			sync.release(1);
		};

		public void await() throws InterruptedException {
			/**
			 * 等待其实就是尝试获取一个资源
			 * */
			sync.acquireInterruptibly(1);
		};
	}

	public static class MyCoundDownSync extends AbstractQueuedSynchronizer {
		/**
		 * 
		 */
		private static final long serialVersionUID = -692825284671672080L;

		MyCoundDownSync(int size) {
			this.setState(size);
		}
	}

}
