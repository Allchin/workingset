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
	public static void main(String[] args) {
		int thread=4;
		SeriTester st=new SeriTester(new Runnable[] {new AtomicLongWorker() ,new AtomicLongWorker(),new LongAdderWorker3() }, thread);
		st.doTest();
		justForGit++;
		
	}
	public static class AtomicLongWorker implements   Runnable {
		private volatile AtomicLong al=new AtomicLong();
		
		
		public void run() {
			 
				justForGit=al.incrementAndGet();
		 
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
