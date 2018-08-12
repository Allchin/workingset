package cn.allchin.jvmstatck;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchAllThreads {
    /**
     *  获取Java VM中当前运行的所有线程
     * @return
     */
    public static Thread[] findAllThreads() { 
    	
    	//
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slacks = new Thread[estimatedSize];
         //获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slacks);
        Thread[] threads = new Thread[actualSize];
        System.arraycopy(slacks, 0, threads, 0, actualSize);
        return threads;
    }
    
    public static void main(String[] args) {
    	Thread manT=new Thread(){
    		@Override
    		public void run() {
    			 for(int i=0;i<100;i++){
    				 try {
						Thread.sleep(1000);
					} catch (InterruptedException e) { }
    				
    			 }
    		}
    	};
    	manT.setName("manNamedThread");
    	manT.start();
    	//
    	ExecutorService es=Executors.newFixedThreadPool(10); 
    	es.submit(manT);
    	es.submit(manT);
    	es.submit(manT);
    	
    	
    	//
    	Thread[] ts=	FetchAllThreads.findAllThreads();
    	for(Thread t:ts){
    		System.out.println("T|name|"+t.getName()+"|id|"+t.getId()+"|class|"+t.getClass());
    		System.out.println("group|"+t.getThreadGroup().getName()+"|"+t.getThreadGroup());
    	}
	}
}
