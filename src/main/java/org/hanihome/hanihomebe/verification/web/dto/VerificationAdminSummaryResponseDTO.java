package org.hanihome.hanihomebe.verification.web.dto;

import org.hanihome.hanihomebe.verification.domain.VerificationStatus;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

import java.time.LocalDateTime;

public record VerificationAdminSummaryResponseDTO(
        Long id,
        VerificationType verificationType,
        LocalDateTime requestedAt,
        VerificationStatus verificationStatus
) {
}
