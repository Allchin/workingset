package cn.allchin.jcutest.palle;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import cn.allchin.jcutest.SeriTester;
import cn.allchin.jcutest.SeriTester.SerilzeWorker;

public class SeriTesterItSelfTester {
	/**
	 * 毫无用处的代码，只是为了不优化掉执行的工作代码
	 */
	private static long justForGit=0;
	/**
	 * <pre>
	 * 
执行结果：
4线程
target|1073741823|SerilzeWorker|addr|961104232last work|进度|6
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=10438, LongAdderWorker2|addr|865113938=31655, AtomicLongWorker|addr|1550089733=16163, LongAdderWorker3|addr|1442407170=8064}
target|1073741823|SerilzeWorker|addr|961104232last work|进度|8
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=10438, LongAdderWorker2|addr|865113938=31655, AtomicLongWorker|addr|1550089733=16163, LongAdderWorker3|addr|1442407170=6338}
target|1073741823|SerilzeWorker|addr|961104232last work|进度|8
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=10556, LongAdderWorker2|addr|865113938=31655, AtomicLongWorker|addr|1550089733=16163, LongAdderWorker3|addr|1442407170=6338}


	 * 2线程
target|1073741823|SerilzeWorker|addr|848636269last work|进度|14
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=24763, LongAdderWorker2|addr|865113938=51267, AtomicLongWorker|addr|1550089733=37324, LongAdderWorker3|addr|1442407170=18714}
target|1073741823|SerilzeWorker|addr|848636269last work|进度|14
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=24984, LongAdderWorker2|addr|865113938=52022, AtomicLongWorker|addr|1550089733=42473, LongAdderWorker3|addr|1442407170=18714}
target|1073741823|SerilzeWorker|addr|848636269last work|进度|16
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=25970, LongAdderWorker2|addr|865113938=52022, AtomicLongWorker|addr|1550089733=42473, LongAdderWorker3|addr|1442407170=18497}

      单线程
      ticker2|tps||{AtomicLongWorkerGI|addr|118352462=71544, LongAdderWorker2|addr|865113938=60025, AtomicLongWorker|addr|1550089733=70827, LongAdderWorker3|addr|1442407170=50306}
target|1073741823|SerilzeWorker|addr|1140114527last work|进度|3
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=69941, LongAdderWorker2|addr|865113938=59048, AtomicLongWorker|addr|1550089733=70827, LongAdderWorker3|addr|1442407170=47293}
target|1073741823|SerilzeWorker|addr|1140114527last work|进度|3
ticker2|tps||{AtomicLongWorkerGI|addr|118352462=69941, LongAdderWorker2|addr|865113938=59204, AtomicLongWorker|addr|1550089733=71127, LongAdderWorker3|addr|1442407170=47293}
总结：
可以看到
LongAdder在单线程情况下，没有AtomicLong性能好，
但是随着线程数升高，则LongAdder越来越好

	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int thread=1;
		SeriTester st=new SeriTester(new Runnable[] {new AtomicLongWorkerGI(),new AtomicLongWorker() ,new LongAdderWorker2(),new LongAdderWorker3() }, thread,2046);
		st.doTest();  
		justForGit++;
		
	}
	public static class AtomicLongWorker implements   Runnable {
		private volatile AtomicLong al=new AtomicLong(); 
		public void run() { 
				justForGit=al.incrementAndGet();
		 
		} 
	}
	public static class AtomicLongWorkerGI implements   Runnable {
		private volatile AtomicLong al=new AtomicLong(); 
		public void run() { 
				justForGit=al.getAndIncrement();
		 
		} 
	}
	public static class LongAdderWorker2 implements   Runnable{
		private volatile LongAdder al=new LongAdder();
		public void run() { 
				al.increment();   
			
		} 
		
	}
	
	public static class LongAdderWorker3 implements   Runnable{
		private volatile LongAdder al=new LongAdder();
		public void run() { 
				al.increment();   
				justForGit=al.longValue();
		} 
		
	}
}
