package cn.allchin.os.mem.l3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * 我想对FalseShareJava6 做个重构，但是为啥结果差异这么大
 * 这个结果还有点疑问？
 * 为什么size 超过64的反而更快了
 * 
class cn.allchin.os.mem.l3.FalseShareJavaAllchin6$Size32|avg|15055455725
class cn.allchin.os.mem.l3.FalseShareJavaAllchin6$Size56|avg|15758899288
class cn.allchin.os.mem.l3.FalseShareJavaAllchin6$Size64|avg|11574395634
class cn.allchin.os.mem.l3.FalseShareJavaAllchin6$Size72|avg|12044148643
class cn.allchin.os.mem.l3.FalseShareJavaAllchin6$Size80|avg|11533109017

 * @author citi0
 *
 */
public class FalseShareJavaAllchin6 implements Runnable {
	public final static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;

	private static VolatileType[] longs = new VolatileType[NUM_THREADS];

	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileType();
		}
	}

	public FalseShareJavaAllchin6(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	/**
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		System.out.println(System.getProperties().get("java.version"));
		System.out.println(VM.current().details()); 
		Class[] clazzs=new Class[]{Size80.class,Size72.class,Size64.class,Size56.class,Size32.class};
		for(int i=0;i<clazzs.length;i++){
			String layout = ClassLayout.parseClass(clazzs[i]).toPrintable();
			System.out.println(layout); 
		}
		Map<Class,List<Long>> map=new HashMap<Class,List<Long>>();
		int retryTimes=7;
		for(int times=0;times<retryTimes;times++){
			for(int classIdx=0;classIdx<clazzs.length;classIdx++){
				Class clazz=clazzs[classIdx];
				List<Long> longList = map.get(clazz);
				if(longList == null){
					longList=new ArrayList<Long>();
					map.put(clazz, longList);
				}
				long during=work(clazz);
				longList.add(during); 
			}
		} 
		System.out.println(map);
		for(Class clazz:map.keySet()){
			long total=0;
			for(Long l:map.get(clazz)){
				total+=l;
			}
			long avg=total/map.get(clazz).size();
			System.out.println(clazz+"|avg|"+avg);
		}
	}
	private static long work(Class<? extends VolatileType > clazz) throws InstantiationException, IllegalAccessException, InterruptedException{
	
 
		for (int i = 0; i < longs.length; i++) {
			//new Instance是不是比较慢啊,但是不影响测试才对
			longs[i] = clazz.newInstance();
		}  
		Thread[] threads = new Thread[NUM_THREADS];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseShareJavaAllchin6(i));
		}
		Thread.sleep(1000);
		final long start = System.nanoTime();
		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
		long during=System.nanoTime() - start;
		System.out.println(longs[0] + "测试运行时间|duration = " + (during));
		return during;
	}

	/**
	 * 消耗cpu的方法， 数组赋值
	 */
	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}

	public static class VolatileType {
		public volatile long value = 0L;
	}

	/**
	 * 填充适用于1.6
	 * 
	 * @author citi0 size 72
	 */
	public final static class Size72 extends VolatileType {

		public long p1, p2, p3, p4, p5, p6; // comment out

	}
	public final static class Size32 extends VolatileType {

		public long   p6; // comment out

	}

	public final static class Size80 extends VolatileType {

		public long p0, p1, p2, p3, p4, p5, p6; // comment out

	}

	public final static class Size64 extends VolatileType {

		public long p2, p3, p4, p5, p6; // comment out
	}

	public final static class Size56 extends VolatileType {

		public long p3, p4, p5, p6; // comment out
	}
}