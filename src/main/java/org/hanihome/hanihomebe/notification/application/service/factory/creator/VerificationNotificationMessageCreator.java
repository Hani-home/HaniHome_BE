package org.hanihome.hanihomebe.notification.application.service.factory.creator;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.domain.NotificationType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VerificationNotificationMessageCreator implements NotificationMessageCreator{
    private final VerificationService verificationService;

    /// Verification
    public NotificationCreateDTO createVerificationApproveMessage(Long verificationId) {
        String title = "신원 인증 검수가 완료되었습니다";
        String content = "신원 인증에 성공했습니다";
        Long receiverId = verificationService.getVerificationById(verificationId).getMemberId();
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.VERIFICATION_CHECKED);
    }

    public NotificationCreateDTO createVerificationRejectMessage(Long verificationId, String reason) {
        String title = "신원 인증 검수가 완료되었습니다";
        String content = "아래와 같은 이유로 신원 인증에 실패했습니다 \n"+reason;
        Long receiverId = verificationService.getVerificationById(verificationId).getMemberId();
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.VERIFICATION_CHECKED);
    }
}
