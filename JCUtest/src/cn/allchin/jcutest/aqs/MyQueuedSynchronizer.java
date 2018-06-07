package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自己实现的AbstractQueuedSynchronizer
 * 
 * @author renxing.zhang
 * 
 * https://www.cnblogs.com/daydaynobug/p/6752837.html
 *
 */
public class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {
	Node head;
	Node tail;
	 
    /**
     * 获取资源
     * 为什么要分2步实现，2个方法来做 ?
     * 
     *  我获取到资源了，为什么要加入队列 ? 
     *  
     * @param arg
     */
    public   void acquire_(int arg) {
        if (!tryAcquire(arg) &&
            this.acquireQueued_(addWaiter_(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    /**
     *  
     * 将当前线程加入到等待队列的队尾，并返回当前线程所在的结点
     * @param mode
     * @return
     */
    private Node addWaiter_(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
       
		// Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
    private void enq(Node node) {
		// TODO Auto-generated method stub
		
	}
	private boolean compareAndSetTail(Node pred, Node node) {
		// TODO Auto-generated method stub
		return false;
	}
	public void selfInterrupt(){};
	/**
	 * 在当前节点能获取资源 前 等待
	 * 
	 * 这个方法存在意义，看 acquire 
	 * 
	 * @param node
	 * @param arg
	 * @return
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
					setHead(node);// 拿到资源后，将head指向该结点。所以head所指的标杆结点，就是当前获取到资源的那个结点或null。
					p.next = null; // help GC //
									// setHead中node.prev已置为null，此处再将head.next置为null，就是为了方便GC回收以前的head结点。也就意味着之前拿完资源的结点出队了！
					failed = false;
					return interrupted;// 返回等待过程中是否被中断过
				}
				// 如果自己可以休息了，就进入waiting状态，直到被unpark()
				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
					interrupted = true;// 如果等待过程中被中断过，哪怕只有那么一次，就将interrupted标记为true
			}
		} finally {
			if (failed)
				cancelAcquire(node);
		}
	}

	private boolean parkAndCheckInterrupt() {
		return false;
	}

	private void cancelAcquire(Node node) {
		// TODO Auto-generated method stub

	}

	private void setHead(Node node) {

	}

	/**
	 * 这个方法存在的意义 ? 出现在acquireQueued(Node, int)
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		return false;

	}

	public static class Node {

		public Node prev;
		public static final Node EXCLUSIVE = null;
		public Object next;

		public Node(Thread currentThread, Node mode) {
			// TODO Auto-generated constructor stub
		}

		public Node predecessor() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}