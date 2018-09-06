package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

public class TaskRejectedException extends RuntimeException {
	private static final long serialVersionUID = 6681519082476492615L;

	public TaskRejectedException() {
	}

	public TaskRejectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskRejectedException(String message) {
		super(message);
	}

	public TaskRejectedException(Throwable cause) {
		super(cause);
	}
}