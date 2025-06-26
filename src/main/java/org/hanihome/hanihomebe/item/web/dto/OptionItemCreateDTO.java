package org.hanihome.hanihomebe.item.web.dto;

import org.hanihome.hanihomebe.item.domain.CategoryCode;

public record OptionItemCreateDTO(
        CategoryCode categoryCode,
        Long parentItemId,
        String itemName
) { }
