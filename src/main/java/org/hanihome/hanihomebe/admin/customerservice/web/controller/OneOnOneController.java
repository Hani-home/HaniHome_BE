package org.hanihome.hanihomebe.admin.customerservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.admin.customerservice.application.OneOnOneNotificationService;
import org.hanihome.hanihomebe.admin.customerservice.application.OneOnOneService;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsultStatus;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultCreateDTO;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultReplyDTO;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultResponseDTO;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.NotificationMessageFactory;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class OneOnOneController {
    private final OneOnOneService oneOnOneService;
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;
    private final OneOnOneNotificationService oneOnOneNotificationService;

    // 상담 등록
    @PostMapping("/one-on-one-consult")
    public OneOnOneConsultResponseDTO create(@RequestBody OneOnOneConsultCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return oneOnOneService.createOneOnOneConsult(dto, userDetails.getUserId());
    }

    // 모든 일대일 상담 내역 조회
    @GetMapping("/admin/one-on-one-consult")
    public List<OneOnOneConsultResponseDTO> getAll(@RequestParam(name = "status", required = false) OneOnOneConsultStatus status) {
        if (status != null) {
            return oneOnOneService.getAllByStatus(status);
        } else {
            return oneOnOneService.getAllOneOnOneConsult();
        }
    }

    // 운영자는 문의에 이메일로 답변 완료
    @PostMapping("/admin/one-on-one-consult/{oneOnOneConsultId}/reply")
    public void replyByEmail(@PathVariable Long oneOnOneConsultId,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestBody OneOnOneConsultReplyDTO dto) {
        // 1. OneOnOne 처리
        oneOnOneService.replyByEmail(oneOnOneConsultId, userDetails.getUserId());
        // 2. 알림 전송
        oneOnOneNotificationService.sendOneOnOneConsultRepliedNotification(userDetails.getUserId());
    }
}
