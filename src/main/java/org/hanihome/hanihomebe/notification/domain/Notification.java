package org.hanihome.hanihomebe.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.member.domain.Member;

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

//    private Long receiverId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member receiver;

    private String title;

    private String content;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationSendStatus notificationSendStatus; // 조회 시 sendStatus가 SUCCESS인 것만 조회되어야한다.

    @Enumerated(EnumType.STRING)
    private NotificationType type; // VIEWING_REMINDER, VIEWING_CREATED, VIEWING_CANCELED

    public static Notification create(Member receiver, String title, String content, NotificationType type) {
        return Notification.builder()
                .receiver(receiver)
                .title(title)
                .content(content)
                .type(type)
                .isRead(false)
                .notificationSendStatus(NotificationSendStatus.PENDING)
                .build();
    }

    public void updateAsRead() {
        this.isRead = true;
    }

    public void updateSendStatus(NotificationSendStatus notificationSendStatus) {
        this.notificationSendStatus = notificationSendStatus;
    }
}

