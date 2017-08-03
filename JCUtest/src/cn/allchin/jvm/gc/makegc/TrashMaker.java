package cn.allchin.jvm.gc.makegc;
/**
 * 制造垃圾，触发回收
 * @author renxing.zhang
 *
 */
public class TrashMaker {  
  
    private static int n = 20;  
  
    /** 
     * @param args 
     * @throws InterruptedException 
     */  
    public static void main(String[] args) throws InterruptedException {  
        
  
        byte[] b1 = getM(50);  
        byte[] b2 = getM(50);  
        byte[] b3 = getM(50);  
        byte[] b4 = getM(50);  
        byte[] b5 = getM(50);  
        byte[] b6 = getM(50);  
        byte[] b7 = getM(5);  
        byte[] b8 = getM(5);  
        byte[] b9 = getM(5);  
        byte[] b10 = getM(5);  
        byte[] b11 = getM(5);  
        byte[] b12 = getM(5);  
        byte[] b13 = getM(5);  
        byte[] b14 = getM(5);  
        byte[] b15 = getM(5);  
        byte[] b16 = getM(5);  
        byte[] b17 = getM(5);  
        byte[] b18 = getM(5);  
        byte[] b19 = getM(5);  
        byte[] b20 = getM(100);  
        byte[] b21 = getM(100);  
        byte[] b22 = getM(100);  
        byte[] b23 = getM(100);  
  
    }  
  
    public static byte[] getM(int m) {  
        return new byte[1024 * 1024 * m];  
    }  
  
}  