package cn.allchin.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * ͨ����д����ѧϰ�̳߳����˼��
 * 
 * </pre>
 * 
 * @author renxing.zhang
 *
 */
public class MyExecutorService extends ThreadPoolExecutor {

	/**
	 * <pre>
	 * ���� COUNT_BITS ����Ϊ 29(32-3)����ζ��ǰ��λ���ڴ���߳�״̬����29λ���ڴ���߳��� �ܶ��ѧ�ߺ�ϲ�����Լ��Ĵ�����д�ܶ�
	 * 29 �������֣�����ĳ��������ַ�����Ȼ��ֲ��ڸ����ط������Ƿǳ�����
	 */
	private static final int COUNT_BITS = Integer.SIZE - 3;
	/**
	 * 
	 * <pre>
	 * 000 11111111111111111111111111111 ����õ����� 29 �� 1��Ҳ����˵�̳߳ص�����߳�����
	 * 2^29-1=536870911 ���������ڼ������ʵ�����������������ǹ��õ�
	 */
	private static final int CAPACITY = (1 << COUNT_BITS) - 1;

	// runState is stored in the high-order bits
	/**
	 * <pre>
	 * ƫ����ƣ���Ϊ�˽�����װ��һ��int���͵ĸ�λ
	 * 
	 * ����˵�ˣ��̳߳ص�״̬����ڸ� 3 λ�� ������Ϊ 111��29��0��111 00000000000000000000000000000
	 * 
	 * ���ûʲô��˵�ģ�������������״̬�������µ����񣬴���ȴ������е�����
	 */
	private static final int RUNNING = -1 << COUNT_BITS;
	/**
	 * �������µ������ύ�����ǻ��������ȴ������е�����
	 */
	private static final int SHUTDOWN = 0 << COUNT_BITS;
	/**
	 * �������µ������ύ�����ٴ���ȴ������е������ж�����ִ��������߳�
	 */
	private static final int STOP = 1 << COUNT_BITS;
	/**
	 * ���е����������ˣ�workCount Ϊ 0���̳߳ص�״̬��ת��Ϊ TIDYING ״̬ʱ����ִ�й��ӷ��� terminated()
	 */
	private static final int TIDYING = 2 << COUNT_BITS;
	/**
	 * terminated() �����������̳߳ص�״̬�ͻ������
	 */
	private static final int TERMINATED = 3 << COUNT_BITS;

	// Packing and unpacking ctl
	private static int runStateOf(int c) {
		return c & ~CAPACITY;
	}

	/**
	 * ��ȡc�д���worker������
	 * @param c
	 * @return
	 */
	private static int workerCountOf(int c) {
		return c & CAPACITY;
	}

	private static int ctlOf(int rs, int wc) {
		return rs | wc;
	}

	/**
	 * The main pool control state, ctl, is an atomic integer packing two
	 * conceptual fields workerCount, indicating the effective number of threads
	 * runState, indicating whether running, shutting down etc
	 *
	 * In order to pack them into one int, we limit workerCount to (2^29)-1
	 * (about 500 million) threads rather than (2^31)-1 (2 billion) otherwise
	 * representable. If this is ever an issue in the future, the variable can
	 * be changed to be an AtomicLong, and the shift/mask constants below
	 * adjusted. But until the need arises, this code is a bit faster and
	 * simpler using an int.
	 *
	 * The workerCount is the number of workers that have been permitted to
	 * start and not permitted to stop. The value may be transiently different
	 * from the actual number of live threads, for example when a ThreadFactory
	 * fails to create a thread when asked, and when exiting threads are still
	 * performing bookkeeping before terminating. The user-visible pool size is
	 * reported as the current size of the workers set.
	 *
	 * The runState provides the main lifecycle control, taking on values:
	 *
	 * RUNNING: Accept new tasks and process queued tasks SHUTDOWN: Don't accept
	 * new tasks, but process queued tasks STOP: Don't accept new tasks, don't
	 * process queued tasks, and interrupt in-progress tasks TIDYING: All tasks
	 * have terminated, workerCount is zero, the thread transitioning to state
	 * TIDYING will run the terminated() hook method TERMINATED: terminated()
	 * has completed
	 *
	 * The numerical order among these values matters, to allow ordered
	 * comparisons. The runState monotonically increases over time, but need not
	 * hit each state. The transitions are:
	 *
	 * RUNNING -> SHUTDOWN On invocation of shutdown(), perhaps implicitly in
	 * finalize() (RUNNING or SHUTDOWN) -> STOP On invocation of shutdownNow()
	 * SHUTDOWN -> TIDYING When both queue and pool are empty STOP -> TIDYING
	 * When pool is empty TIDYING -> TERMINATED When the terminated() hook
	 * method has completed
	 *
	 * Threads waiting in awaitTermination() will return when the state reaches
	 * TERMINATED.
	 *
	 * Detecting the transition from SHUTDOWN to TIDYING is less straightforward
	 * than you'd like because the queue may become empty after non-empty and
	 * vice versa during SHUTDOWN state, but we can only terminate if, after
	 * seeing that it is empty, we see that workerCount is 0 (which sometimes
	 * entails a recheck -- see below).
	 * 
	 * <pre>
	 * �������͵ĳ�Ա��������һ��ԭ�������������ߵ�λ��װ���������
		    runState
		        �̳߳�����״̬��ռ��ctl�ĸ�3λ����RUNNING��SHUTDOWN��STOP��TIDYING��TERMINATED����״̬��
		    workerCount
		        �̳߳��е�ǰ����߳�������ռ��ctl�ĵ�29λ
	 * 
	 * </pre>
	 */
	private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

	/**
	 * Executes the given task sometime in the future. The task may execute in a
	 * new thread or in an existing pooled thread.
	 *
	 * If the task cannot be submitted for execution, either because this
	 * executor has been shutdown or because its capacity has been reached, the
	 * task is handled by the current {@code RejectedExecutionHandler}.
	 * 
	 * <pre>
	 * ���ڲ���Ҫ��ȡ������������ FutureTask �İ�װ��
	 * 
	 * ����������Ҫ�������submit�ύ������Ҫ�������executeִ�С�
	 * 
	 * </pre>
	 * 
	 * @param command
	 *            the task to execute
	 * @throws RejectedExecutionException
	 *             at discretion of {@code RejectedExecutionHandler}, if the
	 *             task cannot be accepted for execution
	 * @throws NullPointerException
	 *             if {@code command} is null
	 */
	public void execute(Runnable command) {
		if (command == null)
			throw new NullPointerException();
		/*
		 * Proceed in 3 steps:
		 *
		 * 1. If fewer than corePoolSize threads are running, try to start a new
		 * thread with the given command as its first task. The call to
		 * addWorker atomically checks runState and workerCount, and so prevents
		 * false alarms that would add threads when it shouldn't, by returning
		 * false.
		 *
		 * 2. If a task can be successfully queued, then we still need to
		 * double-check whether we should have added a thread (because existing
		 * ones died since last checking) or that the pool shut down since entry
		 * into this method. So we recheck state and if necessary roll back the
		 * enqueuing if stopped, or start a new thread if there are none.
		 *
		 * 3. If we cannot queue task, then we try to add a new thread. If it
		 * fails, we know we are shut down or saturated and so reject the task.
		 */
		int c = ctl.get();
		if (workerCountOf(c) < corePoolSize) {
			if (addWorker(command, true))
				return;
			c = ctl.get();
		}
		if (isRunning(c) && workQueue.offer(command)) {
			int recheck = ctl.get();
			if (!isRunning(recheck) && remove(command))
				reject(command);
			else if (workerCountOf(recheck) == 0)
				addWorker(null, false);
		} else if (!addWorker(command, false))
			reject(command);
	}

	public MyExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * �ر��̳߳أ����ύ���������ִ�У������ܼ����ύ������
	 */
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/*
	 * // �ر��̳߳أ�����ֹͣ����ִ�е��������񣬲����ܼ����ύ������ //
	 * ����ǰ��ķ�����ȣ�����һ�����ʡ�now����������������ȥֹͣ��ǰ���ڽ��е�����
	 * Q:����ôֹͣһ������ִ�е�����ģ������ܲ��������������Լ�����ȥ�ͷ���Դ A: TODO
	 */
	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	// �ύ����
	public Future<?> submit(Runnable task) {
		if (task == null)
			throw new NullPointerException();
		// 1. �������װ�� FutureTask
		RunnableFuture<Void> ftask = newTaskFor(task, null);
		// 2. ����ִ����ִ�У�execute �����ɾ����������ʵ��
		// ǰ��Ҳ˵�ˣ�FutureTask ���ʵ����Runnable �ӿڡ�
		execute(ftask);
		return ftask;
	}

	public <T> Future<T> submit(Runnable task, T result) {
		if (task == null)
			throw new NullPointerException();
		// 1. �������װ�� FutureTask
		RunnableFuture<T> ftask = newTaskFor(task, result);
		// 2. ����ִ����ִ��
		execute(ftask);
		return ftask;
	}

	/*
	 * // �ύһ�� Callable ����
	 */
	public <T> Future<T> submit(Callable<T> task) {
		if (task == null)
			throw new NullPointerException();
		RunnableFuture<T> ftask = newTaskFor(task);
		execute(ftask);
		return ftask;
	}

	// �˷���Ŀ�ģ��� tasks �����е������ύ���̳߳�ִ�У�����һ���߳�ִ�����Ϳ��Խ�����
	// �ڶ������� timed �����Ƿ����ó�ʱ���ƣ���ʱʱ��Ϊ������������
	// ��� timed Ϊ true��ͬʱ��ʱ�˻�û��һ���̷߳��ؽ������ô�׳� TimeoutException �쳣
	private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks, boolean timed, long nanos)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (tasks == null)
			throw new NullPointerException();
		// ������
		int ntasks = tasks.size();
		if (ntasks == 0)
			throw new IllegalArgumentException();
		//
		List<Future<T>> futures = new ArrayList<Future<T>>(ntasks);

		// ExecutorCompletionService ����һ��������ִ���������� this ����������ִ����
		// ����ִ���������˰�װ��ÿ����������󣬽�������浽�ڲ���һ�� completionQueue ������
		// ��Ҳ��Ϊʲô���������������и� Completion ��ԭ��ɡ�
		ExecutorCompletionService<T> ecs = new ExecutorCompletionService<T>(this);
		try {
			// ���ڱ����쳣��Ϣ���˷������û�еõ��κ���Ч�Ľ������ô���ǿ����׳����õ���һ���쳣
			ExecutionException ee = null;
			long lastTime = timed ? System.nanoTime() : 0;
			Iterator<? extends Callable<T>> it = tasks.iterator();

			// �������ύһ�����񣬺������������� for ѭ��һ�����ύ
			futures.add(ecs.submit(it.next()));
			// �ύ��һ�������������������� 1
			--ntasks;
			// ����ִ�е�������(�ύ��ʱ�� +1�����������ʱ�� -1)
			int active = 1;

			for (;;) {
				// ecs ����˵�ˣ����ڲ���һ�� completionQueue ���ڱ���ִ����ɵĽ��
				// BlockingQueue �� poll ���������������� null �������Ϊ��
				Future<T> f = ecs.poll();
				// Ϊ null��˵���ո��ύ�ĵ�һ���̻߳�û��ִ�����
				// ��ǰ�����ύһ�����񣬼���������һ�μ�飬Ҳ��Ϊ���������
				if (f == null) {
					if (ntasks > 0) {
						--ntasks;
						futures.add(ecs.submit(it.next()));
						++active;
					}
					// ������ else if������ if������˵����û�������ˣ�ͬʱ active Ϊ 0 ˵��
					// ����ִ������ˡ���ʵ��Ҳû���Ϊʲô������һ�� break��
					// ��Ϊ����Ϊ active Ϊ 0 ���������Ȼ������� f.get() ������

					// 2018-02-23 ��л���� newmicro �� comment��
					// ����� active == 0��˵�����е�����ִ��ʧ�ܣ���ô������ for ѭ������
					else if (active == 0)
						break;
					// ����Ҳ�� else if������˵���ǣ�û�������ˣ����������˳�ʱʱ�䣬�������Ƿ�ʱ
					else if (timed) {
						// ���ȴ��� poll ����
						f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
						// ����Ѿ���ʱ���׳� TimeoutException �쳣�������������ͽ�����
						if (f == null)
							throw new TimeoutException();
						long now = System.nanoTime();
						nanos -= now - lastTime;
						lastTime = now;
					}
					// ������ else��˵����û��������Ҫ�ύ�����ǳ��е�����û����ɣ���û�г�ʱ(��������˳�ʱ)
					// take() ������������ֱ����Ԫ�ط��أ�˵�������������
					else
						f = ecs.take();
				}
				/*
				 * �Ҹо�������һ�β����Ǻܺ���⣬�����˵�¡� 1. ���ȣ�����һ�� for ѭ���У���������ÿһ������û��ô�������
				 * ��ô��ÿһ�ζ��������һ����֧�������ύ����ֱ�������е������ύ�� 2. �����ύ��ɺ���������˳�ʱ����ô
				 * for ѭ����ʵ�����ˡ�һֱ����Ƿ�ʱ�� ��������� 3. ���û�����ó�ʱ���ƣ���ô����Ҫ��ⳬʱ���Ǿͻ�������
				 * ecs.take() �����ϣ� �ȴ���ȡ��һ��ִ�н�� 4. ������е�����ִ��ʧ�ܣ�Ҳ����˵ future �������ˣ�
				 * ���� f.get() �׳��쳣����ô�� active == 0 ��֧��ȥ(��л newmicro ���) //
				 * ��Ȼ�������Ҫ������� if ��֧��
				 */

				// �����������
				if (f != null) {
					--active;
					try {
						// ����ִ�н����������쳣������װ�� ExecutionException
						return f.get();
					} catch (ExecutionException eex) {
						ee = eex;
					} catch (RuntimeException rex) {
						ee = new ExecutionException(rex);
					}
				}
			} // ע�⿴ for ѭ���ķ�Χ��һֱ������

			if (ee == null)
				// ee = new ExecutionException();
				throw ee;

		} finally {
			// �����˳�֮ǰ��ȡ������������
			for (Future<T> f : futures)
				f.cancel(true);
		}
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		try {
			return doInvokeAny(tasks, false, 0);
		} catch (TimeoutException cannotHappen) {
			assert false;
			return null;
		}
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return doInvokeAny(tasks, true, unit.toNanos(timeout));
	}

	// ִ�����е����񣬷�����������
	// �Ȳ�Ҫ��������������������룬��ʵ�����Լ��ύ�����̳߳أ�Ҳ����Ҫ�̳߳�ִ�����е�����
	// ֻ������������ÿ�� submit һ������������һ��������Ϊ�����ύ
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		if (tasks == null)
			throw new NullPointerException();
		List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
		boolean done = false;
		try {
			// ����ܼ�
			for (Callable<T> t : tasks) {
				// ��װ�� FutureTask
				RunnableFuture<T> f = newTaskFor(t);
				futures.add(f);
				// �ύ����
				execute(f);
			}
			for (Future<T> f : futures) {
				if (!f.isDone()) {
					try {
						// ����һ������������ֱ����ȡ��ֵ�����׳����쳣
						// �����и�Сϸ�ڣ���ʵ get ����ǩ�����ǻ��׳� InterruptedException ��
						// ��������û�н��д��������׸����ȥ�ˡ����쳣�����ڻ�ûִ���������ȡ����
						f.get();
					} catch (CancellationException ignore) {
					} catch (ExecutionException ignore) {
					}
				}
			}
			done = true;
			// ����������أ����������ĳ��������� List<Future>����ʵִ�н����û����
			// ������������������ķ��أ����񶼽�����
			return futures;
		} finally {
			// ΪʲôҪ�������������˵�����쳣�����
			if (!done)
				for (Future<T> f : futures)
					f.cancel(true);
		}
	}

	// ����ʱ�� invokeAll�������Ҳ�ͬ��
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		if (tasks == null || unit == null)
			throw new NullPointerException();
		long nanos = unit.toNanos(timeout);
		List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
		boolean done = false;
		try {
			for (Callable<T> t : tasks)
				futures.add(newTaskFor(t));

			long lastTime = System.nanoTime();

			Iterator<Future<T>> it = futures.iterator();
			// �ύһ�����񣬼��һ���Ƿ�ʱ
			while (it.hasNext()) {
				execute((Runnable) (it.next()));
				long now = System.nanoTime();
				nanos -= now - lastTime;
				lastTime = now;
				// ��ʱ
				if (nanos <= 0)
					return futures;
			}

			for (Future<T> f : futures) {
				if (!f.isDone()) {
					if (nanos <= 0)
						return futures;
					try {
						// ���ô���ʱ�� get ����������Ĳ��� nanos ��ʣ���ʱ�䣬
						// ��Ϊ������ʵ�Ѿ��õ���һЩʱ����
						f.get(nanos, TimeUnit.NANOSECONDS);
					} catch (CancellationException ignore) {
					} catch (ExecutionException ignore) {
					} catch (TimeoutException toe) {
						return futures;
					}
					long now = System.nanoTime();
					nanos -= now - lastTime;
					lastTime = now;
				}
			}
			done = true;
			return futures;
		} finally {
			if (!done)
				for (Future<T> f : futures)
					f.cancel(true);
		}
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * The queue used for holding tasks and handing off to worker threads. We do
	 * not require that workQueue.poll() returning null necessarily means that
	 * workQueue.isEmpty(), so rely solely on isEmpty to see if the queue is
	 * empty (which we must do for example when deciding whether to transition
	 * from SHUTDOWN to TIDYING). This accommodates special-purpose queues such
	 * as DelayQueues for which poll() is allowed to return null even if it may
	 * later return non-null when delays expire.
	 */
	private final BlockingQueue<Runnable> workQueue;

	/**
	 * Lock held on access to workers set and related bookkeeping. While we
	 * could use a concurrent set of some sort, it turns out to be generally
	 * preferable to use a lock. Among the reasons is that this serializes
	 * interruptIdleWorkers, which avoids unnecessary interrupt storms,
	 * especially during shutdown. Otherwise exiting threads would concurrently
	 * interrupt those that have not yet interrupted. It also simplifies some of
	 * the associated statistics bookkeeping of largestPoolSize etc. We also
	 * hold mainLock on shutdown and shutdownNow, for the sake of ensuring
	 * workers set is stable while separately checking permission to interrupt
	 * and actually interrupting.
	 */
	private final ReentrantLock mainLock = new ReentrantLock();

	/**
	 * Set containing all worker threads in pool. Accessed only when holding
	 * mainLock.
	 */
	private final HashSet<Worker> workers = new HashSet<Worker>();

	/**
	 * Wait condition to support awaitTermination
	 */
	private final Condition termination = mainLock.newCondition();

	/**
	 * Tracks largest attained pool size. Accessed only under mainLock.
	 */
	private int largestPoolSize;

	/**
	 * Counter for completed tasks. Updated only on termination of worker
	 * threads. Accessed only under mainLock.
	 */
	private long completedTaskCount;

	/*
	 * All user control parameters are declared as volatiles so that ongoing
	 * actions are based on freshest values, but without need for locking, since
	 * no internal invariants depend on them changing synchronously with respect
	 * to other actions.
	 */

	/**
	 * Factory for new threads. All threads are created using this factory (via
	 * method addWorker). All callers must be prepared for addWorker to fail,
	 * which may reflect a system or user's policy limiting the number of
	 * threads. Even though it is not treated as an error, failure to create
	 * threads may result in new tasks being rejected or existing ones remaining
	 * stuck in the queue.
	 *
	 * We go further and preserve pool invariants even in the face of errors
	 * such as OutOfMemoryError, that might be thrown while trying to create
	 * threads. Such errors are rather common due to the need to allocate a
	 * native stack in Thread.start, and users will want to perform clean pool
	 * shutdown to clean up. There will likely be enough memory available for
	 * the cleanup code to complete without encountering yet another
	 * OutOfMemoryError.
	 * 
	 * <pre>
	 * ���������̣߳�һ�����ǿ�����Ĭ�ϵľͿ����ˡ�
	 * ͨ�������ǿ���ͨ���������ǵ��̵߳��������õñȽϿɶ�һЩ��
	 * �� Message-Thread-1�� Message-Thread-2 ����������
	 * </pre>
	 */
	private volatile ThreadFactory threadFactory;

	/**
	 * Handler called when saturated or shutdown in execute.
	 * 
	 * <pre>
	 *���̳߳��Ѿ����ˣ�
	 *���������µ������ύ��ʱ��
	 *�ò�ȡʲô�����������ָ����
	 *�м��ַ�ʽ�ɹ�ѡ�����׳��쳣��
	 *ֱ�Ӿܾ�Ȼ�󷵻صȣ�
	 *Ҳ�����Լ�ʵ����Ӧ�Ľӿ�ʵ���Լ����߼�
	 * </pre>
	 *
	 */
	private volatile RejectedExecutionHandler handler;

	/**
	 * Timeout in nanoseconds for idle threads waiting for work. Threads use
	 * this timeout when there are more than corePoolSize present or if
	 * allowCoreThreadTimeOut. Otherwise they wait forever for new work.
	 * 
	 * <pre>
	 �����̵߳ı���ʱ�䣬���ĳ�̵߳Ŀ���ʱ�䳬�����ֵ��û����������� ��
	 ��ô���Ա��ر��ˡ�ע�����ֵ������������߳������ã�
	 ����̳߳��е��߳������ڵ��ں����߳��� corePoolSize��
	 ��ô��Щ�̲߳�����Ϊ����̫��ʱ������رգ�
	 ��Ȼ��
	 Ҳ����ͨ������ allowCoreThreadTimeOut(true)ʹ�����߳����ڵ��߳�Ҳ���Ա����ա�
	 * </pre>
	 */
	private volatile long keepAliveTime;

	/**
	 * If false (default), core threads stay alive even when idle. If true, core
	 * threads use keepAliveTime to time out waiting for work.
	 */
	private volatile boolean allowCoreThreadTimeOut;

	/**
	 * Core pool size is the minimum number of workers to keep alive (and not
	 * allow to time out etc) unless allowCoreThreadTimeOut is set, in which
	 * case the minimum is zero.
	 */
	private volatile int corePoolSize;

	/**
	 * Maximum pool size. Note that the actual maximum is internally bounded by
	 * CAPACITY.
	 */
	private volatile int maximumPoolSize;

	/**
	 * The default rejected execution handler
	 */
	private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

	/**
	 * Permission required for callers of shutdown and shutdownNow. We
	 * additionally require (see checkShutdownAccess) that callers have
	 * permission to actually interrupt threads in the worker set (as governed
	 * by Thread.interrupt, which relies on ThreadGroup.checkAccess, which in
	 * turn relies on SecurityManager.checkAccess). Shutdowns are attempted only
	 * if these checks pass.
	 *
	 * All actual invocations of Thread.interrupt (see interruptIdleWorkers and
	 * interruptWorkers) ignore SecurityExceptions, meaning that the attempted
	 * interrupts silently fail. In the case of shutdown, they should not fail
	 * unless the SecurityManager has inconsistent policies, sometimes allowing
	 * access to a thread and sometimes not. In such cases, failure to actually
	 * interrupt threads may disable or delay full termination. Other uses of
	 * interruptIdleWorkers are advisory, and failure to actually interrupt will
	 * merely delay response to configuration changes so is not handled
	 * exceptionally.
	 */
	private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
}
