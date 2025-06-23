package org.hanihome.hanihomebe.verification.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

import java.util.List;

@Getter
public class VerificationRequestDTO {
    private VerificationType type;
    private List<String> documentImageUrls;
}
