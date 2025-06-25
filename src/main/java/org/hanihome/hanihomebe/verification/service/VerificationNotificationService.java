package org.hanihome.hanihomebe.verification.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class VerificationNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;

    public void sendRejectNotification(Long verificationId, String reason) {
        NotificationCreateDTO message = messageFactory.createVerificationRejectMessage(verificationId, reason);
        notificationFacadeService.sendNotification(message);
    }

    public void sendApproveNotification(Long verificationId) {
        NotificationCreateDTO message = messageFactory.createVerificationApproveMessage(verificationId);
        notificationFacadeService.sendNotification(message);
    }
}
