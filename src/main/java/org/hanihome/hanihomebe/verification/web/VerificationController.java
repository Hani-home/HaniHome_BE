package org.hanihome.hanihomebe.verification.web;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.verification.service.VerificationNotificationService;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.hanihome.hanihomebe.verification.web.dto.VerificationAdminSummaryResponseDTO;
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



}
