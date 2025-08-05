package org.hanihome.hanihomebe.notification.application.service.factory.creator;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.domain.NotificationType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class PropertyNotificationMessageCreator {
    public NotificationCreateDTO createMeetingDateReminderBefore48h(Long hostId) {
        String title = "뷰잉 예약 기간이 이틀 후에 종료돼요";
        String content = "게스트를 찾지 못했다면 기간을 연장하거나 정보를 수정해보세요";
        NotificationType type = NotificationType.PROPERTY;
        return NotificationCreateDTO.create(hostId, title, content, type);
    }
    public NotificationCreateDTO createMeetingDateReminderBefore24h(Long hostId) {
        String title = "뷰잉 예약 기간이 내일 종료돼요";
        String content = "게스트를 찾지 못했다면 기간을 연장하거나 정보를 수정해보세요";
        NotificationType type = NotificationType.PROPERTY;
        return NotificationCreateDTO.create(hostId, title, content, type);
    }
}
