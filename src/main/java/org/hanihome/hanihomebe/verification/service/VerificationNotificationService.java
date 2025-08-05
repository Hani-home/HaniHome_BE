package org.hanihome.hanihomebe.verification.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.factory.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class VerificationNotificationService {
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;

    public void sendRejectNotification(Long verificationId, String reason) {
        NotificationCreateDTO message = messageFactory.getVerificationMessageCreator().createVerificationRejectMessage(verificationId, reason);
        notificationFacadeService.sendNotification(message);
    }

    public void sendApproveNotification(Long verificationId) {
        NotificationCreateDTO message = messageFactory.getVerificationMessageCreator().createVerificationApproveMessage(verificationId);
        notificationFacadeService.sendNotification(message);
    }
}
