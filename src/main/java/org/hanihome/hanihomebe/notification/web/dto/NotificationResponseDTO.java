package org.hanihome.hanihomebe.notification.web.dto;

import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.domain.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDTO(
        Long id,
        String title,
        String content,
        boolean isRead,
        NotificationType type,
        LocalDateTime createdAt

) {
    public static NotificationResponseDTO from(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getType(),
                notification.getCreatedAt());
    }
}