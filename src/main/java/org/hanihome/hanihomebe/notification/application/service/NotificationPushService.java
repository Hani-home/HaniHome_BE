package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.repository.NotificationRepository;
import org.hanihome.hanihomebe.notification.web.dto.NotificationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationPushService {
    private final NotificationRepository notificationRepository;
    private final EmitterService emitterService;

 /**
  * 특정 사용자에게 알림을 전송합니다.
  * @param notificationId 전송할 알림 엔티티
  */
 // TODO: 성능개선: 비동기 알림 발송
 public void pushNotification(Long notificationId) {
     Notification findNotification = notificationRepository.findById(notificationId)
             .orElseThrow(() -> new CustomException(ServiceCode.NOTIFICATION_NOT_EXISTS));
     Long receiverId = findNotification.getReceiverId();

     Map<String, SseEmitter> userEmitters = emitterService.getEmittersByUserId(receiverId);
     if (userEmitters == null) return;

     userEmitters.forEach((id, emitter) -> {
         try {
             emitter.send(SseEmitter.event()
                     .id(findNotification.getId().toString())
                     .name(findNotification.getType().name())
                     .data(NotificationResponseDTO.from(findNotification)));
         } catch (IOException e) {
             emitterService.removeEmitter(receiverId, id);
         }
     });
 }
}
