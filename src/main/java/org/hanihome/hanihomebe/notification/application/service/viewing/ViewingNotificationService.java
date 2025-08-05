package org.hanihome.hanihomebe.notification.application.service.viewing;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.factory.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.domain.TaskType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ViewingNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;


    public void sendViewingCanceledNotification(Long actorId, Long viewingId) {
        NotificationCreateDTO message = messageFactory.getViewingMessageCreator().createViewingCanceledMessage(actorId, viewingId);
        notificationFacadeService.sendNotification(message);
    }

    public void sendViewingCreateNotification(String actorName, Long viewingId) {
        NotificationCreateDTO message = messageFactory.getViewingMessageCreator().createViewingCreateMessage(actorName, viewingId);
        notificationFacadeService.sendNotification(message);
    }

    public void registerReminderNotification(Long viewingId, LocalDateTime meetingDay) {
        List<NotificationCreateDTO> reminderNotifications = messageFactory.getViewingMessageCreator().createViewingReminderMessage(viewingId);
        LocalDateTime triggerTime = meetingDay.minusHours(24);
        reminderNotifications.forEach(
                reminder->notificationFacadeService.registerReminderNotification(reminder, triggerTime, TaskType.VIEWING_REMINDER)
        );
    }

}
