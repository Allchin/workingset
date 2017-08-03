package cn.allchin.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Construction {
	public static void main(String[] args) {
		Executors.newFixedThreadPool(2);
		ThreadPoolExecutor es= new ThreadPoolExecutor(2, 2,
                 0L, TimeUnit.MILLISECONDS,
                 new LinkedBlockingQueue<Runnable>());
		//
		Executors.newCachedThreadPool();
		new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
		//
		Executors.newScheduledThreadPool(4); 
		/*new ThreadPoolExecutor(4,
				Integer.MAX_VALUE,
                0,
                NANOSECONDS,
                new DelayedWorkQueue());*/
		//
		Executors.newSingleThreadExecutor();
/*		new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));*/
		
		System.out.println(es.getClass());
		
		
	}
	
	public void testLinkedBlockingQueue(){
		LinkedBlockingQueue queue =new LinkedBlockingQueue();
		
	}
}
