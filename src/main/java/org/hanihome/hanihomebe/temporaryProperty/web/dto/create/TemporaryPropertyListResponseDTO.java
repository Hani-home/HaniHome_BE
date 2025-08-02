package org.hanihome.hanihomebe.temporaryProperty.web.dto.create;

import java.time.LocalDateTime;

public record TemporaryPropertyListResponseDTO(
        Long temporaryPropertyId,
        LocalDateTime createdAt
) {
}
