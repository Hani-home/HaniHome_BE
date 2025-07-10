package org.hanihome.hanihomebe.viewing.web.dto.checklist;

import java.util.List;

public record ViewingChecklistRequestDTO(
        Long viewingId,
        List<Long> allOptionItemIds
) {
}
