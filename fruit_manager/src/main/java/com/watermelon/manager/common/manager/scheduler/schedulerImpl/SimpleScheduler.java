package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.Date;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.watermelon.manager.common.manager.scheduler.ScheduledTask;
import com.watermelon.manager.common.manager.scheduler.Scheduler;
import com.watermelon.manager.common.manager.scheduler.Trigger;


@Component
public class SimpleScheduler implements Scheduler {
	private static final Logger log = LoggerFactory.getLogger(SimpleScheduler.class);
	@Autowired(required = false)
	@Qualifier("scheduling_delay_time")
	private Long delayTime = Long.valueOf(60000L);
	@Autowired(required = false)
	@Qualifier("scheduling_pool_size")
	private Integer poolSize = Integer.valueOf(5);
	private FixScheduledThreadPoolExecutor executor;

	@PostConstruct
	protected void init() {
		if (log.isInfoEnabled()) {
			log.info("定时任务线程池大小:{}，修正时间延迟:{}", this.poolSize, this.delayTime);
		}

		this.executor = new FixScheduledThreadPoolExecutor(this.poolSize, this.delayTime);
	}

	@Override
    public ScheduledFuture schedule(ScheduledTask task, final Trigger trigger) {
        try {
            task = new LogDecorateTask(task);
            return new SchedulingRunner(task, trigger, this.executor).schedule();
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    @Override
    public ScheduledFuture schedule(ScheduledTask task, final Date startTime) {
        final long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            task = new LogDecorateTask(task);
            return this.executor.schedule(task, initialDelay, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    @Override
    public ScheduledFuture<?> schedule(final ScheduledTask task, final String cron) {
        final CronTrigger trigger = new CronTrigger(cron);
        return (ScheduledFuture<?>)this.schedule(task, trigger);
    }
    
    @Override
    public ScheduledFuture scheduleAtFixedRate(ScheduledTask task, final Date startTime, final long period) {
        final long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            task = new LogDecorateTask(task);
            return this.executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    @Override
    public ScheduledFuture scheduleAtFixedRate(ScheduledTask task, final long period) {
        try {
            task = new LogDecorateTask(task);
            return this.executor.scheduleAtFixedRate(task, 0L, period, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    @Override
    public ScheduledFuture scheduleWithFixedDelay(ScheduledTask task, final Date startTime, final long delay) {
        final long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            task = new LogDecorateTask(task);
            return this.executor.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    @Override
    public ScheduledFuture scheduleWithFixedDelay(ScheduledTask task, final long delay) {
        try {
            task = new LogDecorateTask(task);
            return this.executor.scheduleWithFixedDelay(task, 0L, delay, TimeUnit.MILLISECONDS);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("执行器不接受[" + task.getName() + "]该任务", ex);
        }
    }
    
    private static class LogDecorateTask implements ScheduledTask
    {
        private ScheduledTask task;
        
        public LogDecorateTask(final ScheduledTask task) {
            this.task = task;
        }
        
        @Override
        public String getName() {
            return this.task.getName();
        }
        
        @Override
        public void run() {
            if (SimpleScheduler.log.isInfoEnabled()) {
                SimpleScheduler.log.info("执行器不接受[" + task.getName() + "]该任务", (Object)this.task.getName(), (Object)new Date());
            }
            try {
                this.task.run();
            }
            catch (RuntimeException e) {
                SimpleScheduler.log.error("执行器不接受[" + task.getName() + "]该任务", (Throwable)e);
                throw e;
            }
            if (SimpleScheduler.log.isInfoEnabled()) {
                SimpleScheduler.log.info("执行器不接受[" + task.getName() + "]该任务", (Object)this.task.getName(), (Object)new Date());
            }
        }
    }

}
