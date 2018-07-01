package cn.allchin.os.mem.l3.falseshare.padding;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

/**
 * Q: contented ��ô��LongAdder �з������õ� ?
 * A: ���̷߳���ͬһ��volitale���������е�ĳ����Աʱ�������ڴ��������ֲ������Ի����false share ���⣬
 * ʹ��contented ���Ԫ������ ,��ֹ����֮.
 * 
 * ��չ�� CAS���̵߳�ʱ����Ҫ���ϵ����ԣ���ɺܶ࿪����
 *  LongAdder����xadd �������ṩ��һ�ֽ������.
 * 
 * 
 * ԭ�� https://github.com/aCoder2013/blog/issues/22
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
		 * �;�����ֱ�Ӹ���base������AtomicLong 
		 * �߲����£��Ὣÿ���̵߳Ĳ���hash����ͬ��
		 * cells�����У��Ӷ���AtomicLong�и��� һ��value����Ϊ�Ż�֮��
		 * ��ɢ�����value��
		 * �Ӷ����͸����ȵ㣬����Ҫ�õ���ǰֵ��ʱ��ֱ�� ������cell�е�value��base��Ӽ���
		 * �����Ǹ�
		 * AtomicLong(compare and change -> xadd)��CAS��ͬ�� 
		 * incrementAndGet�����������
		 * ���Է��ظ��º��ֵ����LongAdder���ص���void
		 * 
		 * 
		 * Q:����ô�����ǵ;���?
		 * A:��������⣬��������һ�ֺ��ֹ۵���ʽ����������һ��cas���ɹ����ǵ;�����ʧ�ܾ��Ǹ߾���
		 *  
		 */
		public void add(long x) {
			Cell[] as;
			long b, v;
			int m;
			Cell a;
			/**
			 * ����ǵ�һ��ִ�У���ֱ��cas ����base
			 * 
			 */
			if ((as = cells) != null || !casBase(b = base, b + x)) {
				//������ٵ�casʧ����,��x���ӵ���ӦcpuͰ�е�ֵ��Ͱas����������cpu��ϣ�õ���
				boolean uncontended = true;
				/**
				 * as����Ϊ��(null����sizeΪ0) ���ߵ�ǰ�߳�ȡģas�����СΪ�� ����cas����Cellʧ��
				 */
				if (as == null || (m = as.length - 1) < 0 || (a = as[getProbe() & m]) == null
						|| !(uncontended = a.cas(v = a.value, v + x)))
					longAccumulate(x, null, uncontended);
			}
		}

		public long sum() {
			// ͨ���ۼ�base��cells�����е�value�Ӷ����sum
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
	 * java.util.concurrent.atomic.Striped64 ����public ��
	 * 
	 * 
	 * LongAdder�̳���Striped64�� Striped64�ڲ�ά����һ�������ص������Լ�һ�������baseʵ����
	 * ����Ĵ�С��2��N�η���ʹ��ÿ���߳�Thread�ڲ��Ĺ�ϣֵ���ʡ�
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
	 * Cell����Contendedע�����Σ�������Ҫ�ǽ��false sharing(α���������)
	 * 
	 * Q:α��������� �������������䵽��ͬһ�������У� ��˻����ÿ���̶߳�ȥ���������е�����Ȩ��
	 * ����A��ȡ������ȨȻ��ִ�и�����ʱ����volatile������������ˢ�µ����棬 �������ڱ���bҲ�����浽ͬһ�������У� ��˾ͻ����cache
	 * miss�������ͻ���ɼ����������ʧ
	 * 
	 * A: @sun.misc.Contended�����ڽ���������,��JVMȥ������Щ������
	 * ������Բο�openjdk.java.net/jeps/142 �� ����ͨ����˵�����ǲ�����ķ��䵽�ڴ��еģ� ���������������������ڴ棬
	 * ��˿��ܻṲ�����У� ��������һ��Contendedע���Է�cells���鷢��α����������
	 * 
	 * ����������ʹ��Contended���εĿ��ܵĳ����� һ���ǲ����̷߳��ʣ������Ǳ�volitale���εı�����һ��������
	 * 
	 * Q:��ͬjvm�ڣ������ǲ�ͬ���󲻹�����䵽�ڴ棬��û�п���Ҳ���䵽������������?
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
