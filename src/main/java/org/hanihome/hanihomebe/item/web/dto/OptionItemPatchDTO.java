package org.hanihome.hanihomebe.item.web.dto;

import org.hanihome.hanihomebe.item.domain.CategoryCode;

public record OptionItemPatchDTO(
        CategoryCode categoryCode,
        Long parentItemId,
        String itemName
) {
}
