package cn.allchin.os.mem.l3;

import cn.allchin.os.mem.l3.FalseShareJava8Allchin.VolatileLongContended;

public class WellShareWorker implements Runnable {
	public static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;
	private static VolatileLongContended[] longs = new VolatileLongContended[NUM_THREADS];

	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileLongContended();
		}
	}

	public WellShareWorker(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}
}