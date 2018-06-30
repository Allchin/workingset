package cn.allchin.os.mem.l3.falseshare.padding;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * <pre>
 * 问题：
 * 查看padding 是如何在LinkedTransferQueue(since 1.5)中使用的，
 * 得出padding 的使用模式
 * 
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166y/LinkedTransferQueue.java?revision=1.1&view=markup
 * 步骤：
 * 我把所有padding优化的类，以及使用到这个类的代码都copy下来了
 * 
 * main执行结果:
 * 为啥我们看到是80字节对象大小
 1.8.0_72
# Running 64-bit HotSpot VM.
# Using compressed oop with 0-bit shift.
# Using compressed klass with 0-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.os.mem.l3.falseshare.padding.LinkedTransferQueueTester$PaddedAtomicReference object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0    12                    (object header)                           N/A
     12     4   java.lang.Object AtomicReference.value                     N/A
     16     4   java.lang.Object PaddedAtomicReference.p0                  N/A
     20     4   java.lang.Object PaddedAtomicReference.p1                  N/A
     24     4   java.lang.Object PaddedAtomicReference.p2                  N/A
     28     4   java.lang.Object PaddedAtomicReference.p3                  N/A
     32     4   java.lang.Object PaddedAtomicReference.p4                  N/A
     36     4   java.lang.Object PaddedAtomicReference.p5                  N/A
     40     4   java.lang.Object PaddedAtomicReference.p6                  N/A
     44     4   java.lang.Object PaddedAtomicReference.p7                  N/A
     48     4   java.lang.Object PaddedAtomicReference.p8                  N/A
     52     4   java.lang.Object PaddedAtomicReference.p9                  N/A
     56     4   java.lang.Object PaddedAtomicReference.pa                  N/A
     60     4   java.lang.Object PaddedAtomicReference.pb                  N/A
     64     4   java.lang.Object PaddedAtomicReference.pc                  N/A
     68     4   java.lang.Object PaddedAtomicReference.pd                  N/A
     72     4   java.lang.Object PaddedAtomicReference.pe                  N/A
     76     4                    (loss due to the next object alignment)
Instance size: 80 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


 * 
 * @author renxing.zhang
 *
 */
public class PaddedUsageLinkedTransferQueueTester {

 
	static{
		System.out.println(System.getProperties().get("java.version"));
		System.out.println(VM.current().details());
		String layout = ClassLayout.parseClass(PaddedAtomicReference.class).toPrintable();
		System.out.println(layout);
	}
	public static void main(String[] args) {
		LinkedTransferQueue q;
	}
	/**
	 * 在文中我们能够看到doug lea 使用padding 构造了自己的AtomicReference实现
	 * 
     * Padded version of AtomicReference used for head, tail and
     * cleanMe, to alleviate contention across threads CASing one vs
     * the other.
     */
    static final class PaddedAtomicReference<T> extends AtomicReference<T> {
        // enough padding for 64bytes with 4byte refs
        Object p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, pa, pb, pc, pd, pe;
        PaddedAtomicReference(T r) { super(r); }
    }
    

    private final QNode dummy = new QNode(null, false);
    
    /**
     * Q:padded 的节点主要用在队列的头和尾，
     * 看起来头尾被竞争使用最厉害 ？
     * 
     *  
     * */
    private final PaddedAtomicReference<QNode> head = 
        new PaddedAtomicReference<QNode>(dummy);
    private final PaddedAtomicReference<QNode> tail = 
        new PaddedAtomicReference<QNode>(dummy);

    /**
     * Reference to a cancelled node that might not yet have been
     * unlinked from queue because it was the last inserted node
     * when it cancelled.
     */
    private final PaddedAtomicReference<QNode> cleanMe =
        new PaddedAtomicReference<QNode>(null);
    
    /**
     * Puts or takes an item. Used for most queue operations (except
     * poll() and tryTransfer())
     * @param e the item or if null, signfies that this is a take
     * @param mode the wait mode: NOWAIT, TIMEOUT, WAIT
     * @param nanos timeout in nanosecs, used only if mode is TIMEOUT
     * @return an item, or null on failure
     */
    private Object xfer(Object e, int mode, long nanos) {
        boolean isData = (e != null);
        QNode s = null;
        /**
         * Q:
         * 这里和java8 的实现很大不同，
         * 为什么会产生转变，java8 使用了volatile 的Node 做头和尾巴，
         * 之前是用PaddedAtomicReference  ??
         * 
         * */
        final PaddedAtomicReference<QNode> head = this.head;
        final PaddedAtomicReference<QNode> tail = this.tail;

        for (;;) {
            QNode t = tail.get(); //tail可能多线程都会访问到
            QNode h = head.get();

            if (t != null && (t == h || t.isData == isData)) {
                if (s == null)
                    s = new QNode(e, isData);
                QNode last = t.next;
                if (last != null) {
                    if (t == tail.get())
                        tail.compareAndSet(t, last);
                }
                else if (t.casNext(null, s)) {
                    tail.compareAndSet(t, s);
                    return awaitFulfill(t, s, e, mode, nanos);
                }
            }
            
            else if (h != null) {
                QNode first = h.next;
                if (t == tail.get() && first != null && 
                    advanceHead(h, first)) {
                    Object x = first.get();
                    if (x != first && first.compareAndSet(x, e)) {
                        LockSupport.unpark(first.waiter);
                        return isData? e : x;
                    }
                }
            }
        }
    }


    /**
     * Version of xfer for poll() and tryTransfer, which
     * simpifies control paths both here and in xfer
     */
    private Object fulfill(Object e) {
        boolean isData = (e != null);
        final PaddedAtomicReference<QNode> head = this.head;
        final PaddedAtomicReference<QNode> tail = this.tail;

        for (;;) {
            QNode t = tail.get();
            QNode h = head.get();

            if (t != null && (t == h || t.isData == isData)) {
                QNode last = t.next;
                if (t == tail.get()) {
                    if (last != null)
                        tail.compareAndSet(t, last);
                    else
                        return null;
                }
            }
            else if (h != null) {
                QNode first = h.next;
                if (t == tail.get() && 
                    first != null &&
                    advanceHead(h, first)) {
                    Object x = first.get();
                    if (x != first && first.compareAndSet(x, e)) {
                        LockSupport.unpark(first.waiter);
                        return isData? e : x;
                    }
                }
            }
        }
    }
    
    
    /* 
     * 下面的方法是其他依赖代码
     * */ 
    
    /** 
     * Node class for LinkedTransferQueue. Opportunistically subclasses from
     * AtomicReference to represent item. Uses Object, not E, to allow
     * setting item to "this" after use, to avoid garbage
     * retention. Similarly, setting the next field to this is used as
     * sentinel that node is off list.
     */
    static final class QNode extends AtomicReference<Object> {
        volatile QNode next;
        volatile Thread waiter;       // to control park/unpark
        final boolean isData;
        QNode(Object item, boolean isData) {
            super(item);
            this.isData = isData;
        }

        static final AtomicReferenceFieldUpdater<QNode, QNode>
            nextUpdater = AtomicReferenceFieldUpdater.newUpdater
            (QNode.class, QNode.class, "next");

        boolean casNext(QNode cmp, QNode val) {
            return nextUpdater.compareAndSet(this, cmp, val);
        }
    }
    
    /**
     * Spins/blocks until node s is fulfilled or caller gives up,
     * depending on wait mode.
     *
     * @param pred the predecessor of waiting node
     * @param s the waiting node
     * @param e the comparison value for checking match
     * @param mode mode
     * @param nanos timeout value
     * @return matched item, or s if cancelled
     */
    private Object awaitFulfill(QNode pred, QNode s, Object e, 
                                int mode, long nanos) {
    	return null;
    }
    
    /**
     * Tries to cas nh as new head; if successful, unlink
     * old head's next node to avoid garbage retention.
     */
    private boolean advanceHead(QNode h, QNode nh) {
        if (h == head.get() && head.compareAndSet(h, nh)) {
            h.next = h; // forget old next
            return true;
        }
        return false;
    }     
}
