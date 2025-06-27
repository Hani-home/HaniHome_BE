package org.hanihome.hanihomebe.item.web.dto;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;

public record OptionCategoryResponseDTO(
        Long id,
        CategoryCode categoryCode,
        String name
) {
    public static OptionCategoryResponseDTO from(OptionCategory category) {
        return new OptionCategoryResponseDTO(category.getId(), category.getCategoryCode(), category.getName());
    }
}
