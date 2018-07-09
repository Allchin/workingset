package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.LockSupport;

import cn.allchin.jcutest.aqs.MyQueuedSynchronizer.Node;

import java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject;
 

/**
 * <pre>
 * Q:ConditionObject�����ó���
 * A:TODO 
 * http://ifeve.com/aqs-2/
 * 
 * AQS����ṩ��һ��ConditionObject�࣬
 * ��ά����ռͬ�������Լ�ʵ��Lock�ӿڵ���ʹ�á�
 * һ����������Թ���������Ŀ����������
 * �����ṩ���͵Ĺ̷ܳ���await��signal��signalAll������
 * �������г�ʱ�ģ��Լ�һЩ��⡢��صķ�����
 * 
 * 
 * @author renxing.zhang
 *
 */
public class MyConditionObject {/*
	public static void main(String[] args) {
		ConditionObject co=null;
	}
	
	  *//**
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
     *//*
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
    *//**
     * ����false �� �ڵ㻹�ڵȴ�״̬�����߽ڵ��ǰһ���ڵ���null ??
     * Q:ǰһ���ڵ���null����ɶ
     * A: TODO 
     * ����true :�ڵ���¸��ڵ㲻��null
     * @param node
     * @return
     *//*
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        
         * node.prev can be non-null, but not yet on queue because
         * the CAS to place it on queue can fail. So we have to
         * traverse from tail to make sure it actually made it.  It
         * will always be near the tail in calls to this method, and
         * unless the CAS failed (which is unlikely), it will be
         * there, so we hardly ever traverse much.
         
        return findNodeFromTail(node);
    }
*/}
