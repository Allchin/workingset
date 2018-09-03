package cn.allchin.jvm.objecjtlayout.target;

import java.util.concurrent.atomic.AtomicInteger;

public class ArrayTargetPage {
	
	private static  int slotSize=100;
	 
	
	private AtomicInteger index=new AtomicInteger(0); 
	/**
	 * �Ƿ񱾴�ִ�г�ʱ��
	 * ����jobRt��jobTimeout������
	 */
	private boolean[] timeouted=new boolean[slotSize];
	
	/**
	 * ������ʱ��
	 * ���ô�
	 */
	//private long[] time=new long[slotSize];
	
	/**
	 * dubbo consumer ��provider��ȡ����
	 * ��ȡ����:Filter
	 * ��¼jobNameͬʱ��¼jobRt
	 * 
	 * ��ͨ�̳߳أ�TODO 
	 * 
	 * QMQ �̳߳أ�
	 */
	private int[] jobRt=new int[slotSize];
	
	public void add(SlotTarget slot){
		/*
		 * ����������ҳ�����Բ���
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
	 * ��������ΪĬ��ֵ,��������Ϊ-1
	 * ����:36000 size�����ٶ�Ϊ1ms
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

