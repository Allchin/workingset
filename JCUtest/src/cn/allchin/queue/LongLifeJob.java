package cn.allchin.queue;

/**
 * Ë¯¾õ10ÃëµÄjob
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
