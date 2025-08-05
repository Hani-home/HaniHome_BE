package org.hanihome.hanihomebe.notification.application.service.property;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.factory.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.domain.TaskType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PropertyNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;

    public void registerMeetingDateReminderNotification(Long hostId, LocalDate meetingDateTo) {
        registerMeetingDateToReminderBefore24h(hostId, meetingDateTo);
        registerMeetingDateToReminderBefore48h(hostId, meetingDateTo);
    }

    private void registerMeetingDateToReminderBefore24h(Long hostId, LocalDate meetingDateTo) {
        NotificationCreateDTO message = messageFactory.getPRopertyMessageCreator().createMeetingDateReminderBefore24h(hostId);
        LocalDateTime triggerTime = meetingDateTo.atStartOfDay().minusHours(24);
        TaskType taskType = TaskType.PROPERTY_MEETING_DATE_REMINDER_BEFORE_24;
        notificationFacadeService.registerReminderNotification(message, triggerTime, taskType);
    }
    private void registerMeetingDateToReminderBefore48h(Long hostId, LocalDate meetingDateTo) {
        NotificationCreateDTO message = messageFactory.getPRopertyMessageCreator().createMeetingDateReminderBefore24h(hostId);
        LocalDateTime triggerTime = meetingDateTo.atStartOfDay().minusHours(48);
        TaskType taskType = TaskType.PROPERTY_MEETING_DATE_REMINDER_BEFORE_48;
        notificationFacadeService.registerReminderNotification(message, triggerTime, taskType);
    }
}
