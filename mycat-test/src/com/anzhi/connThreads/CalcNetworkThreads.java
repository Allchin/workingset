package com.anzhi.connThreads;

/**
 * @author Administrator
 *
 */
public class CalcNetworkThreads {
	public static final long width_100Mb=100L*1024L*1024L/8L;
	public static final long width_1000Mb=1000L*1024L*1024L/8L;
 
	
	/**
	 * @param bandwidth 带宽,字节
	 * @param reqLengthByte 单个请求大小,字节
	 * @param ops  每个线程能够压到的ops
	 * @return
	 */
	public static final long calcThread(long bandwidth,long reqLengthByte,long ops){
			return bandwidth/(reqLengthByte*ops);
	}
	
	public static void main(String[] args) {
		
		System.out.println(calcThread(width_100Mb, 920L/8L, 1501L/2L));
	}
}
