package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.watermelon.manager.common.manager.scheduler.ScheduledTask;
import com.watermelon.manager.common.manager.scheduler.Trigger;


public class SchedulingRunner implements ScheduledTask, ScheduledFuture<Object> {
	private static final Logger log = LoggerFactory.getLogger(SchedulingRunner.class);
	private final ScheduledTask delegate;
	private final Trigger trigger;
	private final SimpleTaskContext taskContext = new SimpleTaskContext();
	private final FixScheduledThreadPoolExecutor executor;
	private volatile ScheduledFuture currentFuture;
	private volatile Date scheduledTime;
	private final Object mutex = new Object();

	public SchedulingRunner(ScheduledTask delegate, Trigger trigger, FixScheduledThreadPoolExecutor executor) {
		this.delegate = delegate;
		this.trigger = trigger;
		this.executor = executor;
	}

	public ScheduledFuture schedule() {
		Object arg0 = this.mutex;
		synchronized (this.mutex) {
			this.scheduledTime = this.trigger.nextTime(this.taskContext);
			if (this.scheduledTime == null) {
				return null;
			} else {
				long initialDelay = this.scheduledTime.getTime() - System.currentTimeMillis();
				this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
				if (log.isInfoEnabled()) {
					log.info("任务[{}]的下次计划执行时间[{}]", this.getName(), this.scheduledTime);
				}
				return this;
			}
		}
	}

	public void run() {
		Date actualExecutionTime = new Date();
		this.delegate.run();
		Date completionTime = new Date();
		Object arg2 = this.mutex;
		synchronized (this.mutex) {
			this.taskContext.update(this.scheduledTime, actualExecutionTime, completionTime);
		}

		if (!this.currentFuture.isCancelled()) {
			this.schedule();
		}

	}

	public String getName() {
		return this.delegate.getName();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return this.currentFuture.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled() {
		return this.currentFuture.isCancelled();
	}

	public boolean isDone() {
		return this.currentFuture.isDone();
	}

	public Object get() throws InterruptedException, ExecutionException {
		return this.currentFuture.get();
	}

	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.currentFuture.get(timeout, unit);
	}

	public long getDelay(TimeUnit unit) {
		return this.currentFuture.getDelay(unit);
	}

	public int compareTo(Delayed other) {
		if (this == other) {
			return 0;
		} else {
			long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
			return diff == 0L ? 0 : (diff < 0L ? -1 : 1);
		}
	}

	public boolean equals(Object obj) {
		return this == obj;
	}
}