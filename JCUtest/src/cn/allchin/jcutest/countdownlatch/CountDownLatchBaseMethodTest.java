package cn.allchin.jcutest.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchBaseMethodTest {
	public static void main(String[] args) {
		CountDownLatch cdl=new CountDownLatch(3);
		try {
			cdl.await();
		} catch (InterruptedException e) { }
	}
}
