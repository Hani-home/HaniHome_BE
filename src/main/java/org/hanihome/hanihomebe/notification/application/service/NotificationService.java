package org.hanihome.hanihomebe.notification.application.service;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.repository.NotificationRepository;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.notification.web.dto.NotificationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public NotificationResponseDTO createNotification(NotificationCreateDTO dto) {
        Member receiver = memberRepository.findById(dto.receiverId())
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
        Notification notification = Notification.create(receiver, dto.title(), dto.content(), dto.notificationType());

        notificationRepository.save(notification);
        return NotificationResponseDTO.from(notification);
    }
    /**
     * 알림 목록 조회
     * @param userId 알림대상 사용자 ID
     * @return 알림 리스트
     */
    public List<Notification> getMyNotifications(Long userId, Boolean isRead) {
        return notificationRepository.findMyNotificationAndIsReadOptional(userId, isRead);
    }

    /**
     * 알림을 읽음으로 상태변경합니다
     * @param notificationId 알림ID
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        n.updateAsRead();
        notificationRepository.save(n);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> list = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        list.forEach(n -> n.updateAsRead());
        notificationRepository.saveAll(list);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

}
