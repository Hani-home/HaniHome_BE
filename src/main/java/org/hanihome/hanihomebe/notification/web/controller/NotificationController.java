package org.hanihome.hanihomebe.notification.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.EmitterService;
import org.hanihome.hanihomebe.notification.application.service.NotificationService;
import org.hanihome.hanihomebe.notification.web.dto.NotificationResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final EmitterService emitterService;

    /**
     * SSE 스트림 연결 요청 엔드포인트
     */
    @GetMapping("/stream")
    public SseEmitter stream(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return emitterService.connect(userDetails.getUserId());
    }

    @GetMapping("/my-notifications")
    public List<NotificationResponseDTO> getNotifications(@RequestParam(required = false) Boolean read, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return notificationService.getMyNotifications(userDetails.getUserId(), read).stream()
                .map(NotificationResponseDTO::from)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 추가 검증 로직(작성자 검사) 필요 시 넣을 수 있음
        notificationService.markAsRead(notificationId);
    }
}
