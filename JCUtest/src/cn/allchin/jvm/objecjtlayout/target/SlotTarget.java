package cn.allchin.jvm.objecjtlayout.target;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class SlotTarget {
	/**
	 * 是否本次执行超时了
	 * 根据jobRt和jobTimeout来计算
	 */
	private boolean timeouted;

	/**
	 * 发生的时间
	 */
	private long time;
	
	/**
	 * dubbo consumer ，provider获取方法
	 * 获取方法:Filter
	 * 记录jobName同时记录jobRt
	 * 
	 * 普通线程池：TODO 
	 * 
	 * QMQ 线程池：
	 */
	private int jobRt;

	public boolean isTimeouted() {
		return timeouted;
	}

	public void setTimeouted(boolean timeouted) {
		this.timeouted = timeouted;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getJobRt() {
		return jobRt;
	}

	public void setJobRt(int jobRt) {
		this.jobRt = jobRt;
	}
	public static void main(String[] args) {
		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(SlotTarget.class).toPrintable();
		System.out.println(layout);
		
		SlotTarget[] sts=new SlotTarget[12];
		sts[0]=new SlotTarget();
		layout=ClassLayout.parseClass(sts.getClass() ).toPrintable();
		System.out.println(layout);
	}
	
}
