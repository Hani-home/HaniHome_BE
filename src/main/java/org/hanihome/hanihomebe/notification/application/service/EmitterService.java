package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.notification.domain.Notification;
import org.hanihome.hanihomebe.notification.repository.NotificationRepository;
import org.hanihome.hanihomebe.notification.web.dto.NotificationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class EmitterService {
    /// 사용자별 Emitters는 jvm 메모리 ConcurrentHashMap 에서 관리
    /// 수평확장 하면 공유 안돼서 문제발생!
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    private static final Long DEFAULT_TIMEOUT = 0L; // 영구 연결

    private final NotificationRepository notificationRepository;

    /**
     * 새로운 SSE 연결을 생성하고 관리
     * @param userId 알림 수신자 사용자 ID
     * @return SseEmitter 객체
     */
    public SseEmitter connect(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        String emitterId = userId + "_" + UUID.randomUUID();
        emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()) //Map<String, SseEmitter>
                .put(emitterId, emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitterId));       // call back 이벤트 리스너 등록.
        emitter.onTimeout(()    -> removeEmitter(userId, emitterId));       //onCompletion, onTimeout, onError는 connect 함수 내에서 "등록만" 하는 것이고,
        emitter.onError((e) -> removeEmitter(userId, emitterId)); //실제 호출은 SSE 연결의 생명주기 도중에 Spring이 알아서 호출

        try {
            // 초기 연결 확인 이벤트
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("SSE connection established"));
        } catch (IOException ex) {
            removeEmitter(userId, emitterId);
        }

        return emitter;
    }


    public Map<String,SseEmitter>  getEmittersByUserId(Long userId) {
        return emitters.get(userId);
    }

    /**
     * 연결이 완료되거나 에러 발생 시 해당 emitter를 제거
     * @param userId 사용자 ID
     * @param emitterId emitter 고유 ID
     */
    public void removeEmitter(Long userId, String emitterId) {
        Map<String, SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitterId);
        }
    }

}
