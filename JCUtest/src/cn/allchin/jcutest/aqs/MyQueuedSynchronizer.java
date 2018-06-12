package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * �Լ�ʵ�ֵ�AbstractQueuedSynchronizer
 * 
 * @author renxing.zhang
 * 
 * https://www.cnblogs.com/daydaynobug/p/6752837.html
 * 
 * ͬ���� ��Ϊʲô���ͬ����
 * http://ifeve.com/aqs-2/
 * 
 * ���ģ�http://gee.cs.oswego.edu/dl/papers/aqs.pdf
 * ���룺https://blog.csdn.net/FAw67J7/article/details/79885944
 *
 */
public class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {
	Node head;
	Node tail;
	
	/**
	 * �˷����Ƕ�ռģʽ���߳��ͷŹ�����Դ�Ķ�����ڡ������ͷ�ָ��������Դ����������ͷ��ˣ���state=0��,
	 * ���ỽ�ѵȴ�������������߳�����ȡ��Դ��
	 * ��Ҳ����unlock()�����壬��Ȼ������ֻ����unlock()��������release()��Դ�룺
	 * 
	 * 
	 * @param arg
	 * @return
	 */
	public final boolean release_(int arg) {
	    if (tryRelease(arg)) {
	        Node h = head;//�ҵ�ͷ���
	        if (h != null && h.waitStatus != 0)
	            unparkSuccessor(h);//���ѵȴ����������һ���߳�
	        return true;
	    }
	    return false;
	}
    
	 
    private void unparkSuccessor(Node h) {
		// TODO Auto-generated method stub
		
	}


	/**
     * ��ȡ��Դ
     * ΪʲôҪ��2��ʵ�֣�2���������� ?
     * 
     *  �һ�ȡ����Դ�ˣ�ΪʲôҪ������� ? 
     *  
     *  
     *  1�����Զ���ͬ������tryAcquire()����ֱ��ȥ��ȡ��Դ������ɹ���ֱ�ӷ��أ�
2 û�ɹ�����addWaiter()�����̼߳���ȴ����е�β���������Ϊ��ռģʽ��
3 acquireQueued()ʹ�߳��ڵȴ���������Ϣ���л���ʱ���ֵ��Լ����ᱻunpark()����ȥ���Ի�ȡ��Դ����ȡ����Դ��ŷ��ء�����������ȴ������б��жϹ����򷵻�true�����򷵻�false��
4 ����߳��ڵȴ������б��жϹ������ǲ���Ӧ�ġ�ֻ�ǻ�ȡ��Դ����ٽ��������ж�selfInterrupt()�����жϲ��ϡ�
���ڴ˺���������֮�أ�����������ͼ�ܽ�һ�£�

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
     * ����ǰ�̼߳��뵽�ȴ����еĶ�β�������ص�ǰ�߳����ڵĽ��
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
	 * �ڵ�ǰ�ڵ��ܻ�ȡ��Դ ǰ �ȴ�
	 * 
	 * ��������������壬�� acquire 
	 * 
	 * 1�������β�󣬼��״̬���ҵ���ȫ��Ϣ�㣻
2����park()����waiting״̬���ȴ�unpark()��interrupt()�����Լ���
3�����Ѻ󣬿��Լ��ǲ������ʸ����õ��š�����õ���headָ��ǰ��㣬�����ش���ӵ��õ��ŵ������������Ƿ��жϹ������û�õ�����������1

	 * 
	 * @param node
	 * @param arg
	 * @return
	 */
	boolean acquireQueued_(final Node node, int arg) {
		boolean failed = true;// ����Ƿ�ɹ��õ���Դ
		try {
			boolean interrupted = false;
			 // ��ǵȴ��������Ƿ��жϹ�
			for (;;) {
				final Node p = node.predecessor();
				// ���ǰ����head�����ý���ѳ��϶�����ô�����ʸ�ȥ���Ի�ȡ��Դ���������ϴ��ͷ�����Դ�����Լ��ģ���ȻҲ���ܱ�interrupt�ˣ���

				if (p == head && tryAcquire(arg)) {
					setHead(node);// �õ���Դ�󣬽�headָ��ý�㡣����head��ָ�ı�˽�㣬���ǵ�ǰ��ȡ����Դ���Ǹ�����null��
					p.next = null; // help GC //
									// setHead��node.prev����Ϊnull���˴��ٽ�head.next��Ϊnull������Ϊ�˷���GC������ǰ��head��㡣Ҳ����ζ��֮ǰ������Դ�Ľ������ˣ�
					failed = false;
					return interrupted;// ���صȴ��������Ƿ��жϹ�
				}
				// ����Լ�������Ϣ�ˣ��ͽ���waiting״̬��ֱ����unpark()
				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
					interrupted = true;// ����ȴ������б��жϹ�������ֻ����ôһ�Σ��ͽ�interrupted���Ϊtrue
			}
		} finally {
			if (failed)
				cancelAcquire(node);
		}
	}

	/**
	 * ����߳��Һð�ȫ��Ϣ����ǾͿ��԰���ȥ��Ϣ�ˡ�
	 * �˷����������߳�ȥ��Ϣ����������ȴ�״̬��
	 * @return
	 */
	private boolean parkAndCheckInterrupt() {
		
		  LockSupport.park(this);//����park()ʹ�߳̽���waiting״̬
		  return Thread.interrupted();//��������ѣ��鿴�Լ��ǲ��Ǳ��жϵġ�
		 
	}

	private void cancelAcquire(Node node) {
		// TODO Auto-generated method stub

	}

	private void setHead(Node node) {

	}

	/**
	 * ����������ڵ����� ? ������acquireQueued(Node, int)
	 * 
	 * 
	 * �˷�����Ҫ���ڼ��״̬��
	 * �����Լ��Ƿ���Ŀ���ȥ��Ϣ��
	 * ��һ����ǰ�ߵ��̶߳�������ֻ��Ϲվ�ţ���Ҳ˵�������԰ɣ�
	 * 
	 * ���������У����ǰ������״̬����SIGNAL����ô�Լ��Ͳ��ܰ���ȥ��Ϣ��
	 * ��Ҫȥ�Ҹ����ĵ���Ϣ�㣬ͬʱ�����ٳ����¿���û�л����ֵ��Լ��úš�
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
	    int ws = pred.waitStatus;//�õ�ǰ����״̬
	    if (ws == Node.SIGNAL)
	        //����Ѿ�����ǰ������ź�֪ͨ�Լ�һ�£��ǾͿ��԰�����Ϣ��
	        return true;
	    if (ws > 0) {
	        /*
	         * ���ǰ�������ˣ��Ǿ�һֱ��ǰ�ң�ֱ���ҵ����һ�������ȴ���״̬�����������ĺ�ߡ�
	         * ע�⣺��Щ�����Ľ�㣬���ڱ��Լ���������������ǰ�ߣ������൱���γ�һ�������������Ժ�ͻᱻ�������������(GC����)��
	         */
	        do {
	            node.prev = pred = pred.prev;
	        } while (pred.waitStatus > 0);
	        pred.next = node;
	    } else {
	         //���ǰ���������ǾͰ�ǰ����״̬���ó�SIGNAL������������ź�֪ͨ�Լ�һ�¡��п���ʧ�ܣ��˼�˵�����ո��ͷ����أ�
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