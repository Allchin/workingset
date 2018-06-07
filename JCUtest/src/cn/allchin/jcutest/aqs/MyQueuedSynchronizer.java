package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * �Լ�ʵ�ֵ�AbstractQueuedSynchronizer
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
     * ��ȡ��Դ
     * ΪʲôҪ��2��ʵ�֣�2���������� ? 
     * @param arg
     */
    public   void acquire_(int arg) {
        if (!tryAcquire(arg) &&
            this.acquireQueued_(addWaiter_(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
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

	private boolean parkAndCheckInterrupt() {
		return false;
	}

	private void cancelAcquire(Node node) {
		// TODO Auto-generated method stub

	}

	private void setHead(Node node) {

	}

	/**
	 * ����������ڵ����� ? ������acquireQueued(Node, int)
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