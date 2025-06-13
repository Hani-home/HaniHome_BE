package org.hanihome.hanihomebe.viewing.web.dto;

import java.util.List;

public record ViewingChecklistRequestDTO(
        Long viewingId,
        List<Long> optionItemIds
) {
}
