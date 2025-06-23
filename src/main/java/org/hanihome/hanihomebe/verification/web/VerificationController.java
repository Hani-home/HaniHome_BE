package org.hanihome.hanihomebe.verification.web;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.NotificationFacadeService;
import org.hanihome.hanihomebe.notification.application.service.NotificationMessageFactory;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.verification.service.VerificationNotificationService;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.hanihome.hanihomebe.verification.web.dto.VerificationRejectRequestDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationRequestDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/verifications")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory messageFactory;
    private final VerificationNotificationService verificationNotificationService;

    /*
    Create. 사용자용 신원인증 요청
     */
    @PostMapping
    public ResponseEntity<VerificationResponseDTO> requestVerify(@RequestBody VerificationRequestDTO verificationRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        VerificationResponseDTO response = verificationService.requestVerification(verificationRequestDTO, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    /*
    Read. 사용자용, 본인의 모든 신원 요청 불러오기
     */
    @GetMapping
    public ResponseEntity<List<VerificationResponseDTO>> getMyAllVerifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<VerificationResponseDTO> response = verificationService.getMyAllVerifications(userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    /*
    Read. 사용자용, 본인의 개별 신원 요청 불러오기
     */
    @GetMapping("/{verificationId}")
    public ResponseEntity<VerificationResponseDTO> getMyVerification(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long verificationId) {
        VerificationResponseDTO response = verificationService.getMyVerification(userDetails.getUserId(), verificationId);
        return ResponseEntity.ok(response);
    }

    /*
    Read. 관리자용, 모든 신원 요청 불러오기
    TODO : 추후 ADMIN 접근 권한 설정
    @PreAuthorize("hasRole('ADMIN')")
     */
    @GetMapping("/admin")
    public ResponseEntity<List<VerificationResponseDTO>> getAllVerificationsForAdmin() {
        List<VerificationResponseDTO> response = verificationService.getAllVerificationsForAdmin();
        return ResponseEntity.ok(response);
    }

    /*
    Update 관리자가 승인 or 거부
    TODO : 추후 ADMIN 접근 권한 설정
    @PreAuthorize("hasRole('ADMIN')")
     */
    @PatchMapping("/admin/{verificationId}/approve")
    public ResponseEntity<String> approveVerification(@PathVariable Long verificationId) {
        verificationService.approveVerification(verificationId);

        // 승인 시 문의자에게 알림
        verificationNotificationService.sendApproveNotification(verificationId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{verificationId}/reject")
    public ResponseEntity<String> rejectVerification(@RequestBody VerificationRejectRequestDTO verificationRejectRequestDTO, @PathVariable Long verificationId) {
        verificationService.rejectVerification(verificationRejectRequestDTO.getReason(), verificationId);

        // 거부 시 문의자에게 알림
        verificationNotificationService.sendRejectNotification(verificationId, verificationRejectRequestDTO.getReason());
        return ResponseEntity.ok().build();
    }

    /*
    Delete 얘는 사용자...? 관리자...? 고민이네...
    추후 프로젝트 진행 방향성에 따라 작성하겠습니다.
     */










}
