package org.hanihome.hanihomebe.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.domain.NotificationType;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.property.application.service.PropertyService;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCancelRequestDTO;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCreateDTO;
import org.hanihome.hanihomebe.viewing.web.dto.response.ViewingResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationMessageFactory {
    private final ViewingService viewingService;
    private final PropertyService propertyService;
    private final VerificationService verificationService;

    public NotificationCreateDTO createViewingCanceledMessage(Long actorId, Long viewingId) {
        ViewingResponseDTO findViewing = viewingService.getViewingById(viewingId);
        Long guestId = findViewing.getMemberId();
        Long hostId = propertyService.getPropertyById(findViewing.getPropertyId()).memberId();

        boolean isGuest = actorId.equals(guestId);
        String actor = isGuest ? "게스트" : "호스트";
        Long receiverId = isGuest ? hostId : guestId;

        String title = actor + "가 내 뷰잉 예약을 취소했어요";
        String content = "취소 사유를 확인하고 새로운 일정을 예약해주세요";
        NotificationType type = NotificationType.VIEWING_CANCELED;

        return NotificationCreateDTO.create(receiverId, title, content, type);
    }

    public NotificationCreateDTO createViewingCreateMessage(String actorName, Long viewingId) {
        Long hostId = propertyService.getPropertyById(viewingService.getViewingById(viewingId).getPropertyId()).memberId();

        String title = "뷰잉 요청 알림";
        String content = actorName + "님이 뷰잉을 요청하였습니다";
        NotificationType type = NotificationType.VIEWING_CREATED;

        return NotificationCreateDTO.create(hostId, title, content, type);
    }

    public List<NotificationCreateDTO> createViewingReminderMessage(Long viewingId) {
        ViewingResponseDTO viewing = viewingService.getViewingById(viewingId);
        Long guestId = viewing.getMemberId();
        Long hostId = propertyService.getPropertyById(viewing.getPropertyId()).memberId();
        Instant triggerTime = viewing.getMeetingDay().minusHours(24)
                .toInstant(ZoneOffset.UTC);

        String title="";
        String content="";
        if (triggerTime.isAfter(Instant.now())) {
            title = "예약한 뷰잉이 하루 남았어요";
            content = "24시간 뒤에 있을 뷰잉 일정을 확인해주세요";
        } else {
            title = "예약된 뷰잉에 준비해주세요";
            content = "24시간 내에 있을 뷰잉 일정을 확인해주세요";
        }


        NotificationCreateDTO hostDTO = NotificationCreateDTO.create(hostId, title, content, NotificationType.VIEWING_REMINDER);
        return List.of(NotificationCreateDTO.create(guestId, title, content, NotificationType.VIEWING_REMINDER), hostDTO);
    }

    public NotificationCreateDTO createOneOnOneConsultRepliedMessage(Long receiverId) {
        String title = "1:1 문의에 대한 답변이 완료되었어요";
        String content = "이메일로 확인해주세요";
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.ONE_ON_ONE_CONSULT_REPLIED);
    }

    public NotificationCreateDTO createVerificationApproveMessage(Long verificationId) {
        String title = "신원 인증 검수가 완료되었습니다";
        String content = "신원 인증에 성공했습니다";
        Long receiverId = verificationService.getVerificationById(verificationId).getMember().getId();
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.VERIFICATION_CHECKED);
    }

    public NotificationCreateDTO createVerificationRejectMessage(Long verificationId, String reason) {
        String title = "신원 인증 검수가 완료되었습니다";
        String content = "아래와 같은 이유로 신원 인증에 실패했습니다 \n"+reason;
        Long receiverId = verificationService.getVerificationById(verificationId).getMember().getId();
        return NotificationCreateDTO.create(receiverId, title, content, NotificationType.VERIFICATION_CHECKED);
    }
}
