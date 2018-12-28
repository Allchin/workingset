package cn.allchin.queue;

/**
 * Ë¯¾õ10ÃëµÄjob
 * @author renxing.zhang
 *
 */
public class LongLifeJob  extends Thread implements java.lang.Comparable<LongLifeJob>{
	private long start=System.currentTimeMillis();
	 
	@Override
	public void run() {
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) { }
	}

	@Override
	public int compareTo(LongLifeJob o) {
		
		return (int) (start-o.start);
	}

	 
}
