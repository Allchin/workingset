package cn.allchin.jcutest.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import cn.allchin.jcutest.Features;

public class SemaphoreBaseMethodTest implements Features {
	public static void main(String[] args) {
		
		
		SemaphoreBaseMethodTest test = new SemaphoreBaseMethodTest();
		test.unnamed1();
		System.out.println(test.isReentrantable());
		test.unname2();

	}
	
	/**
	 * 
	 * released
	   after relesase count |3
	 */
	public void unname2(){
		Semaphore semphore = new Semaphore(2);
		//breakpoint here
		/**
		 * 重复调用
		 * semphore.release();
		 * 是有危害的，他会简单的将资源的数目修改为+1，所以即便是当初分配了2个资源，先release会导致资源变成3
		 * */
		semphore.release();
		
		System.out.println("released");
		int count=0;
		for(int i=0;i<3;i++){
			try {
				boolean got=semphore.tryAcquire(10,TimeUnit.MILLISECONDS);
				if(got){
					count++;
				} 
			} catch (InterruptedException e) { 
			}
			
		}
		System.out.println("after relesase count |"+count);
	}
	
	/**
	 * reentrant2 
	 */
	public void unnamed1(){ 
		Semaphore semphore = new Semaphore(2);
		//semphore.tryAcquire(timeout, unit)
		int count=0;
		for(int i=0;i<3;i++){
			try {
				boolean got=semphore.tryAcquire(10,TimeUnit.MILLISECONDS);
				if(got){
					count++;
				} 
			} catch (InterruptedException e) {
				 System.err.println("打断");
			}
			
		}
		boolean isReentrant=count>=3;
		System.out.println(isReentrant+ "|"+count);
		
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
		

		boolean result = t.getCount() >= 3;
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
