package com.watermelon.manager.common.manager.scheduler.schedulerImpl;


public interface RejectedExecutionHandler {
	void rejectedExecution(Runnable arg0, FixThreadPoolExecutor arg1);
}