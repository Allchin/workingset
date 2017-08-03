package cn.allchin.jvm.heap;

import cn.allchin.jvm.gc.makegc.TrashMaker;

/**
 * jvm±ÀÀ£Ê±dump¶Ñ³öÈ¥
 * 
 *  -XX:+HeapDumpOnOutOfMemoryError
	-XX:HeapDumpPath=D:\tmp
 * 
 * @author renxing.zhang
 *
 */
public class HeapDumpMaker {
	public static void main(String[] args) {
		byte[][] bs=new byte[10240][];
		for(int i=1;i<Integer.MAX_VALUE;i++){
			bs[i]=TrashMaker.getM(i);
			System.out.println("i|"+i);
		}
	}
}
