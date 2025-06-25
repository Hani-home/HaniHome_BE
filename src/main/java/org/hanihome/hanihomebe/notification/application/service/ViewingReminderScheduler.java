package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ViewingReminderScheduler {
    private final NotificationPushService notificationPushService;
    private final TaskScheduler taskScheduler;

    public void scheduleReminder(Long notificationId,LocalDateTime triggerTime) {
        Instant triggerInstant = triggerTime.atOffset(ZoneOffset.UTC).toInstant();
        // 미팅이 24시간 미만으로 남았을 경우 즉시 전송
        if (!triggerInstant.isAfter(Instant.now())) {
            notificationPushService.pushNotification(notificationId);
        }
        // task scheduling
        else {
            Date triggerDate = Date.from(triggerInstant);
            taskScheduler.schedule(() -> notificationPushService.pushNotification(notificationId), triggerDate);
        }
    }
}
