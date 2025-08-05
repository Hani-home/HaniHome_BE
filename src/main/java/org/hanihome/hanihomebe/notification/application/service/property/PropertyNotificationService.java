package org.hanihome.hanihomebe.notification.application.service.property;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.factory.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PropertyNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;

    public void registerReminderNotification(Long hostId, LocalDateTime meetingDateTo) {
        registerMeetingDateToReminderBefore24h(hostId, meetingDateTo);
        registerMeetingDateToReminderBefore48h(hostId, meetingDateTo);
    }

    private void registerMeetingDateToReminderBefore24h(Long hostId, LocalDateTime meetingDateTo) {
        NotificationCreateDTO message = messageFactory.getPRopertyMessageCreator().createMeetingDateReminderBefore24h(hostId);
        LocalDateTime triggerTime = meetingDateTo.minusHours(24);
        notificationFacadeService.registerReminderNotification(message, triggerTime);
    }
    private void registerMeetingDateToReminderBefore48h(Long hostId, LocalDateTime meetingDateTo) {
        NotificationCreateDTO message = messageFactory.getPRopertyMessageCreator().createMeetingDateReminderBefore24h(hostId);
        LocalDateTime triggerTime = meetingDateTo.minusHours(48);
        notificationFacadeService.registerReminderNotification(message, triggerTime);
    }
}
