package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * 自己实现的AbstractQueuedSynchronizer
 * 
 * @author renxing.zhang
 * 
 * https://www.cnblogs.com/daydaynobug/p/6752837.html
 * 
 * 同步器 ，为什么设计同步器
 * http://ifeve.com/aqs-2/
 * 
 * 论文：http://gee.cs.oswego.edu/dl/papers/aqs.pdf
 * 翻译：https://blog.csdn.net/FAw67J7/article/details/79885944
 *
 */
public class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {
	Node head;
	Node tail;
	
	/**
	 * 此方法是独占模式下线程释放共享资源的顶层入口。它会释放指定量的资源，如果彻底释放了（即state=0）,
	 * 它会唤醒等待队列里的其他线程来获取资源。
	 * 这也正是unlock()的语义，当然不仅仅只限于unlock()。下面是release()的源码：
	 * 
	 * 
	 * @param arg
	 * @return
	 */
	public final boolean release_(int arg) {
	    if (tryRelease(arg)) {
	        Node h = head;//找到头结点
	        if (h != null && h.waitStatus != 0)
	            unparkSuccessor(h);//唤醒等待队列里的下一个线程
	        return true;
	    }
	    return false;
	}
    
	 
    private void unparkSuccessor(Node h) {
		// TODO Auto-generated method stub
		
	}


	/**
     * 获取资源
     * 为什么要分2步实现，2个方法来做 ?
     * 
     *  我获取到资源了，为什么要加入队列 ? 
     *  
     *  
     *  1调用自定义同步器的tryAcquire()尝试直接去获取资源，如果成功则直接返回；
2 没成功，则addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
3 acquireQueued()使线程在等待队列中休息，有机会时（轮到自己，会被unpark()）会去尝试获取资源。获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。
4 如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
由于此函数是重中之重，我再用流程图总结一下：

     *  
     * @param arg
     */
    public   void acquire_(int arg) {
        if (!tryAcquire(arg) &&
            this.acquireQueued_(addWaiter_(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    public void acquire2(int arg){
    	if(!tryAcquire(arg)){
    		//创建并新入队 新节点
    		Node node=addWaiter_(Node.EXCLUSIVE) ;
    		
    		Node pred= node.predecessor();
    		//head 时，结束；tryAcquire 成功时结束
    		while(pred != head || !tryAcquire(arg)){
    			if(pred.waitStatus == Node.SIGNAL){
    				LockSupport.park(this);//调用park()使线程进入waiting状态
    			}
    			else{
    				compareAndSetWaitStatus(pred, pred.waitStatus, Node.SIGNAL);
    				pred=node.predecessor();
    			}
    		}
    	}
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
	 * 1结点进入队尾后，检查状态，找到安全休息点；
2调用park()进入waiting状态，等待unpark()或interrupt()唤醒自己；
3被唤醒后，看自己是不是有资格能拿到号。如果拿到，head指向当前结点，并返回从入队到拿到号的整个过程中是否被中断过；如果没拿到，继续流程1

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

	/**
	 * 如果线程找好安全休息点后，那就可以安心去休息了。
	 * 此方法就是让线程去休息，真正进入等待状态。
	 * @return
	 */
	private boolean parkAndCheckInterrupt() {
		
		  LockSupport.park(this);//调用park()使线程进入waiting状态
		  return Thread.interrupted();//如果被唤醒，查看自己是不是被中断的。
		 
	}

	private void cancelAcquire(Node node) {
		// TODO Auto-generated method stub

	}

	private void setHead(Node node) {

	}

	/**
	 * 这个方法存在的意义 ? 出现在acquireQueued(Node, int)
	 * 
	 * 
	 * 此方法主要用于检查状态，
	 * 看看自己是否真的可以去休息了
	 * 万一队列前边的线程都放弃了只是瞎站着，那也说不定，对吧！
	 * 
	 * 整个流程中，如果前驱结点的状态不是SIGNAL，那么自己就不能安心去休息，
	 * 需要去找个安心的休息点，同时可以再尝试下看有没有机会轮到自己拿号。
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
	    int ws = pred.waitStatus;//拿到前驱的状态
	    if (ws == Node.SIGNAL)
	        //如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
	        return true;
	    if (ws > 0) {
	        /*
	         * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
	         * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会被保安大叔赶走了(GC回收)！
	         */
	        do {
	            node.prev = pred = pred.prev;
	        } while (pred.waitStatus > 0);
	        pred.next = node;
	    } else {
	         //如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
	        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
	    }
	    return false;

	}

	private static void compareAndSetWaitStatus(Node pred, int ws, int signal) {
		// TODO Auto-generated method stub
		
	}

	public static class Node {

		public static final int SIGNAL = 0;
		public int waitStatus;
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