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
	 * �Լ�ʵ�ֵ�countDownLatch
	 * 
	 * @author renxing.zhang
	 *
	 */
	public static class MyCoundDownLatch {
		private MyCoundDownSync sync = null;

		MyCoundDownLatch(int size) {
			/**
			 * countdown latch ���ǱȽϼ򵥣����ǳ�ʼ��n����Դ������ͷž���
			 * */
			sync = new MyCoundDownSync(size);
		}

		public void countDown() {
			/**
			 * ÿ��countdown ���ͷ�һ��
			 * */
			sync.release(1);
		};

		public void await() throws InterruptedException {
			/**
			 * �ȴ���ʵ���ǳ��Ի�ȡһ����Դ
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
