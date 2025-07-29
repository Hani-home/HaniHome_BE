package org.hanihome.hanihomebe.notification.repository;

import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.domain.NotificationSendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    /**
     * 내 알림 전체 조회
     * 또는
     * 읽기 여부에 따른 내 알림 조회
     * @param receiverId 요청한 사용자
     * @param isRead 알림 읽음여부(optional)
     * @return
     */
    @Query("select n from Notification n " +
            "where n.receiver.id = :receiverId " +
            "and (:isRead is null or n.isRead = :isRead) and n.notificationSendStatus in :sendStatusList")
    List<Notification> findMyNotificationByIsReadOptionalAndSendStatus(Long receiverId,
                                                                       Boolean isRead,
                                                                       List<NotificationSendStatus> sendStatusList);

}
