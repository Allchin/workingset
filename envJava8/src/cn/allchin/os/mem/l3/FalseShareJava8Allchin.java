package cn.allchin.os.mem.l3;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/** <pre>
 * 在JAVA 8中，缓存行填充终于被JAVA原生支持了。
 * JAVA 8中添加了一个@Contended的注解，添加这个的注解，
 * 将会在自动进行缓存行填充。
 执行时，必须加上虚拟机参数-XX:-RestrictContended，
 @Contended注释才会生效。很多文章把这个漏掉了，
 那样的话实际上就没有起作用。
  
  
  这个例子其实不好，没有比较，不够直观
 * @author renxing.zhang
 *
 */
import sun.misc.Contended;
/**
 * 
 * <pre>
 不加@Contented
 WellShareWorker|duration = 28109484270
 FalseShareWorker|duration = 26874224045
 
加@Contented,不加-XX:-RestrictContended
 
FalseShareWorker|duration = 34084673751
WellShareWorker|duration = 21820166776
WellShareWorker|duration = 29021606859

加@Contented,加上：-XX:-RestrictContended
WellShareWorker|duration = 11998039175
FalseShareWorker|duration = 35046889506
 * @author renxing.zhang
 *
 */
public class FalseShareJava8Allchin  {  
    public static int NUM_THREADS = 4; // change  
    static{
    	System.out.println(System.getProperties().get("java.version"));
    	 
 		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(VolatileLongContended.class).toPrintable();
		System.out.println(layout);
		
		layout=ClassLayout.parseClass(PlantLong.class).toPrintable();
		System.out.println(layout);
		
    }
 
  
    public static void main(final String[] args) throws Exception {
        Thread.sleep(1000);  
        System.out.println("starting....");   
        runTest();
        
    }  
  
    private static void runTest() throws InterruptedException {  
        Thread[] threads = new Thread[NUM_THREADS];  
        for (int i = 0; i < threads.length; i++) {  
            threads[i] = new Thread(new FalseShareWorker(i));  
        }  
        final long start = System.nanoTime();  
        for (Thread t : threads) {  
            t.start();  
        }  
        for (Thread t : threads) {  
            t.join();  
        }
        System.out.println("FalseShareWorker|duration = " + (System.nanoTime() - start));  
    }  
    private static void runTest2() throws InterruptedException {  
        Thread[] threads = new Thread[NUM_THREADS];  
        for (int i = 0; i < threads.length; i++) {  
            threads[i] = new Thread(new WellShareWorker(i));  
        }  
        final long start = System.nanoTime();  
        for (Thread t : threads) {  
            t.start();  
        }  
        for (Thread t : threads) {  
            t.join();  
        }
        System.out.println("WellShareWorker|duration = " + (System.nanoTime() - start));  
    }  
    
    
	@Contended
    public static  class VolatileLongContended { 
        public volatile long value = 0L;  
    }
	
    public static  class PlantLong  { 
        public volatile long value = 0L;  
    }
}