package com.watermelon.manager.common.manager.scheduler;

import java.util.Date;

public interface TaskContext {
	Date lastScheduledTime();

	Date lastActualTime();

	Date lastCompletionTime();
}