package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryPropertyStepStatus;

import java.time.LocalDateTime;

public record TemporaryPropertyListResponseDTO(
        Long temporaryPropertyId,
        TemporaryPropertyStepStatus status,
        LocalDateTime createdAt
) {
}
