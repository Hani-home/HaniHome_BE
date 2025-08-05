package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.domain.TaskType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Service
public class TaskReminderScheduler {
    private final NotificationPushService notificationPushService;
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleReminder(Long notificationId, LocalDateTime triggerTime, TaskType taskType) {
        Instant triggerInstant = triggerTime.atOffset(ZoneOffset.UTC).toInstant();
        // 미팅이 24시간 미만으로 남았을 경우 즉시 전송
        if (!triggerInstant.isAfter(Instant.now())) {
            notificationPushService.pushNotification(notificationId);
        }
        // task scheduling
        else {
            String rawKey = generateRawKey(notificationId, taskType);
            String uuidKey= toUUID(rawKey);

            cancelTask(uuidKey);

            Date triggerDate = Date.from(triggerInstant);
            Runnable task = () -> {
                notificationPushService.pushNotification(notificationId);
                scheduledTasks.remove(uuidKey);
            };
            ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, triggerDate);

            scheduledTasks.put(uuidKey, scheduledFuture);
        }
    }

    public void cancelTask(String uuidKey) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.remove(uuidKey);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private static String toUUID(String rawKey) {
        return UUID.nameUUIDFromBytes(rawKey.getBytes(StandardCharsets.UTF_8)).toString();
    }

    private static String generateRawKey(Long notificationId, TaskType taskType) {
        return Notification.class.getSimpleName() + "_" + String.valueOf(notificationId) + "_" + taskType;
    }
}
