package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FixThreadPoolExecutor extends AbstractExecutorService {
	private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
	volatile int runState;
	static final int RUNNING = 0;
	static final int SHUTDOWN = 1;
	static final int STOP = 2;
	static final int TERMINATED = 3;
	private final BlockingQueue<Runnable> workQueue;
	private final ReentrantLock mainLock;
	private final Condition termination;
	private final HashSet<Worker> workers;
	private volatile long keepAliveTime;
	private volatile boolean allowCoreThreadTimeOut;
	private volatile int corePoolSize;
	private volatile int maximumPoolSize;
	private volatile int poolSize;
	private volatile RejectedExecutionHandler handler;
	private volatile ThreadFactory threadFactory;
	private int largestPoolSize;
	private long completedTaskCount;
	private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

	public FixThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(),
				defaultHandler);
	}

	public FixThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, defaultHandler);
	}

	public FixThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
	}

	public FixThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		this.mainLock = new ReentrantLock();
		this.termination = this.mainLock.newCondition();
		this.workers = new HashSet();
		if (corePoolSize >= 0 && maximumPoolSize > 0 && maximumPoolSize >= corePoolSize && keepAliveTime >= 0L) {
			if (workQueue != null && threadFactory != null && handler != null) {
				this.corePoolSize = corePoolSize;
				this.maximumPoolSize = maximumPoolSize;
				this.workQueue = workQueue;
				this.keepAliveTime = unit.toNanos(keepAliveTime);
				this.threadFactory = threadFactory;
				this.handler = handler;
			} else {
				throw new NullPointerException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void execute(Runnable command) {
		if (command == null) {
			throw new NullPointerException();
		} else {
			if (this.poolSize >= this.corePoolSize || !this.addIfUnderCorePoolSize(command)) {
				if (this.runState == 0 && this.workQueue.offer(command)) {
					if (this.runState != 0 || this.poolSize == 0) {
						this.ensureQueuedTaskHandled(command);
					}
				} else if (!this.addIfUnderMaximumPoolSize(command)) {
					this.reject(command);
				}
			}

		}
	}

	private Thread addThread(Runnable firstTask) {
		Worker w = new Worker(firstTask);
		Thread t = this.threadFactory.newThread(w);
		if (t != null) {
			w.thread = t;
			this.workers.add(w);
			int nt = ++this.poolSize;
			if (nt > this.largestPoolSize) {
				this.largestPoolSize = nt;
			}
		}
		return t;
	}

	private boolean addIfUnderCorePoolSize(Runnable firstTask) {
		Thread t = null;
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			if (this.poolSize < this.corePoolSize && this.runState == 0) {
				t = this.addThread(firstTask);
			}
		} finally {
			mainLock.unlock();
		}

		if (t == null) {
			return false;
		} else {
			t.start();
			return true;
		}
	}

	private boolean addIfUnderMaximumPoolSize(Runnable firstTask) {
		Thread t = null;
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			if (this.poolSize < this.maximumPoolSize && this.runState == 0) {
				t = this.addThread(firstTask);
			}
		} finally {
			mainLock.unlock();
		}

		if (t == null) {
			return false;
		} else {
			t.start();
			return true;
		}
	}

	private void ensureQueuedTaskHandled(Runnable command) {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		boolean reject = false;
		Thread t = null;

		try {
			int state = this.runState;
			if (state != 0 && this.workQueue.remove(command)) {
				reject = true;
			} else if (state < 2 && this.poolSize < Math.max(this.corePoolSize, 1) && !this.workQueue.isEmpty()) {
				t = this.addThread((Runnable) null);
			}
		} finally {
			mainLock.unlock();
		}

		if (reject) {
			this.reject(command);
		} else if (t != null) {
			t.start();
		}

	}

	void reject(Runnable command) {
		this.handler.rejectedExecution(command, this);
	}

	Runnable getTask() {
		while (true) {
			try {
				do {
					int state = this.runState;
					if (state > 1) {
						return null;
					}

					Runnable r;
					if (state == 1) {
						r = (Runnable) this.workQueue.poll();
					} else if (this.poolSize <= this.corePoolSize && !this.allowCoreThreadTimeOut) {
						r = (Runnable) this.workQueue.take();
					} else {
						r = (Runnable) this.workQueue.poll(this.keepAliveTime, TimeUnit.NANOSECONDS);
					}

					if (r != null) {
						return r;
					}
				} while (!this.workerCanExit());

				if (this.runState >= 1) {
					this.interruptIdleWorkers();
				}

				return null;
			} catch (InterruptedException arg2) {
				;
			}
		}
	}

	private boolean workerCanExit() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		boolean canExit;
		try {
			canExit = this.runState >= 2 || this.workQueue.isEmpty()
					|| this.allowCoreThreadTimeOut && this.poolSize > Math.max(1, this.corePoolSize);
		} finally {
			mainLock.unlock();
		}

		return canExit;
	}

	void interruptIdleWorkers() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			Iterator arg2 = this.workers.iterator();

			while (arg2.hasNext()) {
				Worker w = (Worker) arg2.next();
				w.interruptIfIdle();
			}
		} finally {
			mainLock.unlock();
		}

	}

	void workerDone(Worker w) {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			this.completedTaskCount += w.completedTasks;
			this.workers.remove(w);
			if (--this.poolSize == 0) {
				this.tryTerminate();
			}
		} finally {
			mainLock.unlock();
		}

	}

	private void tryTerminate() {
		if (this.poolSize == 0) {
			int state = this.runState;
			if (state < 2 && !this.workQueue.isEmpty()) {
				state = 0;
				Thread t = this.addThread((Runnable) null);
				if (t != null) {
					t.start();
				}
			}

			if (state == 2 || state == 1) {
				this.runState = 3;
				this.termination.signalAll();
				this.terminated();
			}
		}

	}

	public void shutdown() {
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkPermission(shutdownPerm);
		}

		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			if (security != null) {
				Iterator se = this.workers.iterator();

				while (se.hasNext()) {
					Worker state = (Worker) se.next();
					security.checkAccess(state.thread);
				}
			}

			int state1 = this.runState;
			if (state1 < 1) {
				this.runState = 1;
			}

			try {
				Iterator arg4 = this.workers.iterator();

				while (arg4.hasNext()) {
					Worker se1 = (Worker) arg4.next();
					se1.interruptIfIdle();
				}
			} catch (SecurityException arg8) {
				this.runState = state1;
				throw arg8;
			}

			this.tryTerminate();
		} finally {
			mainLock.unlock();
		}

	}

	public List<Runnable> shutdownNow() {
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkPermission(shutdownPerm);
		}

		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		List arg6;
		try {
			if (security != null) {
				Iterator tasks = this.workers.iterator();

				while (tasks.hasNext()) {
					Worker state = (Worker) tasks.next();
					security.checkAccess(state.thread);
				}
			}

			int state1 = this.runState;
			if (state1 < 2) {
				this.runState = 2;
			}

			try {
				Iterator arg4 = this.workers.iterator();

				while (arg4.hasNext()) {
					Worker tasks1 = (Worker) arg4.next();
					tasks1.interruptNow();
				}
			} catch (SecurityException arg9) {
				this.runState = state1;
				throw arg9;
			}

			List tasks2 = this.drainQueue();
			this.tryTerminate();
			arg6 = tasks2;
		} finally {
			mainLock.unlock();
		}

		return arg6;
	}

	private List<Runnable> drainQueue() {
		ArrayList taskList = new ArrayList();
		this.workQueue.drainTo(taskList);

		while (!this.workQueue.isEmpty()) {
			Iterator it = this.workQueue.iterator();

			try {
				if (it.hasNext()) {
					Runnable r = (Runnable) it.next();
					if (this.workQueue.remove(r)) {
						taskList.add(r);
					}
				}
			} catch (ConcurrentModificationException arg3) {
				;
			}
		}

		return taskList;
	}

	public boolean isShutdown() {
		return this.runState != 0;
	}

	boolean isStopped() {
		return this.runState == 2;
	}

	public boolean isTerminating() {
		int state = this.runState;
		return state == 1 || state == 2;
	}

	public boolean isTerminated() {
		return this.runState == 3;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		while (true) {
			try {
				if (this.runState == 3) {
					return true;
				}

				if (nanos > 0L) {
					nanos = this.termination.awaitNanos(nanos);
					continue;
				}
			} finally {
				mainLock.unlock();
			}

			return false;
		}
	}

	protected void finalize() {
		this.shutdown();
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
		if (threadFactory == null) {
			throw new NullPointerException();
		} else {
			this.threadFactory = threadFactory;
		}
	}

	public ThreadFactory getThreadFactory() {
		return this.threadFactory;
	}

	public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
		if (handler == null) {
			throw new NullPointerException();
		} else {
			this.handler = handler;
		}
	}

	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return this.handler;
	}

	public void setCorePoolSize(int corePoolSize) {
		if (corePoolSize < 0) {
			throw new IllegalArgumentException();
		} else {
			ReentrantLock mainLock = this.mainLock;
			mainLock.lock();

			try {
				int extra = this.corePoolSize - corePoolSize;
				this.corePoolSize = corePoolSize;
				if (extra < 0) {
					int it = this.workQueue.size();

					while (extra++ < 0 && it-- > 0 && this.poolSize < corePoolSize) {
						Thread t = this.addThread((Runnable) null);
						if (t == null) {
							break;
						}

						t.start();
					}
				} else if (extra > 0 && this.poolSize > corePoolSize) {
					try {
						Iterator arg10 = this.workers.iterator();

						while (arg10.hasNext() && extra-- > 0 && this.poolSize > corePoolSize
								&& this.workQueue.remainingCapacity() == 0) {
							((Worker) arg10.next()).interruptIfIdle();
						}
					} catch (SecurityException arg8) {
						;
					}
				}
			} finally {
				mainLock.unlock();
			}

		}
	}

	public int getCorePoolSize() {
		return this.corePoolSize;
	}

	public boolean prestartCoreThread() {
		return this.addIfUnderCorePoolSize((Runnable) null);
	}

	public int prestartAllCoreThreads() {
		int n;
		for (n = 0; this.addIfUnderCorePoolSize((Runnable) null); ++n) {
			;
		}

		return n;
	}

	public boolean allowsCoreThreadTimeOut() {
		return this.allowCoreThreadTimeOut;
	}

	public void allowCoreThreadTimeOut(boolean value) {
		if (value && this.keepAliveTime <= 0L) {
			throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
		} else {
			this.allowCoreThreadTimeOut = value;
		}
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		if (maximumPoolSize > 0 && maximumPoolSize >= this.corePoolSize) {
			ReentrantLock mainLock = this.mainLock;
			mainLock.lock();

			try {
				int extra = this.maximumPoolSize - maximumPoolSize;
				this.maximumPoolSize = maximumPoolSize;
				if (extra > 0 && this.poolSize > maximumPoolSize) {
					try {
						for (Iterator it = this.workers.iterator(); it.hasNext() && extra > 0
								&& this.poolSize > maximumPoolSize; --extra) {
							((Worker) it.next()).interruptIfIdle();
						}
					} catch (SecurityException arg7) {
						;
					}
				}
			} finally {
				mainLock.unlock();
			}

		} else {
			throw new IllegalArgumentException();
		}
	}

	public int getMaximumPoolSize() {
		return this.maximumPoolSize;
	}

	public void setKeepAliveTime(long time, TimeUnit unit) {
		if (time < 0L) {
			throw new IllegalArgumentException();
		} else if (time == 0L && this.allowsCoreThreadTimeOut()) {
			throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
		} else {
			this.keepAliveTime = unit.toNanos(time);
		}
	}

	public long getKeepAliveTime(TimeUnit unit) {
		return unit.convert(this.keepAliveTime, TimeUnit.NANOSECONDS);
	}

	public BlockingQueue<Runnable> getQueue() {
		return this.workQueue;
	}

	public boolean remove(Runnable task) {
		return this.getQueue().remove(task);
	}

	public void purge() {
		try {
			Iterator ex = this.getQueue().iterator();

			while (ex.hasNext()) {
				Runnable r = (Runnable) ex.next();
				if (r instanceof Future) {
					Future c = (Future) r;
					if (c.isCancelled()) {
						ex.remove();
					}
				}
			}

		} catch (ConcurrentModificationException arg3) {
			;
		}
	}

	public int getPoolSize() {
		return this.poolSize;
	}

	public int getActiveCount() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			int n = 0;
			Iterator arg3 = this.workers.iterator();

			while (arg3.hasNext()) {
				Worker w = (Worker) arg3.next();
				if (w.isActive()) {
					++n;
				}
			}

			int arg5 = n;
			return arg5;
		} finally {
			mainLock.unlock();
		}
	}

	public int getLargestPoolSize() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		int arg2;
		try {
			arg2 = this.largestPoolSize;
		} finally {
			mainLock.unlock();
		}

		return arg2;
	}

	public long getTaskCount() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			long n = this.completedTaskCount;
			Iterator arg4 = this.workers.iterator();

			while (arg4.hasNext()) {
				Worker w = (Worker) arg4.next();
				n += w.completedTasks;
				if (w.isActive()) {
					++n;
				}
			}

			long arg6 = n + (long) this.workQueue.size();
			return arg6;
		} finally {
			mainLock.unlock();
		}
	}

	public long getCompletedTaskCount() {
		ReentrantLock mainLock = this.mainLock;
		mainLock.lock();

		try {
			long n = this.completedTaskCount;

			Worker w;
			for (Iterator arg4 = this.workers.iterator(); arg4.hasNext(); n += w.completedTasks) {
				w = (Worker) arg4.next();
			}

			long arg6 = n;
			return arg6;
		} finally {
			mainLock.unlock();
		}
	}

	protected void beforeExecute(Thread t, Runnable r) {
	}

	protected void afterExecute(Runnable r, Throwable t) {
	}

	protected void terminated() {
	}
	private final class Worker implements Runnable
    {
        private final ReentrantLock runLock;
        private Runnable firstTask;
        volatile long completedTasks;
        Thread thread;
        
        Worker(final Runnable firstTask) {
            this.runLock = new ReentrantLock();
            this.firstTask = firstTask;
        }
        
        boolean isActive() {
            return this.runLock.isLocked();
        }
        
        void interruptIfIdle() {
            final ReentrantLock runLock = this.runLock;
            if (runLock.tryLock()) {
                try {
                    if (this.thread != Thread.currentThread()) {
                        this.thread.interrupt();
                    }
                }
                finally {
                    runLock.unlock();
                }
                runLock.unlock();
            }
        }
        
        void interruptNow() {
            this.thread.interrupt();
        }
        
        private void runTask(final Runnable task) {
            final ReentrantLock runLock = this.runLock;
            runLock.lock();
            try {
                if (FixThreadPoolExecutor.this.runState < 2 && Thread.interrupted() && FixThreadPoolExecutor.this.runState >= 2) {
                    this.thread.interrupt();
                }
                boolean ran = false;
                FixThreadPoolExecutor.this.beforeExecute(this.thread, task);
                try {
                    task.run();
                    ran = true;
                    FixThreadPoolExecutor.this.afterExecute(task, null);
                    ++this.completedTasks;
                }
                catch (RuntimeException ex) {
                    if (!ran) {
                        FixThreadPoolExecutor.this.afterExecute(task, ex);
                    }
                    throw ex;
                }
            }
            finally {
                runLock.unlock();
            }
        }
        
        @Override
        public void run() {
            try {
                Runnable task = this.firstTask;
                this.firstTask = null;
                while (task != null || (task = FixThreadPoolExecutor.this.getTask()) != null) {
                    this.runTask(task);
                    task = null;
                }
            }
            finally {
                FixThreadPoolExecutor.this.workerDone(this);
            }
            FixThreadPoolExecutor.this.workerDone(this);
        }
    }
    
    public static class CallerRunsPolicy implements RejectedExecutionHandler
    {
        @Override
        public void rejectedExecution(final Runnable r, final FixThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }
    
    public static class AbortPolicy implements RejectedExecutionHandler
    {
        @Override
        public void rejectedExecution(final Runnable r, final FixThreadPoolExecutor e) {
            throw new RejectedExecutionException();
        }
    }
    
    public static class DiscardPolicy implements RejectedExecutionHandler
    {
        @Override
        public void rejectedExecution(final Runnable r, final FixThreadPoolExecutor e) {
        }
    }
    
    public static class DiscardOldestPolicy implements RejectedExecutionHandler
    {
        @Override
        public void rejectedExecution(final Runnable r, final FixThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        }
    }
}