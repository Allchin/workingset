package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.LockSupport;

import cn.allchin.jcutest.aqs.MyQueuedSynchronizer.Node;

import java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject;
 

/**
 * <pre>
 * Q:ConditionObject的适用场景
 * A:TODO 
 * http://ifeve.com/aqs-2/
 * 
 * AQS框架提供了一个ConditionObject类，
 * 给维护独占同步的类以及实现Lock接口的类使用。
 * 一个锁对象可以关联任意数目的条件对象，
 * 可以提供典型的管程风格的await、signal和signalAll操作，
 * 包括带有超时的，以及一些检测、监控的方法。
 * 
 * 
 * @author renxing.zhang
 *
 */
public class MyConditionObject {
	public static void main(String[] args) {
		ConditionObject co=null;
	}
	
	  /**
     * Implements interruptible condition wait.
     * <ol>
     * <li> If current thread is interrupted, throw InterruptedException.
     * <li> Save lock state returned by {@link #getState}.
     * <li> Invoke {@link #release} with saved state as argument,
     *      throwing IllegalMonitorStateException if it fails.
     * <li> Block until signalled or interrupted.
     * <li> Reacquire by invoking specialized version of
     *      {@link #acquire} with saved state as argument.
     * <li> If interrupted while blocked in step 4, throw InterruptedException.
     * </ol>
     */
    public final void await() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        Node node = addConditionWaiter();
        int savedState = fullyRelease(node);
        int interruptMode = 0;
        while (!isOnSyncQueue(node)) {
            LockSupport.park(this);
            if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                break;
        }
        if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
            interruptMode = REINTERRUPT;
        if (node.nextWaiter != null) // clean up if cancelled
            unlinkCancelledWaiters();
        if (interruptMode != 0)
            reportInterruptAfterWait(interruptMode);
    }
    /**
     * 返回false ： 节点还在等待状态，或者节点的前一个节点是null ??
     * Q:前一个节点是null代表啥
     * A: TODO 
     * 返回true :节点的下个节点不是null
     * @param node
     * @return
     */
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        /*
         * node.prev can be non-null, but not yet on queue because
         * the CAS to place it on queue can fail. So we have to
         * traverse from tail to make sure it actually made it.  It
         * will always be near the tail in calls to this method, and
         * unless the CAS failed (which is unlikely), it will be
         * there, so we hardly ever traverse much.
         */
        return findNodeFromTail(node);
    }
}
