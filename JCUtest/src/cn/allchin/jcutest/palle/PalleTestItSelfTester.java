package cn.allchin.jcutest.palle;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import cn.allchin.jcutest.PalleTester;
import cn.allchin.jcutest.PalleTester.Worker;

public class PalleTestItSelfTester {
	public static void main(String[] args) {
		PalleTester pt=new PalleTester(new Worker[] {new LongAdderWorker2(),new AtomicLongWorker() }, 64);
		pt.doTest();
	}
	public static class AtomicLongWorker extends Worker{
		private volatile AtomicLong al=new AtomicLong();
		
		@Override
		public void workOnce() {
			al.incrementAndGet();
		 
		 
		}
		 
	}
	
	public static class LongAdderWorker2 extends Worker{
		private volatile LongAdder al=new LongAdder();
		@Override
		public void workOnce() {
			al.add(1); 
		}
		 
		
	}
}
