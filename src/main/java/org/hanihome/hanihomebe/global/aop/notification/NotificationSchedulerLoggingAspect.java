package org.hanihome.hanihomebe.global.aop.notification;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.domain.TaskType;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class NotificationSchedulerLoggingAspect {

    @Pointcut("execution(* org.hanihome.hanihomebe.notification.application.service.TaskReminderScheduler.scheduleReminder(..))")
    public void schedulePointcut() {}

    @Pointcut("execution(* org.hanihome.hanihomebe.notification.application.service.TaskReminderScheduler.cancelTask(..))")
    public void cancelPointcut() {}

    @Before("schedulePointcut()")
    public void logBeforeSchedule(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length >= 3) {
            Class<?> entityClass = Notification.class;
            Long entityId = (Long) args[0];
            String taskType = String.valueOf((TaskType) args[2]);
            String rawKey = entityClass.getSimpleName() + "_" + entityId + "_" + taskType;

            log.info("[TaskReminderScheduler] 작업 등록 - {}", rawKey);
        }
    }

    @Before("cancelPointcut()")
    public void logBeforeCancel(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length >= 1) {
            String uuidKey = (String) joinPoint.getArgs()[0];
            log.info("[TaskReminderScheduler] 작업 취소 - UUIDKey={}", uuidKey);
        }
    }
}
