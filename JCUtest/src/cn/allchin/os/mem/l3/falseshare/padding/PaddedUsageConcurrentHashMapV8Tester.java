package cn.allchin.os.mem.l3.falseshare.padding;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 问题：
 * padding 在早起的ConcurrentHashMap中是怎么使用的 ? 
 * 
 * 
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/ConcurrentHashMapV8.java?revision=1.121&view=markup
 * 
 * @author renxing.zhang
 *
 */
public class PaddedUsageConcurrentHashMapV8Tester {
	public static void main(String[] args) {

	}

	/** Number of CPUS, to place bounds on some sizings */
	static final int NCPU = Runtime.getRuntime().availableProcessors();
	// Adapted from LongAdder and Striped64.
	// See their internal docs for explanation.

	// A padded cell for distributing counts
	/**
	 * 这个分布式计数器 使用了padding
	 * 
	 * @author renxing.zhang
	 *
	 */
	static final class CounterCell {
		volatile long p0, p1, p2, p3, p4, p5, p6;
		volatile long value;
		volatile long q0, q1, q2, q3, q4, q5, q6;

		CounterCell(long x) {
			value = x;
		}
	}

	/**
	 * Holder for the thread-local hash code determining which CounterCell to
	 * use. The code is initialized via the counterHashCodeGenerator, but may be
	 * moved upon collisions.
	 */
	static final class CounterHashCode {
		int code;
	}

	/**
	 * Per-thread counter hash codes. Shared across all instances.
	 */
	static final ThreadLocal<CounterHashCode> threadCounterHashCode = new ThreadLocal<CounterHashCode>();

	final long sumCount() {
		CounterCell[] as = counterCells;
		CounterCell a;
		long sum = baseCount;
		if (as != null) {
			for (int i = 0; i < as.length; ++i) {
				if ((a = as[i]) != null)
					sum += a.value;
			}
		}
		return sum;
	}

	// See LongAdder version for explanation
	private final void fullAddCount(long x, CounterHashCode hc, boolean wasUncontended) {
		int h;
		if (hc == null) {
			hc = new CounterHashCode();
			int s = counterHashCodeGenerator.addAndGet(SEED_INCREMENT);
			h = hc.code = (s == 0) ? 1 : s; // Avoid zero
			threadCounterHashCode.set(hc);
		} else
			h = hc.code;
		boolean collide = false; // True if last slot nonempty
		for (;;) {
			CounterCell[] as;
			CounterCell a;
			int n;
			long v;
			if ((as = counterCells) != null && (n = as.length) > 0) {
				if ((a = as[(n - 1) & h]) == null) {
					if (cellsBusy == 0) { // Try to attach new Cell
						CounterCell r = new CounterCell(x); // Optimistic create
						if (cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
							boolean created = false;
							try { // Recheck under lock
								CounterCell[] rs;
								int m, j;
								if ((rs = counterCells) != null && (m = rs.length) > 0 && rs[j = (m - 1) & h] == null) {
									rs[j] = r;
									created = true;
								}
							} finally {
								cellsBusy = 0;
							}
							if (created)
								break;
							continue; // Slot is now non-empty
						}
					}
					collide = false;
				} else if (!wasUncontended) // CAS already known to fail
					wasUncontended = true; // Continue after rehash
				else if (U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))
					break;
				else if (counterCells != as || n >= NCPU)
					collide = false; // At max size or stale
				else if (!collide)
					collide = true;
				else if (cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
					try {
						if (counterCells == as) {// Expand table unless stale
							CounterCell[] rs = new CounterCell[n << 1];
							for (int i = 0; i < n; ++i)
								rs[i] = as[i];
							counterCells = rs;
						}
					} finally {
						cellsBusy = 0;
					}
					collide = false;
					continue; // Retry with expanded table
				}
				h ^= h << 13; // Rehash
				h ^= h >>> 17;
				h ^= h << 5;
			} else if (cellsBusy == 0 && counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
				boolean init = false;
				try { // Initialize table
					if (counterCells == as) {
						CounterCell[] rs = new CounterCell[2];
						rs[h & 1] = new CounterCell(x);
						counterCells = rs;
						init = true;
					}
				} finally {
					cellsBusy = 0;
				}
				if (init)
					break;
			} else if (U.compareAndSwapLong(this, BASECOUNT, v = baseCount, v + x))
				break; // Fall back on using base
		}
		hc.code = h; // Record index for next time
	}

	// Unsafe mechanics
	private static final sun.misc.Unsafe U;
	private static final long SIZECTL;
	private static final long TRANSFERINDEX;
	private static final long BASECOUNT;
	private static final long CELLSBUSY;
	private static final long CELLVALUE;
	private static final long ABASE;
	private static final int ASHIFT;

	static {
		try {
			U = getUnsafe();
			Class<?> k = PaddedUsageConcurrentHashMapV8Tester.class;
			SIZECTL = U.objectFieldOffset(k.getDeclaredField("sizeCtl"));
			TRANSFERINDEX = U.objectFieldOffset(k.getDeclaredField("transferIndex"));
			BASECOUNT = U.objectFieldOffset(k.getDeclaredField("baseCount"));
			CELLSBUSY = U.objectFieldOffset(k.getDeclaredField("cellsBusy"));
			Class<?> ck = CounterCell.class;
			CELLVALUE = U.objectFieldOffset(ck.getDeclaredField("value"));
			Class<?> ak = Node[].class;
			ABASE = U.arrayBaseOffset(ak);
			int scale = U.arrayIndexScale(ak);
			if ((scale & (scale - 1)) != 0)
				throw new Error("data type scale not a power of two");
			ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	/**
	 * Base counter value, used mainly when there is no contention, but also as
	 * a fallback during table initialization races. Updated via CAS.
	 */
	private transient volatile long baseCount;
	/**
	 * Generates initial value for per-thread CounterHashCodes.
	 */
	static final AtomicInteger counterHashCodeGenerator = new AtomicInteger();

	/**
	 * Table of counter cells. When non-null, size is a power of 2.
	 */
	private transient volatile CounterCell[] counterCells;

	/**
	 * Spinlock (locked via CAS) used when resizing and/or creating
	 * CounterCells.
	 */
	private transient volatile int cellsBusy;
	/**
	 * Increment for counterHashCodeGenerator. See class ThreadLocal for
	 * explanation.
	 */
	static final int SEED_INCREMENT = 0x61c88647;

	/**
	 * Returns a sun.misc.Unsafe. Suitable for use in a 3rd party package.
	 * Replace with a simple call to Unsafe.getUnsafe when integrating into a
	 * jdk.
	 *
	 * @return a sun.misc.Unsafe
	 */
	private static sun.misc.Unsafe getUnsafe() {
		try {
			return sun.misc.Unsafe.getUnsafe();
		} catch (SecurityException tryReflectionInstead) {
		}
		try {
			return java.security.AccessController
					.doPrivileged(new java.security.PrivilegedExceptionAction<sun.misc.Unsafe>() {
						public sun.misc.Unsafe run() throws Exception {
							Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;
							for (java.lang.reflect.Field f : k.getDeclaredFields()) {
								f.setAccessible(true);
								Object x = f.get(null);
								if (k.isInstance(x))
									return k.cast(x);
							}
							throw new NoSuchFieldError("the Unsafe");
						}
					});
		} catch (java.security.PrivilegedActionException e) {
			throw new RuntimeException("Could not initialize intrinsics", e.getCause());
		}
	}
	/* ---------------- Nodes -------------- */

	/**
	 * Key-value entry. This class is never exported out as a user-mutable
	 * Map.Entry (i.e., one supporting setValue; see MapEntry below), but can be
	 * used for read-only traversals used in bulk tasks. Subclasses of Node with
	 * a negative hash field are special, and contain null keys and values (but
	 * are never exported). Otherwise, keys and vals are never null.
	 */
	static class Node<K, V> implements Map.Entry<K, V> {
		final int hash;
		final K key;
		volatile V val;
		volatile Node<K, V> next;

		Node(int hash, K key, V val, Node<K, V> next) {
			this.hash = hash;
			this.key = key;
			this.val = val;
			this.next = next;
		}

		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return val;
		}

		public final int hashCode() {
			return key.hashCode() ^ val.hashCode();
		}

		public final String toString() {
			return key + "=" + val;
		}

		public final V setValue(V value) {
			throw new UnsupportedOperationException();
		}

		public final boolean equals(Object o) {
			Object k, v, u;
			Map.Entry<?, ?> e;
			return ((o instanceof Map.Entry) && (k = (e = (Map.Entry<?, ?>) o).getKey()) != null
					&& (v = e.getValue()) != null && (k == key || k.equals(key)) && (v == (u = val) || v.equals(u)));
		}

		/**
		 * Virtualized support for map.get(); overridden in subclasses.
		 */
		Node<K, V> find(int h, Object k) {
			Node<K, V> e = this;
			if (k != null) {
				do {
					K ek;
					if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
						return e;
				} while ((e = e.next) != null);
			}
			return null;
		}
	}
}
