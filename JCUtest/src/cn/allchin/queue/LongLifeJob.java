package cn.allchin.queue;

/**
 * ˯��10���job
 * @author renxing.zhang
 *
 */
public class LongLifeJob  extends Thread{
	@Override
	public void run() {
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) { }
	}
}
