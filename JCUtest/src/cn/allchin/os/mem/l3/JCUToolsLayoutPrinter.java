package cn.allchin.os.mem.l3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 打印常见JCU 工具的大小
 * @author renxing.zhang
 *
 */
import sun.misc.Contended;

/**
 *
<pre>

 * @author renxing.zhang
 *
 */
public class JCUToolsLayoutPrinter implements FalseShare {
	
	static {
		System.out.println(System.getProperties().get("java.version"));
		System.out.println(VM.current().details());
		String layout = ClassLayout.parseClass(CountDownLatch.class).toPrintable();
		System.out.println(layout);

		layout = ClassLayout.parseClass(ReentrantLock.class).toPrintable();
		System.out.println(layout);

		layout = ClassLayout.parseClass(AtomicInteger.class).toPrintable();
		System.out.println(layout);

		layout = ClassLayout.parseClass(AtomicReference.class).toPrintable();
		System.out.println(layout);

		layout = ClassLayout.parseClass(AtomicLong.class).toPrintable();
		System.out.println(layout);
		

		layout = ClassLayout.parseClass(AlingmentAtomicInteger.class).toPrintable();
		System.out.println(layout);
		layout = ClassLayout.parseClass(ManualAlingmentAtomicInteger.class).toPrintable();
		System.out.println(layout);

	}
	
	public static void main(String[] args) throws InterruptedException { }
  

	/**
	 * @author renxing.zhang
	 *
	 */
	@Contended
	public static class AlingmentAtomicInteger extends AtomicInteger {

		public AlingmentAtomicInteger(int i) {
			set(i);
		}

	}
	
	public static class ManualAlingmentAtomicInteger extends AtomicInteger {
		private long p1,p2,p3,p4,p5,p6;
		public ManualAlingmentAtomicInteger(int i) {
			set(i);
			p1=i;
			p2=i;
			p3=i;
			p4=i;
			p5=i;
			p6=i;
		}

	}
}
