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
 * 通过重写，来学习线程池设计思想
 * 
 * </pre>
 * 
 * @author renxing.zhang
 *
 */
public class MyExecutorService extends ThreadPoolExecutor {

	/**
	 * <pre>
	 * 这里 COUNT_BITS 设置为 29(32-3)，意味着前三位用于存放线程状态，后29位用于存放线程数 很多初学者很喜欢在自己的代码中写很多
	 * 29 这种数字，或者某个特殊的字符串，然后分布在各个地方，这是非常糟糕的
	 */
	private static final int COUNT_BITS = Integer.SIZE - 3;
	/**
	 * 
	 * <pre>
	 * 000 11111111111111111111111111111 这里得到的是 29 个 1，也就是说线程池的最大线程数是
	 * 2^29-1=536870911 以我们现在计算机的实际情况，这个数量还是够用的
	 */
	private static final int CAPACITY = (1 << COUNT_BITS) - 1;

	// runState is stored in the high-order bits
	/**
	 * <pre>
	 * 偏移设计，是为了将来封装到一个int类型的高位
	 * 
	 * 我们说了，线程池的状态存放在高 3 位中 运算结果为 111跟29个0：111 00000000000000000000000000000
	 * 
	 * 这个没什么好说的，这是最正常的状态：接受新的任务，处理等待队列中的任务
	 */
	private static final int RUNNING = -1 << COUNT_BITS;
	/**
	 * 不接受新的任务提交，但是会继续处理等待队列中的任务
	 */
	private static final int SHUTDOWN = 0 << COUNT_BITS;
	/**
	 * 不接受新的任务提交，不再处理等待队列中的任务，中断正在执行任务的线程
	 */
	private static final int STOP = 1 << COUNT_BITS;
	/**
	 * 所有的任务都销毁了，workCount 为 0。线程池的状态在转换为 TIDYING 状态时，会执行钩子方法 terminated()
	 */
	private static final int TIDYING = 2 << COUNT_BITS;
	/**
	 * terminated() 方法结束后，线程池的状态就会变成这个
	 */
	private static final int TERMINATED = 3 << COUNT_BITS;

	// Packing and unpacking ctl
	private static int runStateOf(int c) {
		return c & ~CAPACITY;
	}

	/**
	 * 获取c中代表worker的数量
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
	 * 复合类型的成员变量，是一个原子整数，借助高低位包装了两个概念：
		    runState
		        线程池运行状态，占据ctl的高3位，有RUNNING、SHUTDOWN、STOP、TIDYING、TERMINATED五种状态。
		    workerCount
		        线程池中当前活动的线程数量，占据ctl的低29位
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
	 * 由于不需要获取结果，不会进行 FutureTask 的包装。
	 * 
	 * 看起来：需要结果的用submit提交，不需要结果的用execute执行。
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
	 * 关闭线程池，已提交的任务继续执行，不接受继续提交新任务
	 */
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/*
	 * // 关闭线程池，尝试停止正在执行的所有任务，不接受继续提交新任务 //
	 * 它和前面的方法相比，加了一个单词“now”，区别在于它会去停止当前正在进行的任务
	 * Q:它怎么停止一个正在执行的任务的？我们能不能利用起来，自己管理去释放资源 A: TODO
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

	// 提交任务
	public Future<?> submit(Runnable task) {
		if (task == null)
			throw new NullPointerException();
		// 1. 将任务包装成 FutureTask
		RunnableFuture<Void> ftask = newTaskFor(task, null);
		// 2. 交给执行器执行，execute 方法由具体的子类来实现
		// 前面也说了，FutureTask 间接实现了Runnable 接口。
		execute(ftask);
		return ftask;
	}

	public <T> Future<T> submit(Runnable task, T result) {
		if (task == null)
			throw new NullPointerException();
		// 1. 将任务包装成 FutureTask
		RunnableFuture<T> ftask = newTaskFor(task, result);
		// 2. 交给执行器执行
		execute(ftask);
		return ftask;
	}

	/*
	 * // 提交一个 Callable 任务
	 */
	public <T> Future<T> submit(Callable<T> task) {
		if (task == null)
			throw new NullPointerException();
		RunnableFuture<T> ftask = newTaskFor(task);
		execute(ftask);
		return ftask;
	}

	// 此方法目的：将 tasks 集合中的任务提交到线程池执行，任意一个线程执行完后就可以结束了
	// 第二个参数 timed 代表是否设置超时机制，超时时间为第三个参数，
	// 如果 timed 为 true，同时超时了还没有一个线程返回结果，那么抛出 TimeoutException 异常
	private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks, boolean timed, long nanos)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (tasks == null)
			throw new NullPointerException();
		// 任务数
		int ntasks = tasks.size();
		if (ntasks == 0)
			throw new IllegalArgumentException();
		//
		List<Future<T>> futures = new ArrayList<Future<T>>(ntasks);

		// ExecutorCompletionService 不是一个真正的执行器，参数 this 才是真正的执行器
		// 它对执行器进行了包装，每个任务结束后，将结果保存到内部的一个 completionQueue 队列中
		// 这也是为什么这个类的名字里面有个 Completion 的原因吧。
		ExecutorCompletionService<T> ecs = new ExecutorCompletionService<T>(this);
		try {
			// 用于保存异常信息，此方法如果没有得到任何有效的结果，那么我们可以抛出最后得到的一个异常
			ExecutionException ee = null;
			long lastTime = timed ? System.nanoTime() : 0;
			Iterator<? extends Callable<T>> it = tasks.iterator();

			// 首先先提交一个任务，后面的任务到下面的 for 循环一个个提交
			futures.add(ecs.submit(it.next()));
			// 提交了一个任务，所以任务数量减 1
			--ntasks;
			// 正在执行的任务数(提交的时候 +1，任务结束的时候 -1)
			int active = 1;

			for (;;) {
				// ecs 上面说了，其内部有一个 completionQueue 用于保存执行完成的结果
				// BlockingQueue 的 poll 方法不阻塞，返回 null 代表队列为空
				Future<T> f = ecs.poll();
				// 为 null，说明刚刚提交的第一个线程还没有执行完成
				// 在前面先提交一个任务，加上这里做一次检查，也是为了提高性能
				if (f == null) {
					if (ntasks > 0) {
						--ntasks;
						futures.add(ecs.submit(it.next()));
						++active;
					}
					// 这里是 else if，不是 if。这里说明，没有任务了，同时 active 为 0 说明
					// 任务都执行完成了。其实我也没理解为什么这里做一次 break？
					// 因为我认为 active 为 0 的情况，必然从下面的 f.get() 返回了

					// 2018-02-23 感谢读者 newmicro 的 comment，
					// 这里的 active == 0，说明所有的任务都执行失败，那么这里是 for 循环出口
					else if (active == 0)
						break;
					// 这里也是 else if。这里说的是，没有任务了，但是设置了超时时间，这里检测是否超时
					else if (timed) {
						// 带等待的 poll 方法
						f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
						// 如果已经超时，抛出 TimeoutException 异常，这整个方法就结束了
						if (f == null)
							throw new TimeoutException();
						long now = System.nanoTime();
						nanos -= now - lastTime;
						lastTime = now;
					}
					// 这里是 else。说明，没有任务需要提交，但是池中的任务没有完成，还没有超时(如果设置了超时)
					// take() 方法会阻塞，直到有元素返回，说明有任务结束了
					else
						f = ecs.take();
				}
				/*
				 * 我感觉上面这一段并不是很好理解，这里简单说下。 1. 首先，这在一个 for 循环中，我们设想每一个任务都没那么快结束，
				 * 那么，每一次都会进到第一个分支，进行提交任务，直到将所有的任务都提交了 2. 任务都提交完成后，如果设置了超时，那么
				 * for 循环其实进入了“一直检测是否超时” 这件事情上 3. 如果没有设置超时机制，那么不必要检测超时，那就会阻塞在
				 * ecs.take() 方法上， 等待获取第一个执行结果 4. 如果所有的任务都执行失败，也就是说 future 都返回了，
				 * 但是 f.get() 抛出异常，那么从 active == 0 分支出去(感谢 newmicro 提出) //
				 * 当然，这个需要看下面的 if 分支。
				 */

				// 有任务结束了
				if (f != null) {
					--active;
					try {
						// 返回执行结果，如果有异常，都包装成 ExecutionException
						return f.get();
					} catch (ExecutionException eex) {
						ee = eex;
					} catch (RuntimeException rex) {
						ee = new ExecutionException(rex);
					}
				}
			} // 注意看 for 循环的范围，一直到这里

			if (ee == null)
				// ee = new ExecutionException();
				throw ee;

		} finally {
			// 方法退出之前，取消其他的任务
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

	// 执行所有的任务，返回任务结果。
	// 先不要看这个方法，我们先想想，其实我们自己提交任务到线程池，也是想要线程池执行所有的任务
	// 只不过，我们是每次 submit 一个任务，这里以一个集合作为参数提交
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		if (tasks == null)
			throw new NullPointerException();
		List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
		boolean done = false;
		try {
			// 这个很简单
			for (Callable<T> t : tasks) {
				// 包装成 FutureTask
				RunnableFuture<T> f = newTaskFor(t);
				futures.add(f);
				// 提交任务
				execute(f);
			}
			for (Future<T> f : futures) {
				if (!f.isDone()) {
					try {
						// 这是一个阻塞方法，直到获取到值，或抛出了异常
						// 这里有个小细节，其实 get 方法签名上是会抛出 InterruptedException 的
						// 可是这里没有进行处理，而是抛给外层去了。此异常发生于还没执行完的任务被取消了
						f.get();
					} catch (CancellationException ignore) {
					} catch (ExecutionException ignore) {
					}
				}
			}
			done = true;
			// 这个方法返回，不像其他的场景，返回 List<Future>，其实执行结果还没出来
			// 这个方法返回是真正的返回，任务都结束了
			return futures;
		} finally {
			// 为什么要这个？就是上面说的有异常的情况
			if (!done)
				for (Future<T> f : futures)
					f.cancel(true);
		}
	}

	// 带超时的 invokeAll，我们找不同吧
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
			// 提交一个任务，检测一次是否超时
			while (it.hasNext()) {
				execute((Runnable) (it.next()));
				long now = System.nanoTime();
				nanos -= now - lastTime;
				lastTime = now;
				// 超时
				if (nanos <= 0)
					return futures;
			}

			for (Future<T> f : futures) {
				if (!f.isDone()) {
					if (nanos <= 0)
						return futures;
					try {
						// 调用带超时的 get 方法，这里的参数 nanos 是剩余的时间，
						// 因为上面其实已经用掉了一些时间了
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
	 * 用于生成线程，一般我们可以用默认的就可以了。
	 * 通常，我们可以通过它将我们的线程的名字设置得比较可读一些，
	 * 如 Message-Thread-1， Message-Thread-2 类似这样。
	 * </pre>
	 */
	private volatile ThreadFactory threadFactory;

	/**
	 * Handler called when saturated or shutdown in execute.
	 * 
	 * <pre>
	 *当线程池已经满了，
	 *但是又有新的任务提交的时候，
	 *该采取什么策略由这个来指定。
	 *有几种方式可供选择，像抛出异常、
	 *直接拒绝然后返回等，
	 *也可以自己实现相应的接口实现自己的逻辑
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
	 空闲线程的保活时间，如果某线程的空闲时间超过这个值都没有任务给它做 ，
	 那么可以被关闭了。注意这个值并不会对所有线程起作用，
	 如果线程池中的线程数少于等于核心线程数 corePoolSize，
	 那么这些线程不会因为空闲太长时间而被关闭，
	 当然，
	 也可以通过调用 allowCoreThreadTimeOut(true)使核心线程数内的线程也可以被回收。
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
