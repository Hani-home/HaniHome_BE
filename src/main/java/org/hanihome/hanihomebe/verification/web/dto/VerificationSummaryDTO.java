package org.hanihome.hanihomebe.verification.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

@Getter
@AllArgsConstructor
public class VerificationSummaryDTO {
    private VerificationType type;
    private boolean isVerified;
}
