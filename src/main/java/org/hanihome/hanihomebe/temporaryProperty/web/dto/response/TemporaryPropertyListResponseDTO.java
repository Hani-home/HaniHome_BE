package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import java.time.LocalDateTime;

public record TemporaryPropertyListResponseDTO(
        Long temporaryPropertyId,
        LocalDateTime createdAt
) {
}
