package org.hanihome.hanihomebe.notification.application.service.factory.creator;

import org.hanihome.hanihomebe.notification.domain.NotificationType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class OneOnOneConsultNotificationMessageCreator implements NotificationMessageCreator{
    /// one on one consult
    public NotificationCreateDTO createOneOnOneConsultRepliedMessage(Long receiverId) {
        String title = "1:1 문의에 대한 답변이 완료되었어요";
        String content = "이메일로 확인해주세요";
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.ONE_ON_ONE_CONSULT_REPLIED);
    }
}
