package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.util.Date;
import java.util.TimeZone;

import com.watermelon.manager.common.manager.scheduler.TaskContext;
import com.watermelon.manager.common.manager.scheduler.Trigger;


public class CronTrigger implements Trigger {
	private final CronSequenceGenerator sequenceGenerator;

	public CronTrigger(String expression) {
		this(expression, TimeZone.getDefault());
	}

	public CronTrigger(String cronExpression, TimeZone timeZone) {
		this.sequenceGenerator = new CronSequenceGenerator(cronExpression, timeZone);
	}

	public Date nextTime(TaskContext context) {
		Date date = context.lastCompletionTime();
		Date result;
		if (date != null) {
			result = context.lastScheduledTime();
			if (result != null && date.before(result)) {
				date = result;
			}
		} else {
			date = new Date();
		}

		result = this.sequenceGenerator.next(date);
		return result;
	}
}