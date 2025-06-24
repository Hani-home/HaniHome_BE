package org.hanihome.hanihomebe.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.global.BaseEntity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder(access = PROTECTED)
@Table(name = "notifications")
@Entity
public class Notification extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private Long receiverId;

    private String title;

    private String content;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // VIEWING_REMINDER, VIEWING_CREATED, VIEWING_CANCELED

    public static Notification create(Long receiverId, String title, String content) {
        return Notification.builder()
                .receiverId(receiverId)
                .title(title)
                .content(content)
                .isRead(false)
                .build();
    }

    public void updateAsRead() {
        this.isRead = true;
    }
}

