package cn.allchin.mysql.ibd;

import java.io.FileReader;

class Reader {
	
	public static void main(String[] args) {
	 
		byte[] b = {'a','b','c','d'};
		
		Reader.read(b, 3, 7);
		  
	}
	
	/**
	 * @param src 长度为4的数组
	 * @param startIdx
	 * @param endIdx
	 * @return
	 */
	public static int read(byte[] src,int startIdx,int endIdx){
		int result=0;
		int[] temp=new int[4];
		for(int i=3;i>=0;i--){
			temp[i]=src[i]<<((3-i)*8) ;
			System.out.println(Integer.toBinaryString(temp[i]));
			result+=temp[i];
		}
		//System.out.println("srcInt|"+Integer.toBinaryString(result));
		
		result=result>>(32-endIdx-1);
		//System.out.println("movRight|"+(32-endIdx-1)+"|"+Integer.toBinaryString(result));
			
		int mask=~(0xFFFFFFFF<<(32-startIdx)>>(32-endIdx-1));
		//System.out.println("mask|"+Integer.toBinaryString(mask));
		
		result=result & mask;
		//System.out.println("result|"+Integer.toBinaryString(result));
		
		return result;		
			
	}
	
	/**
	 * @param src
	 * @param startIdx 0
	 * @param endIdx 7
	 * @return
	 */
	public static byte read(byte src, int startIdx, int endIdx) {
		byte result = src;
		 
		//System.out.println("src|"+byteToBit(result)); 
		
		result = (byte) (result >>>  ( 7 - endIdx) );
		//System.out.println(byteToBit(result));
		
		byte mask=(byte) ~(0xFF<< (8-startIdx));
		//System.out.println("mask|"+byteToBit(mask));
		result=(byte) (result&mask);
		//System.out.println("result|"+byteToBit(result));
		 
		
		return result;
	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	/**
	 * Bit转Byte
	 */
	public static byte bitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

}