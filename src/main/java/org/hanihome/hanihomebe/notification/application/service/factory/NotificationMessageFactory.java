package org.hanihome.hanihomebe.notification.application.service.factory;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.factory.creator.OneOnOneConsultNotificationMessageCreator;
import org.hanihome.hanihomebe.notification.application.service.factory.creator.PropertyNotificationMessageCreator;
import org.hanihome.hanihomebe.notification.application.service.factory.creator.VerificationNotificationMessageCreator;
import org.hanihome.hanihomebe.notification.application.service.factory.creator.ViewingNotificationMessageCreator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationMessageFactory {
    private final ViewingNotificationMessageCreator viewingNotificationMessageCreator;
    private final VerificationNotificationMessageCreator verificationNotificationMessageCreator;
    private final OneOnOneConsultNotificationMessageCreator oneOnOneConsultNotificationMessageCreator;
    private final PropertyNotificationMessageCreator propertyNotificationMessageCreator;

    public ViewingNotificationMessageCreator getViewingMessageCreator() {
        return viewingNotificationMessageCreator;
    }
    public VerificationNotificationMessageCreator getVerificationMessageCreator() {
        return verificationNotificationMessageCreator;
    }
    public OneOnOneConsultNotificationMessageCreator getOneOnOneConsultMessageCreator() {
        return oneOnOneConsultNotificationMessageCreator;
    }
    public PropertyNotificationMessageCreator getPRopertyMessageCreator() {
        return propertyNotificationMessageCreator;
    }
}
