package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * https://www.cnblogs.com/daydaynobug/p/6752837.html
 * @author renxing.zhang
 *
 */
public class AbstractQueuedSynchronizerTester {
	
	
	public static class MyCoundDownLatch {
		private MyCoundDownSync sync=null;
		MyCoundDownLatch(int size){
			sync = new MyCoundDownSync(size);
		}
		
		public void countDown(){
			sync.release(1);
		};
		
		public void await() throws InterruptedException{
			sync.acquireInterruptibly(1);
		};
	}
	public static class MyCoundDownSync extends   AbstractQueuedSynchronizer{
		MyCoundDownSync(int size){
			this.setState(size);
		}
		
		
	}
}
