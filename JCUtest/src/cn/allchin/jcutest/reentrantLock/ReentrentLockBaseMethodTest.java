package cn.allchin.jcutest.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import cn.allchin.jcutest.Features;

public class ReentrentLockBaseMethodTest implements Features {
	 public static void main(String[] args) {
		 ReentrantLock lock=new ReentrantLock();
		 lock.lock();
		 lock.lock();
		 lock.lock();
		 System.out.println("pass 3");
		 
		 Thread t=new Thread(){
			 public void run() {
				 lock.lock();
			 };
		 };
		 t.start();
	}
	
	public void tryLock(){
		 ReentrantLock lock=new ReentrantLock();
		 lock.tryLock();
	}

	@Override
	public boolean isReentrantable() {

		 ReentrantLock lock=new ReentrantLock();
		 lock.lock();
		 lock.lock();
		 lock.lock();
		 System.out.println("pass 3");
		 return true; 
	
	}
}
