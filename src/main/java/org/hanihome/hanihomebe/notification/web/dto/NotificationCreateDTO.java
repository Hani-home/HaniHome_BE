package org.hanihome.hanihomebe.notification.web.dto;

import org.hanihome.hanihomebe.notification.domain.NotificationType;

public record NotificationCreateDTO (
         Long receiverId,
         String title,
         String content,
         NotificationType notificationType

){
    public static NotificationCreateDTO create(Long receiverId, String title, String content, NotificationType notificationType) {
        return new NotificationCreateDTO(receiverId, title, content, notificationType);
    }
}
