package org.hanihome.hanihomebe.admin.customerservice.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OneOnOneNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;

    public void sendOneOnOneConsultRepliedNotification(Long customerId) {
        NotificationCreateDTO message = messageFactory.createOneOnOneConsultRepliedMessage(customerId);
        notificationFacadeService.sendNotification(message);
    }
}
