package org.hanihome.hanihomebe.verification.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

@Getter
public class VerificationRequestDTO {
    private VerificationType type;
    private String documentImageUrl;
}
