package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.AbstractQueuedSynchronizer.Node;

import sun.misc.Unsafe;

/**
 *  <pre>
 * 自己实现的AbstractQueuedSynchronizer
 * 
 * @author renxing.zhang
 * 
 *         https://www.cnblogs.com/daydaynobug/p/6752837.html
 * 
 *         同步器 ，为什么设计同步器 http://ifeve.com/aqs-2/
 * 
 *         论文：http://gee.cs.oswego.edu/dl/papers/aqs.pdf
 *         翻译：https://blog.csdn.net/FAw67J7/article/details/79885944
 *
 */
@SuppressWarnings("restriction")
public class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {
	private static final Unsafe unsafe = Unsafe.getUnsafe();
	private static final long stateOffset;
	private static final long headOffset;
	private static final long tailOffset;
	private static final long waitStatusOffset;
	private static final long nextOffset;

	static {
		try {
			stateOffset = unsafe.objectFieldOffset(MyQueuedSynchronizer.class.getDeclaredField("state"));
			headOffset = unsafe.objectFieldOffset(MyQueuedSynchronizer.class.getDeclaredField("head"));
			tailOffset = unsafe.objectFieldOffset(MyQueuedSynchronizer.class.getDeclaredField("tail"));
			waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
			nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));

		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	Node head;
	Node tail;

	/**
	 *  <pre>
	 * 此方法是独占模式下线程释放共享资源的顶层入口。
	 * 它会释放指定量的资源，如果彻底释放了（即state=0）, 
	 * 它会唤醒等待队列里的其他线程来获取资源。
	 * 这也正是unlock()的语义，当然不仅仅只限于unlock()。下面是release()的源码：
	 * 
	 * 
	 * @param arg
	 * @return
	 */
	public final boolean release_(int arg) {
		if (tryRelease(arg)) {
			Node h = head;// 找到头结点
			if (h != null && h.waitStatus != 0)
				unparkSuccessor(h);// 唤醒等待队列里的下一个线程
			return true;
		}
		return false;
	}

	/**
	 * 唤醒node后面的有效的节点
	 * @param node
	 */
	private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         * Q:为啥要设置成 0 
         * ???
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        Node s = node.next;
        /**
         * 如果位于第二的元素为空，或者第二元素已经被取消执行了
         * */
        if (s == null || s.waitStatus > 0) {
            s = null;
            /**
             * Q:为啥从后向前找啊，从前往后不是更快么？
             * A: 因为第二元素已经被取消，那他的next实际上就不准了，或者是null了
             * 
             * 从尾巴一直向前找，找到最前面的需要被unaprk的节点
             * */
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

	/**
	 *  <pre>
	 * 获取资源 为什么要分2步实现，2个方法来做 ?
	 * 
	 * 我获取到资源了，为什么要加入队列 ?
	 * 
	 * 
	 * 1调用自定义同步器的tryAcquire()尝试直接去获取资源，如果成功则直接返回； 2
	 * 没成功，则addWaiter()将该线程加入等待队列的尾部，并标记为独占模式； 3
	 * acquireQueued()使线程在等待队列中休息，有机会时（轮到自己，会被unpark()）会去尝试获取资源。获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。
	 * 4 如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
	 * 由于此函数是重中之重，我再用流程图总结一下：
	 * 
	 * 
	 * @param arg
	 */
	public void acquire_(int arg) {
		/**
		 * 1调用自定义同步器的tryAcquire()尝试直接去获取资源，如果成功则直接返回； 2
	 * 没成功，则addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
		 * */
		if (!tryAcquire(arg) && this.acquireQueued_(addWaiter_(Node.EXCLUSIVE), arg))
			selfInterrupt();
	}

	/**
	 * 和acquire_语义相同
	 * 
	 * @param arg
	 */
	public void acquire2(int arg) {
		if (!tryAcquire(arg)) {
			// 创建并新入队 新节点
			Node node = addWaiter_(Node.EXCLUSIVE);

			Node pred = node.predecessor();
			// head 时，结束；tryAcquire 成功时结束
			while (pred != head || !tryAcquire(arg)) {
				if (pred.waitStatus == Node.SIGNAL) {
					// Q: 我park了，什么时候unpark 呢
					// A :release 的时候
					LockSupport.park(this);// 调用park()使线程进入waiting状态
				} else {
					// Q:将我的前一项设置为SIGNAL
					// A:让前一个节点可以被unpark
					compareAndSetWaitStatus(pred, pred.waitStatus, Node.SIGNAL);

					// Q:为啥要重新获取pred,那不就再进来的时候就一定park了么
					// A:重新获取pred才有机会获取到head
					pred = node.predecessor();
				}
			}
		}
	}

	/**
	 * 
	 * 将当前线程加入到等待队列的队尾，并返回当前线程所在的结点
	 * 
	 * @param mode
	 * @return
	 */
	private Node addWaiter_(Node mode) {
		Node node = new Node(Thread.currentThread(), mode);

		// Try the fast path of enq; backup to full enq on failure
		Node pred = tail;
		if (pred != null) {
			node.prev = pred;
			/**
			 * 用cas快速的尝试入队，只尝试一次,如果失败了，就再去兜底
			 */
			if (compareAndSetTail(pred, node)) {
				pred.next = node;
				return node;
			}
		}
		/**
		 * 如果上面用cas不行的话，用enq方法兜底
		 */
		enq(node);
		return node;
	}

	/**
	 *  <pre>
	 * 入队
	 * 
	 * 不断cas重试
	 * 
	 * @param node
	 */
	private Node enq(Node node) {
		for (;;) { // 不断重试

			Node t = tail;
			if (t == null) { // Must initialize
				/**
				 * 如果尾巴是空，说明还没初始化呢，就初始化一下 初始化完尾巴和头是同样同一个对象
				 */
				if (compareAndSetHead(new Node()))
					tail = head;
			} else {
				node.prev = t;
				if (compareAndSetTail(t, node)) {
					t.next = node;
					return t;
				}
			}
		}
	}

	private boolean compareAndSetHead(Node node) {
		return unsafe.compareAndSwapObject(this, headOffset, null, node);
	}

	/**
	 * //原子的执行 if(tail == pred ) tail =node;
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	private boolean compareAndSetTail(Node pred, Node node) {
		return unsafe.compareAndSwapObject(this, tailOffset, pred, node);

	}

	public void selfInterrupt() {
		Thread.currentThread().interrupt();
	};

	/**
	 * <pre>
	 * 在当前节点能获取资源 前 等待
	 * 
	 * 这个方法存在意义，看 acquire
	 * 
	 * 1结点进入队尾后，检查状态，找到安全休息点； 2调用park()进入waiting状态，等待unpark()或interrupt()唤醒自己；
	 * 3被唤醒后，看自己是不是有资格能拿到号。如果拿到，head指向当前结点，并返回从入队到拿到号的整个过程中是否被中断过；如果没拿到，继续流程1
	 * 
	 * 
	 * @param node
	 * @param arg
	 * @return true 是否中断过
	 */
	boolean acquireQueued_(final Node node, int arg) {
		boolean failed = true;// 标记是否成功拿到资源
		try {
			boolean interrupted = false;
			// 标记等待过程中是否被中断过
			for (;;) {
				final Node p = node.predecessor();
				// 如果前驱是head，即该结点已成老二，那么便有资格去尝试获取资源（可能是老大释放完资源唤醒自己的，当然也可能被interrupt了）。

				if (p == head && tryAcquire(arg)) {
					/**
					 * 当前节点作为第二节点，但是获取到了资源
					 * 
					 * 获取到资源自己就成了头
					 */
					setHead(node);// 拿到资源后，将head指向该结点。所以head所指的标杆结点，就是当前获取到资源的那个结点或null。
					/**
					 * 头节点出列后将他的成员引用设置null // help GC // //
					 * setHead中node.prev已置为null，此处再将head.next置为null，就是为了方便GC回收以前的head结点。也就意味着之前拿完资源的结点出队了！
					 * 
					 */
					p.next = null;

					failed = false;
					return interrupted;// 返回等待过程中是否被中断过
				}
				/**
				 * 获取不到就park
				 * 先看下是不是真的需要park，如果自己也被中断了，就不用park了
				 * */
				// 如果自己可以休息了，就进入waiting状态，直到被unpark()
				/**
				 * shouldParkAfterFailedAcquire返回true 表示需要进行park
				 * parkAndCheckInterrupt 进行park, 返回true表示被中断过
				 * */
				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
					interrupted = true;// 如果等待过程中被中断过，哪怕只有那么一次，就将interrupted标记为true
			}
		} finally {
			if (failed)
				cancelAcquire(node);
		}
	}

	/**
	 *  <pre>
	 * 如果线程找好安全休息点后，那就可以安心去休息了。 此方法就是让线程去休息，真正进入等待状态。
	 * 
	 * @return
	 */
	private boolean parkAndCheckInterrupt() {

		LockSupport.park(this);// 调用park()使线程进入waiting状态
		return Thread.interrupted();// 如果被唤醒，查看自己是不是被中断的。

	}

	private void cancelAcquire(Node node) {
		// TODO Auto-generated method stub

	}

	private void setHead(Node node) {
		head = node;
		node.thread = null;
		node.prev = null;
	}

	/**
	 * <pre>
	 * Q:这个方法存在的意义 ? 出现在acquireQueued(Node, int)
	 * A:先看下是不是真的需要park，如果自己也被中断了，就不用park了
	 * 
	 * 
	 * 
	 * 此方法主要用于检查状态， 看看自己是否真的可以去休息了 万一队列前边的线程都放弃了只是瞎站着，那也说不定，对吧！
	 * 
	 * 整个流程中，如果前驱结点的状态不是SIGNAL，那么自己就不能安心去休息， 需要去找个安心的休息点，同时可以再尝试下看有没有机会轮到自己拿号。
	 * 
	 *
	 * @param pred
	 * @param node
	 * @return
	 * </pre>
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		int ws = pred.waitStatus;// 拿到前驱的状态
		if (ws == Node.SIGNAL)
			/**
			 * 如果我之前的节点表明他准备号被unparking,那我自己肯定是需要被park的
			 * */
			// 如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
			return true;
		if (ws > 0) {
			/*
			 * 大于0的值就1个，就是被取消。
			 * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
			 * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会被保安大叔赶走了(GC回收)！
			 */
			do {
				node.prev = pred = pred.prev;
			} while (pred.waitStatus > 0);
			pred.next = node;
		} else {
			// 如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
			/**
			 * 前驱节点没>0 ,那就是在等待条件或者需要被直接唤醒，
			 * 那就设置他的状态成为需要unparking
			 * */
			compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
		}
		return false;

	}

	private static void compareAndSetWaitStatus(Node pred, int ws, int signal) {
		unsafe.compareAndSwapInt(pred, waitStatusOffset, ws, signal); 

	}

	public static class Node {

		public Thread thread;
        /** 线程在等待条件 */
        static final int CONDITION = -2;
        /**
         * 线程在这个标记下，在下个acquireShared的时候应该无条件被晋升执行。
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;

		/**
		 * 当waitStatus是signal时表示 准备好了被unparking
		 *  waitStatus value to indicate successor's
		 * thread needs unparking
		 */
		static final int SIGNAL = -1;
        /**  这个线程已经被取消 */
        static final int CANCELLED =  1;
        
		/**
		 * 是负数表示正在等待unpark
		 */
		public int waitStatus;
		public Node prev;
		public static final Node EXCLUSIVE = null;
		public Node next;

		public Node(Thread currentThread, Node mode) {

			// TODO Auto-generated constructor stub
		}

		public Node() {
			// TODO Auto-generated constructor stub
		}

		/**
		 *  <pre>
		 * 返回前一个结点,如果是null跑出npe 在前一个节点不可能是null时候去使用。 null check 可以被省略，但是 可以帮助vm ?
		 * 
		 * 为什么不直接返回prev ?
		 * 
		 * @return
		 */
		public Node predecessor() {
			Node p = prev;
			if (p == null) {
				// 为什么要判空 ?
				throw new NullPointerException();
			}

			return p;
		}
	}
}