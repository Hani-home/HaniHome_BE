package org.hanihome.hanihomebe.s3.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

@Getter
public class S3VerificationRequestDTO {
    private VerificationType type;
}
