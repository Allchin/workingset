package cn.allchin.os.mem.l3.falseshare.padding;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

/**
 * Q: contented 怎么在LongAdder 中发挥作用的 ?
 * A: 多线程访问同一个volitale修饰数组中的某个成员时，可能内存是连续分布，所以会存在false share 问题，
 * 使用contented 标记元素类型 ,防止产生之.
 * 
 * 扩展： CAS多线程的时候，需要不断的重试，造成很多开销，
 *  LongAdder利用xadd 操作，提供了一种解决方案.
 * 
 * 
 * 原文 https://github.com/aCoder2013/blog/issues/22
 * 
 * @author renxing.zhang
 *
 */
public class PaddedLongAdderTester {
	public static void main(String[] args) {
		LongAdder la = new LongAdder();
	}

	public static class MyLongAdder extends Striped64 implements Serializable {
		private static final long serialVersionUID = -7850373725634918240L;

		// ...
		/**
		 * <pre>
		 * 低竞争下直接更新base，类似AtomicLong 
		 * 高并发下，会将每个线程的操作hash到不同的
		 * cells数组中，从而将AtomicLong中更新 一个value的行为优化之后，
		 * 分散到多个value中
		 * 从而降低更新热点，而需要得到当前值的时候，直接 将所有cell中的value与base相加即可
		 * ，但是跟
		 * AtomicLong(compare and change -> xadd)的CAS不同， 
		 * incrementAndGet操作及其变种
		 * 可以返回更新后的值，而LongAdder返回的是void
		 * 
		 * 
		 * Q:你怎么现在是低竞争?
		 * A:不主动检测，而是用了一种很乐观的形式，就是做了一次cas，成功就是低竞争，失败就是高竞争
		 *  
		 */
		public void add(long x) {
			Cell[] as;
			long b, v;
			int m;
			Cell a;
			/**
			 * 如果是第一次执行，则直接cas 操作base
			 * 
			 */
			if ((as = cells) != null || !casBase(b = base, b + x)) {
				//如果快速的cas失败了,将x增加到对应cpu桶中的值，桶as的索引就是cpu哈希得到的
				boolean uncontended = true;
				/**
				 * as数组为空(null或者size为0) 或者当前线程取模as数组大小为空 或者cas更新Cell失败
				 */
				if (as == null || (m = as.length - 1) < 0 || (a = as[getProbe() & m]) == null
						|| !(uncontended = a.cas(v = a.value, v + x)))
					longAccumulate(x, null, uncontended);
			}
		}

		public long sum() {
			// 通过累加base与cells数组中的value从而获得sum
			Cell[] as = cells;
			Cell a;
			long sum = base;
			if (as != null) {
				for (int i = 0; i < as.length; ++i) {
					if ((a = as[i]) != null)
						sum += a.value;
				}
			}
			return sum;
		}

		@Override
		public int intValue() { 
			return 0;
		}

		@Override
		public long longValue() { 
			return 0;
		}

		@Override
		public float floatValue() { 
			return 0;
		}

		@Override
		public double doubleValue() { 
			return 0;
		}
	}

	/**
	 * <pre>
	 * java.util.concurrent.atomic.Striped64 不是public 的
	 * 
	 * 
	 * LongAdder继承自Striped64， Striped64内部维护了一个懒加载的数组以及一个额外的base实力域，
	 * 数组的大小是2的N次方，使用每个线程Thread内部的哈希值访问。
	 * 
	 * 
	 * @author renxing.zhang
	 *
	 */
	static abstract class Striped64 extends Number {
		private static final sun.misc.Unsafe UNSAFE;
		private static final long BASE;
		private static final long CELLSBUSY;
		private static final long PROBE;

		static {
			try {
				UNSAFE = sun.misc.Unsafe.getUnsafe();
				Class<?> sk = Striped64.class;
				BASE = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
				CELLSBUSY = UNSAFE.objectFieldOffset(sk.getDeclaredField("cellsBusy"));
				Class<?> tk = Thread.class;
				PROBE = UNSAFE.objectFieldOffset(tk.getDeclaredField("threadLocalRandomProbe"));
			} catch (Exception e) {
				throw new Error(e);
			}
		}

		/** Number of CPUS, to place bound on table size */
		static final int NCPU = Runtime.getRuntime().availableProcessors();

		/**
		 * Table of cells. When non-null, size is a power of 2.
		 */
		transient volatile Cell[] cells;
		/**
		 * Base value, used mainly when there is no contention, but also as a
		 * fallback during table initialization races. Updated via CAS.
		 */
		transient volatile long base;

		/**
		 * Spinlock (locked via CAS) used when resizing and/or creating Cells.
		 */
		transient volatile int cellsBusy;

		/**
		 * CASes the base field.
		 */
		final boolean casBase(long cmp, long val) {
			return UNSAFE.compareAndSwapLong(this, BASE, cmp, val);
		}
	    /**
	     * Returns the probe value for the current thread.
	     * Duplicated from ThreadLocalRandom because of packaging restrictions.
	     */
	    static final int getProbe() {
	        return UNSAFE.getInt(Thread.currentThread(), PROBE);
	    }

	    /**
	     * CASes the cellsBusy field from 0 to 1 to acquire lock.
	     */
	    final boolean casCellsBusy() {
	        return UNSAFE.compareAndSwapInt(this, CELLSBUSY, 0, 1);
	    }
	    
	    /**
	     * Handles cases of updates involving initialization, resizing,
	     * creating new Cells, and/or contention. See above for
	     * explanation. This method suffers the usual non-modularity
	     * problems of optimistic retry code, relying on rechecked sets of
	     * reads.
	     *
	     * @param x the value
	     * @param fn the update function, or null for add (this convention
	     * avoids the need for an extra field or function in LongAdder).
	     * @param wasUncontended false if CAS failed before call
	     */
	    final void longAccumulate(long x, LongBinaryOperator fn,
	                              boolean wasUncontended) {
	        int h;
	        if ((h = getProbe()) == 0) {
	            ThreadLocalRandom.current(); // force initialization
	            h = getProbe();
	            wasUncontended = true;
	        }
	        boolean collide = false;                // True if last slot nonempty
	        for (;;) {
	            Cell[] as; Cell a; int n; long v;
	            if ((as = cells) != null && (n = as.length) > 0) {
	                if ((a = as[(n - 1) & h]) == null) {
	                    if (cellsBusy == 0) {       // Try to attach new Cell
	                        Cell r = new Cell(x);   // Optimistically create
	                        if (cellsBusy == 0 && casCellsBusy()) {
	                            boolean created = false;
	                            try {               // Recheck under lock
	                                Cell[] rs; int m, j;
	                                if ((rs = cells) != null &&
	                                    (m = rs.length) > 0 &&
	                                    rs[j = (m - 1) & h] == null) {
	                                    rs[j] = r;
	                                    created = true;
	                                }
	                            } finally {
	                                cellsBusy = 0;
	                            }
	                            if (created)
	                                break;
	                            continue;           // Slot is now non-empty
	                        }
	                    }
	                    collide = false;
	                }
	                else if (!wasUncontended)       // CAS already known to fail
	                    wasUncontended = true;      // Continue after rehash
	                else if (a.cas(v = a.value, ((fn == null) ? v + x :
	                                             fn.applyAsLong(v, x))))
	                    break;
	                else if (n >= NCPU || cells != as)
	                    collide = false;            // At max size or stale
	                else if (!collide)
	                    collide = true;
	                else if (cellsBusy == 0 && casCellsBusy()) {
	                    try {
	                        if (cells == as) {      // Expand table unless stale
	                            Cell[] rs = new Cell[n << 1];
	                            for (int i = 0; i < n; ++i)
	                                rs[i] = as[i];
	                            cells = rs;
	                        }
	                    } finally {
	                        cellsBusy = 0;
	                    }
	                    collide = false;
	                    continue;                   // Retry with expanded table
	                }
	                h = advanceProbe(h);
	            }
	            else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
	                boolean init = false;
	                try {                           // Initialize table
	                    if (cells == as) {
	                        Cell[] rs = new Cell[2];
	                        rs[h & 1] = new Cell(x);
	                        cells = rs;
	                        init = true;
	                    }
	                } finally {
	                    cellsBusy = 0;
	                }
	                if (init)
	                    break;
	            }
	            else if (casBase(v = base, ((fn == null) ? v + x :
	                                        fn.applyAsLong(v, x))))
	                break;                          // Fall back on using base
	        }
	    }
	    
	    /**
	     * Pseudo-randomly advances and records the given probe value for the
	     * given thread.
	     * Duplicated from ThreadLocalRandom because of packaging restrictions.
	     */
	    static final int advanceProbe(int probe) {
	        probe ^= probe << 13;   // xorshift
	        probe ^= probe >>> 17;
	        probe ^= probe << 5;
	        UNSAFE.putInt(Thread.currentThread(), PROBE, probe);
	        return probe;
	    }
	    
	}

	/**
	 * <pre>
	 * Cell类用Contended注解修饰，这里主要是解决false sharing(伪共享的问题)
	 * 
	 * Q:伪共享的问题 两个变量被分配到了同一个缓存行， 因此会造成每个线程都去争抢缓存行的所有权，
	 * 例如A获取了所有权然后执行更新这时由于volatile的语义会造成其刷新到主存， 但是由于变量b也被缓存到同一个缓存行， 因此就会造成cache
	 * miss，这样就会造成极大的性能损失
	 * 
	 * A: @sun.misc.Contended解用于解决这个问题,由JVM去插入这些变量，
	 * 具体可以参考openjdk.java.net/jeps/142 ， 但是通常来说对象是不规则的分配到内存中的， 但是数组由于是连续的内存，
	 * 因此可能会共享缓存行， 因此这里加一个Contended注解以防cells数组发生伪共享的情况。
	 * 
	 * 所以这里有使用Contended修饰的可能的场景： 一个是并发线程访问，可能是被volitale修饰的变量，一个是数组
	 * 
	 * Q:相同jvm内，即便是不同对象不规则分配到内存，有没有可能也分配到共享缓存行里面?
	 * TODO 
	 * 
	 * 
	 * @author renxing.zhang
	 *
	 */
	@sun.misc.Contended
	static final class Cell {
		volatile long value;

		Cell(long x) {
			value = x;
		}

		final boolean cas(long cmp, long val) {
			return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
		}

		// Unsafe mechanics
		private static final sun.misc.Unsafe UNSAFE;
		private static final long valueOffset;

		static {
			try {
				UNSAFE = sun.misc.Unsafe.getUnsafe();
				Class<?> ak = Cell.class;
				valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
}
