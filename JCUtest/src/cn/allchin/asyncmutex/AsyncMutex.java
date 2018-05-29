package cn.allchin.asyncmutex;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Consumer;

public final class AsyncMutex {

	private static final AtomicReferenceFieldUpdater<InvocationNode, InvocationNode> FORWARD_LIST_UPDATER =

	AtomicReferenceFieldUpdater.newUpdater(InvocationNode.class, InvocationNode.class, "next");

	private 	InvocationNode<?> head = new InvocationNode<>(null, null, null);

	private volatile InvocationNode<?> tail = head;

	private final AtomicInteger pendingCount = new AtomicInteger();

	public <T> void attach(Executor executor,

	Callable<T> callable,

	Consumer<?

	super T> callback)

	{

		InvocationNode<T> current =

		new

		InvocationNode<>(executor, callable, callback);

		while

		(!FORWARD_LIST_UPDATER.weakCompareAndSet(tail,

		null, current))

		{
		}

		tail = current;

		if

		(pendingCount.getAndIncrement()

		==

		0)

		{

			(head = head.next).invoke(this);

		}

	}

	private

	void release()

	{

		if

		(pendingCount.decrementAndGet()

		!=

		0)

		{

			(head = head.next).invoke(this);

		}

	}

	private

	static

	final

	class

	InvocationNode<T>

	{

		private

		Executor executor;

		private

		Callable<T> callable;

		private

		Consumer<?

		super T> callback;

		volatile

		InvocationNode<?>

		next;

		private

		InvocationNode(Executor executor,

		Callable<T> callable,

		Consumer<?

		super T> callback)

		{

			this.executor = executor;

			this.callable = callable;

			this.callback = callback;

		}

		private

		void invoke(AsyncMutex mutex)

		{

			try

			{

				executor.execute(()

				->

				{

					T result;

					try

					{

						result = callable.call();

					}

					catch

					(Throwable ignore)

					{

						return;

					}

					finally

					{

						mutex.release();

					}

					try

					{

						callback.accept(result);

					}

					catch

					(Throwable ignore)

					{

					}

					executor =

					null;

					// Help GC

					callable =

					null;

					callback =

					null;

				});

			}

			catch

			(Throwable ignore)

			{

				mutex.release();

			}

		}

	}

}