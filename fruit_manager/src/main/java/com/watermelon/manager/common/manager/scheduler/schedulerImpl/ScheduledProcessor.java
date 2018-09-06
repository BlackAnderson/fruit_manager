package com.watermelon.manager.common.manager.scheduler.schedulerImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.support.MethodInvokingRunnable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.watermelon.manager.common.manager.scheduler.Scheduled;
import com.watermelon.manager.common.manager.scheduler.ScheduledTask;
import com.watermelon.manager.common.manager.scheduler.Scheduler;


@Component
public class ScheduledProcessor implements BeanPostProcessor, ApplicationListener, Ordered {
	private final Map<ScheduledTask, String> tasks = new HashMap();
	@Autowired
	private Scheduler scheduler;

	public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        ReflectionUtils.doWithMethods(AopUtils.getTargetClass(bean), (ReflectionUtils.MethodCallback)new ReflectionUtils.MethodCallback() {
            public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
                final Scheduled annotation = (Scheduled)AnnotationUtils.getAnnotation(method, (Class)Scheduled.class);
                if (annotation == null) {
                    return;
                }
                final ScheduledTask task = ScheduledProcessor.this.createTask(bean, method, annotation);
                final String experssion = ScheduledProcessor.this.resolveExperssion(bean, annotation);
                ScheduledProcessor.this.tasks.put(task, experssion);
            }
        });
        return bean;
    }

	private ScheduledTask createTask(Object bean, Method method, Scheduled annotation) {
      if(!Void.TYPE.equals(method.getReturnType())) {
         throw new IllegalArgumentException("定时方法的返回值必须为 void");
      } else if(method.getParameterTypes().length != 0) {
         throw new IllegalArgumentException("定时方法不能有参数");
      } else {
         MethodInvokingRunnable runnable = new MethodInvokingRunnable();
         runnable.setTargetObject(bean);
         runnable.setTargetMethod(method.getName());
         runnable.setArguments(new Object[0]);

         try {
            runnable.prepare();
         } catch (Exception arg5) {
            throw new IllegalStateException("无法创建定时任务", arg5);
         }

         String name = annotation.name();
         return new ScheduledTask() {
             @Override
             public void run() {
                 runnable.run();
             }
             
             @Override
             public String getName() {
                 return name;
             }
         };
      }
   }

	private String resolveExperssion(Object bean, Scheduled annotation) {
		String result = null;

		try {
            switch (annotation.type()) {
                case EXPRESSION: {
                    result = annotation.value();
                    break;
                }
                case FIELD_NAME: {
                    final String name = annotation.value();
                    final Field field = bean.getClass().getDeclaredField(name);
                    if (!String.class.equals(field.getType())) {
                        throw new IllegalArgumentException(String.valueOf(field.getName()) + "的属性类型必须为字符串");
                    }
                    field.setAccessible(true);
                    result = (String)field.get(bean);
                    break;
                }
            }
		} catch (Exception arg5) {
			throw new IllegalArgumentException("无法获取正确的表达式值", arg5);
		}
		return result;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			Iterator arg2 = this.tasks.entrySet().iterator();

			while (arg2.hasNext()) {
				Entry entry = (Entry) arg2.next();
				this.scheduler.schedule((ScheduledTask) entry.getKey(), new CronTrigger((String) entry.getValue()));
			}
		}

	}

	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}
}