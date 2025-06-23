package org.hanihome.hanihomebe.s3.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hanihome.hanihomebe.verification.domain.VerificationType;

import java.util.List;

@Getter
public class S3VerificationRequestDTO {
    private VerificationType type;

    @Size(min = 1)
    private List<String> fileExtensions;
}
