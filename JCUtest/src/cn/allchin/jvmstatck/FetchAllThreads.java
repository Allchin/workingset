package cn.allchin.jvmstatck;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchAllThreads {
    /**
     *  ��ȡJava VM�е�ǰ���е������߳�
     * @return
     */
    public static Thread[] findAllThreads() { 
    	
    	//
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // �����߳���������ȡ���߳���
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // ������߳����ӱ�
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slacks = new Thread[estimatedSize];
         //��ȡ���߳���������߳�
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
