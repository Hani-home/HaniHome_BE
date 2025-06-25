package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.domain.NotificationType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.property.application.service.PropertyService;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class NotificationFacadeService {
    // refactoring 내용
    // 여러 서비스의 조합이 필요한 경우
    // 컨트롤러가 로직의 흐름을 제어하지 않음
    // 컨트롤러에는 서비스 조합을 단순하게 노출시켜 하나의 진입점으로 만든다.
    private final NotificationService notificationService;
    private final NotificationPushService notificationPushService;
    private final ViewingReminderScheduler viewingReminderScheduler;

    public void sendNotification(NotificationCreateDTO dto) {
        // 알림 생성
        Long notificationId = notificationService.createNotification(dto)
                .id();
        // 알림 보내기
        notificationPushService.pushNotification(notificationId);
    }

    // 3. 24시간 전 리마인더 알림 등록
    public void registerReminderNotification(NotificationCreateDTO dto, LocalDateTime triggerTime) {
        // 알림생성
        Long notificationId = notificationService.createNotification(dto).id();
        // 알림전송 스케줄링
        viewingReminderScheduler.scheduleReminder(notificationId, triggerTime);
    }
    /*
    public void markAllAsRead(Long userId) {
        notificationService.markAllAsRead(userId);
        notificationPushService.pushNotification(userId, null);
    }
    public void deleteNotification(Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }*/
}
