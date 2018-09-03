package cn.allchin.jvm.objecjtlayout.target;

import java.util.concurrent.atomic.AtomicInteger;

public class ArrayTargetPage {
	
	private static  int slotSize=100;
	 
	
	private AtomicInteger index=new AtomicInteger(0); 
	/**
	 * 是否本次执行超时了
	 * 根据jobRt和jobTimeout来计算
	 */
	private boolean[] timeouted=new boolean[slotSize];
	
	/**
	 * 发生的时间
	 * 不用存
	 */
	//private long[] time=new long[slotSize];
	
	/**
	 * dubbo consumer ，provider获取方法
	 * 获取方法:Filter
	 * 记录jobName同时记录jobRt
	 * 
	 * 普通线程池：TODO 
	 * 
	 * QMQ 线程池：
	 */
	private int[] jobRt=new int[slotSize];
	
	public void add(SlotTarget slot){
		/*
		 * 按照秒来分页，可以不用
		 * if(!limiter.tryAcquire() ){
			//too fast to record 
			return ;
		}*/
		int idx=index.incrementAndGet();
		if(idx>slotSize-1){
			//too much to record
			return ;
		}
		jobRt[idx]=slot.getJobRt();
	
		timeouted[idx]=slot.isTimeouted();
	}
	
	/**
	 * 
	 * 清理：设置为默认值,索引设置为-1
	 * 测试:36000 size清理速度为1ms
	 * 
	 */
	public void reset(){
		for(int i =0;i<slotSize;i++){
			 jobRt[i]=-1;
			 
			 timeouted[i] =false;
			 index.set(-1);
		}
	}
	public static int getSlotsize() {
		return slotSize;
	}
	
	public static void setSlotsize(int slotsize) {
		slotSize = slotsize;
	}
	
	public AtomicInteger getIndex() {
		return index;
	}
	
	public void setIndex(AtomicInteger index) {
		this.index = index;
	}
	
	public boolean[] getTimeouted() {
		return timeouted;
	}
	
	public void setTimeouted(boolean[] timeouted) {
		this.timeouted = timeouted;
	}
	
	
	
	public int[] getJobRt() {
		return jobRt;
	}
	
	public void setJobRt(int[] jobRt) {
		this.jobRt = jobRt;
	} 
	
	
	
	
}

