package cn.allchin.jvm.objecjtlayout.target;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class Page {
	int size=36000;
	/**
	 * 是否本次执行超时了
	 * 根据jobRt和jobTimeout来计算
	 */
	private boolean[] timeouted=new boolean[size];

	/**
	 * 发生的时间
	 */
	private long[] time=new long[size];
	
	/**
	 * dubbo consumer ，provider获取方法
	 * 获取方法:Filter
	 * 记录jobName同时记录jobRt
	 * 
	 * 普通线程池：TODO 
	 * 
	 * QMQ 线程池：
	 */
	private int[] jobRt=new int[size]; 
	
	public static void main(String[] args) {
		
		
		//
		StringBuffer sb=new StringBuffer();
		sb.append(VM.current().details() );
	 
		
		String layout=ClassLayout.parseClass(Page.class).toPrintable();
		sb.append(layout);
		//
		Page page=new Page();
		reset(page);
		String layout2=ClassLayout.parseClass(Page.class).toPrintable(page);
		sb.append(layout2);
		System.out.println(sb);
	 
	}
	
	private static void reset(Page page){
		long start=System.currentTimeMillis();
		for(int i =0;i<page.size;i++){
			page.jobRt[i]=-1;
			page.time[i]=-1;
			page.timeouted[i] =false;
		}
		long during=System.currentTimeMillis()-start;
		System.out.println("清理完成，耗时|"+during);
	}
}
