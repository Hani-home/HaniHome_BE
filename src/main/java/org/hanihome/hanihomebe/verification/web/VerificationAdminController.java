package org.hanihome.hanihomebe.verification.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.verification.service.VerificationNotificationService;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.hanihome.hanihomebe.verification.web.dto.VerificationAdminSummaryResponseDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationRejectRequestDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/verifications")
@RequiredArgsConstructor
public class VerificationAdminController {

    private final VerificationService verificationService;
    private final VerificationNotificationService verificationNotificationService;

    /*
    Read. 관리자용, 모든 신원 요청 불러오기
    TODO : 추후 ADMIN 접근 권한 설정
    @PreAuthorize("hasRole('ADMIN')")
     */
    @GetMapping
    public ResponseEntity<List<VerificationAdminSummaryResponseDTO>> getAllVerificationsForAdmin() {
        List<VerificationAdminSummaryResponseDTO> response = verificationService.getAllVerificationsForAdmin();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{verificationId}")
    public ResponseEntity<VerificationResponseDTO> getVerificationForAdmin(@PathVariable Long verificationId) {
        VerificationResponseDTO response = verificationService.getVerificationById(verificationId);
        return ResponseEntity.ok(response);
    }

    /*
    Update 관리자가 승인 or 거부
    TODO : 추후 ADMIN 접근 권한 설정
    @PreAuthorize("hasRole('ADMIN')")
     */
    @PatchMapping("/{verificationId}/approve")
    public ResponseEntity<Void> approveVerification(@PathVariable Long verificationId) {
        verificationService.approveVerification(verificationId);

        // 승인 시 문의자에게 알림
        verificationNotificationService.sendApproveNotification(verificationId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{verificationId}/reject")
    public ResponseEntity<Void> rejectVerification(@RequestBody VerificationRejectRequestDTO verificationRejectRequestDTO, @PathVariable Long verificationId) {
        verificationService.rejectVerification(verificationRejectRequestDTO.getReason(), verificationId);

        // 거부 시 문의자에게 알림
        verificationNotificationService.sendRejectNotification(verificationId, verificationRejectRequestDTO.getReason());
        return ResponseEntity.ok().build();
    }

}
