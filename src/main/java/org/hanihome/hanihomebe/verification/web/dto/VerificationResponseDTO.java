package org.hanihome.hanihomebe.verification.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationStatus;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class VerificationResponseDTO {
    private Long id;
    private VerificationType type;
    private VerificationStatus status;
    private List<String> documentImageUrls;
    private String rejectionReason;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private Long memberId;
}
