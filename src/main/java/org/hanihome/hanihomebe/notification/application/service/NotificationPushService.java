package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.domain.NotificationSendStatus;
import org.hanihome.hanihomebe.notification.repository.NotificationRepository;
import org.hanihome.hanihomebe.notification.web.dto.NotificationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
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
 @Transactional
 public void pushNotification(Long notificationId) {
     Notification findNotification = notificationRepository.findById(notificationId)
             .orElseThrow(() -> new CustomException(ServiceCode.NOTIFICATION_NOT_EXISTS));
     Long receiverId = findNotification.getReceiver().getId();

     Map<String, SseEmitter> userEmitters = emitterService.getEmittersByUserId(receiverId);
     log.info("알림 전송 대기. receiverId: {" +
             "}, notificationId: {}", receiverId, notificationId);
     if (userEmitters == null || userEmitters.isEmpty()) {
         log.info("SSE emitter 없음: receiverId={}", receiverId);

         updateNotificationAsSuccess(findNotification);
         return;
     }

     log.info("알림 전송 시작. receiverId={}, notificationId={}, emitters={}",
             receiverId, notificationId, String.join(",", userEmitters.keySet()));
     userEmitters.forEach((id, emitter) -> {
         try {
             emitter.send(SseEmitter.event()
                     .id(findNotification.getId().toString())
                     .name(findNotification.getType().name()) // 클라이언트의 이벤트 리스너는 해당 이름으로 수신해야함
                     .data(NotificationResponseDTO.from(findNotification)));
             log.info("SSE 전송 성공: receiverId={}, emitterId={}", receiverId, id);

             updateNotificationAsSuccess(findNotification);
         } catch (IOException e) {
             log.warn("SSE 전송 실패: receiverId={}, notificationId={}, emitterId={}, error={}",
                     receiverId, notificationId, id, e.toString());

             updateNotificationAsFailed(findNotification);
             emitterService.removeEmitter(receiverId, id);
         } catch (Exception e) {
             log.info("message:{}", e.toString());

             updateNotificationAsFailed(findNotification);
             throw new CustomException(ServiceCode.NOTIFICATION_SEND_FAILED, e);
         }
     });

 }

    private static void updateNotificationAsFailed(Notification findNotification) {
        findNotification.updateSendStatus(NotificationSendStatus.FAILED);
    }

    private static void updateNotificationAsSuccess(Notification findNotification) {
        findNotification.updateSendStatus(NotificationSendStatus.SUCCESS);
    }
}
