package com.watermelon.manager.common.manager.scheduler;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public interface Scheduler {
	ScheduledFuture<?> schedule(ScheduledTask arg0, Trigger arg1);

	ScheduledFuture<?> schedule(ScheduledTask arg0, Date arg1);

	ScheduledFuture<?> schedule(ScheduledTask arg0, String arg1);

	ScheduledFuture<?> scheduleAtFixedRate(ScheduledTask arg0, Date arg1, long arg2);

	ScheduledFuture<?> scheduleAtFixedRate(ScheduledTask arg0, long arg1);

	ScheduledFuture<?> scheduleWithFixedDelay(ScheduledTask arg0, Date arg1, long arg2);

	ScheduledFuture<?> scheduleWithFixedDelay(ScheduledTask arg0, long arg1);
}
