package com.watermelon.manager.common.manager.scheduler;

import java.util.Date;

public interface Trigger {
	Date nextTime(TaskContext arg0);
}