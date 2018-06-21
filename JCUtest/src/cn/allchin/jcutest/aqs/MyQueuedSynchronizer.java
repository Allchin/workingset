package cn.allchin.jcutest.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

/**
 * �Լ�ʵ�ֵ�AbstractQueuedSynchronizer
 * 
 * @author renxing.zhang
 * 
 *         https://www.cnblogs.com/daydaynobug/p/6752837.html
 * 
 *         ͬ���� ��Ϊʲô���ͬ���� http://ifeve.com/aqs-2/
 * 
 *         ���ģ�http://gee.cs.oswego.edu/dl/papers/aqs.pdf
 *         ���룺https://blog.csdn.net/FAw67J7/article/details/79885944
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
	 * �˷����Ƕ�ռģʽ���߳��ͷŹ�����Դ�Ķ�����ڡ������ͷ�ָ��������Դ����������ͷ��ˣ���state=0��, ���ỽ�ѵȴ�������������߳�����ȡ��Դ��
	 * ��Ҳ����unlock()�����壬��Ȼ������ֻ����unlock()��������release()��Դ�룺
	 * 
	 * 
	 * @param arg
	 * @return
	 */
	public final boolean release_(int arg) {
		if (tryRelease(arg)) {
			Node h = head;// �ҵ�ͷ���
			if (h != null && h.waitStatus != 0)
				unparkSuccessor(h);// ���ѵȴ����������һ���߳�
			return true;
		}
		return false;
	}

	private void unparkSuccessor(Node h) {
		// TODO Auto-generated method stub

	}

	/**
	 * ��ȡ��Դ ΪʲôҪ��2��ʵ�֣�2���������� ?
	 * 
	 * �һ�ȡ����Դ�ˣ�ΪʲôҪ������� ?
	 * 
	 * 
	 * 1�����Զ���ͬ������tryAcquire()����ֱ��ȥ��ȡ��Դ������ɹ���ֱ�ӷ��أ� 2
	 * û�ɹ�����addWaiter()�����̼߳���ȴ����е�β���������Ϊ��ռģʽ�� 3
	 * acquireQueued()ʹ�߳��ڵȴ���������Ϣ���л���ʱ���ֵ��Լ����ᱻunpark()����ȥ���Ի�ȡ��Դ����ȡ����Դ��ŷ��ء�����������ȴ������б��жϹ����򷵻�true�����򷵻�false��
	 * 4 ����߳��ڵȴ������б��жϹ������ǲ���Ӧ�ġ�ֻ�ǻ�ȡ��Դ����ٽ��������ж�selfInterrupt()�����жϲ��ϡ�
	 * ���ڴ˺���������֮�أ�����������ͼ�ܽ�һ�£�
	 * 
	 * 
	 * @param arg
	 */
	public void acquire_(int arg) {
		if (!tryAcquire(arg) && this.acquireQueued_(addWaiter_(Node.EXCLUSIVE), arg))
			selfInterrupt();
	}

	/**
	 * ��acquire_������ͬ
	 * 
	 * @param arg
	 */
	public void acquire2(int arg) {
		if (!tryAcquire(arg)) {
			// ����������� �½ڵ�
			Node node = addWaiter_(Node.EXCLUSIVE);

			Node pred = node.predecessor();
			// head ʱ��������tryAcquire �ɹ�ʱ����
			while (pred != head || !tryAcquire(arg)) {
				if (pred.waitStatus == Node.SIGNAL) {
					// Q: ��park�ˣ�ʲôʱ��unpark ��???
					LockSupport.park(this);// ����park()ʹ�߳̽���waiting״̬
				} else {
					// Q:���ҵ�ǰһ������ΪSIGNAL
					// A:��ǰһ���ڵ���Ա�unpark
					compareAndSetWaitStatus(pred, pred.waitStatus, Node.SIGNAL);

					// Q:ΪɶҪ���»�ȡpred,�ǲ����ٽ�����ʱ���һ��park��ô
					// A:���»�ȡpred���л����ȡ��head
					pred = node.predecessor();
				}
			}
		}
	}

	/**
	 * 
	 * ����ǰ�̼߳��뵽�ȴ����еĶ�β�������ص�ǰ�߳����ڵĽ��
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
			 * ��cas���ٵĳ�����ӣ�ֻ����һ��,���ʧ���ˣ�����ȥ����
			 */
			if (compareAndSetTail(pred, node)) {
				pred.next = node;
				return node;
			}
		}
		/**
		 * ���������cas���еĻ�����enq��������
		 */
		enq(node);
		return node;
	}

	/**
	 * ���
	 * 
	 * ����cas����
	 * 
	 * @param node
	 */
	private Node enq(Node node) {
		for (;;) { // ��������

			Node t = tail;
			if (t == null) { // Must initialize
				/**
				 * ���β���ǿգ�˵����û��ʼ���أ��ͳ�ʼ��һ�� ��ʼ����β�ͺ�ͷ��ͬ��ͬһ������
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
	 * //ԭ�ӵ�ִ�� if(tail == pred ) tail =node;
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	private boolean compareAndSetTail(Node pred, Node node) {
		return unsafe.compareAndSwapObject(this, tailOffset, pred, node);

	}

	public void selfInterrupt() {
	};

	/**
	 * �ڵ�ǰ�ڵ��ܻ�ȡ��Դ ǰ �ȴ�
	 * 
	 * ��������������壬�� acquire
	 * 
	 * 1�������β�󣬼��״̬���ҵ���ȫ��Ϣ�㣻 2����park()����waiting״̬���ȴ�unpark()��interrupt()�����Լ���
	 * 3�����Ѻ󣬿��Լ��ǲ������ʸ����õ��š�����õ���headָ��ǰ��㣬�����ش���ӵ��õ��ŵ������������Ƿ��жϹ������û�õ�����������1
	 * 
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
					/**
					 * ��ǰ�ڵ���Ϊ�ڶ��ڵ㣬���ǻ�ȡ������Դ
					 * 
					 * ��ȡ����Դ�Լ��ͳ���ͷ
					 */
					setHead(node);// �õ���Դ�󣬽�headָ��ý�㡣����head��ָ�ı�˽�㣬���ǵ�ǰ��ȡ����Դ���Ǹ�����null��
					/**
					 * ͷ�ڵ���к����ĳ�Ա��������null // help GC // //
					 * setHead��node.prev����Ϊnull���˴��ٽ�head.next��Ϊnull������Ϊ�˷���GC������ǰ��head��㡣Ҳ����ζ��֮ǰ������Դ�Ľ������ˣ�
					 * 
					 */
					p.next = null;

					failed = false;
					return interrupted;// ���صȴ��������Ƿ��жϹ�
				}
				/**
				 * ��ȡ������park
				 * �ȿ����ǲ��������Ҫpark������Լ�Ҳ���ж��ˣ��Ͳ���park��
				 * */
				// ����Լ�������Ϣ�ˣ��ͽ���waiting״̬��ֱ����unpark()
				/**
				 * shouldParkAfterFailedAcquire����true ��ʾ��Ҫ����park
				 * parkAndCheckInterrupt ����park, ����true��ʾ���жϹ�
				 * */
				if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
					interrupted = true;// ����ȴ������б��жϹ�������ֻ����ôһ�Σ��ͽ�interrupted���Ϊtrue
			}
		} finally {
			if (failed)
				cancelAcquire(node);
		}
	}

	/**
	 * ����߳��Һð�ȫ��Ϣ����ǾͿ��԰���ȥ��Ϣ�ˡ� �˷����������߳�ȥ��Ϣ����������ȴ�״̬��
	 * 
	 * @return
	 */
	private boolean parkAndCheckInterrupt() {

		LockSupport.park(this);// ����park()ʹ�߳̽���waiting״̬
		return Thread.interrupted();// ��������ѣ��鿴�Լ��ǲ��Ǳ��жϵġ�

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
	 * Q:����������ڵ����� ? ������acquireQueued(Node, int)
	 * A:�ȿ����ǲ��������Ҫpark������Լ�Ҳ���ж��ˣ��Ͳ���park��
	 * 
	 * 
	 * 
	 * �˷�����Ҫ���ڼ��״̬�� �����Լ��Ƿ���Ŀ���ȥ��Ϣ�� ��һ����ǰ�ߵ��̶߳�������ֻ��Ϲվ�ţ���Ҳ˵�������԰ɣ�
	 * 
	 * ���������У����ǰ������״̬����SIGNAL����ô�Լ��Ͳ��ܰ���ȥ��Ϣ�� ��Ҫȥ�Ҹ����ĵ���Ϣ�㣬ͬʱ�����ٳ����¿���û�л����ֵ��Լ��úš�
	 * 
	 *
	 * @param pred
	 * @param node
	 * @return
	 * </pre>
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		int ws = pred.waitStatus;// �õ�ǰ����״̬
		if (ws == Node.SIGNAL)
			/**
			 * �����֮ǰ�Ľڵ������׼���ű�unparking,�����Լ��϶�����Ҫ��park��
			 * */
			// ����Ѿ�����ǰ������ź�֪ͨ�Լ�һ�£��ǾͿ��԰�����Ϣ��
			return true;
		if (ws > 0) {
			/*
			 * ����0��ֵ��1�������Ǳ�ȡ����
			 * ���ǰ�������ˣ��Ǿ�һֱ��ǰ�ң�ֱ���ҵ����һ�������ȴ���״̬�����������ĺ�ߡ�
			 * ע�⣺��Щ�����Ľ�㣬���ڱ��Լ���������������ǰ�ߣ������൱���γ�һ�������������Ժ�ͻᱻ�������������(GC����)��
			 */
			do {
				node.prev = pred = pred.prev;
			} while (pred.waitStatus > 0);
			pred.next = node;
		} else {
			// ���ǰ���������ǾͰ�ǰ����״̬���ó�SIGNAL������������ź�֪ͨ�Լ�һ�¡��п���ʧ�ܣ��˼�˵�����ո��ͷ����أ�
			/**
			 * ǰ���ڵ�û>0 ,�Ǿ����ڵȴ�����������Ҫ��ֱ�ӻ��ѣ�
			 * �Ǿ���������״̬��Ϊ��Ҫunparking
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
        /** �߳��ڵȴ����� */
        static final int CONDITION = -2;
        /**
         * �߳����������£����¸�acquireShared��ʱ��Ӧ��������������ִ�С�
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;

		/**
		 * ��waitStatus��signalʱ��ʾ ׼�����˱�unparking waitStatus value to indicate successor's
		 * thread needs unparking
		 */
		static final int SIGNAL = -1;
        /**  ����߳��Ѿ���ȡ�� */
        static final int CANCELLED =  1;
        
		public int waitStatus;
		public Node prev;
		public static final Node EXCLUSIVE = null;
		public Object next;

		public Node(Thread currentThread, Node mode) {

			// TODO Auto-generated constructor stub
		}

		public Node() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * ����ǰһ�����,�����null�ܳ�npe ��ǰһ���ڵ㲻������nullʱ��ȥʹ�á� null check ���Ա�ʡ�ԣ����� ���԰���vm ?
		 * 
		 * Ϊʲô��ֱ�ӷ���prev ?
		 * 
		 * @return
		 */
		public Node predecessor() {
			Node p = prev;
			if (p == null) {
				// ΪʲôҪ�п� ?
				throw new NullPointerException();
			}

			return p;
		}
	}
}