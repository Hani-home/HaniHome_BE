package org.hanihome.hanihomebe.item.web.dto;

import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;

public record OptionItemResponseDTO(
        Long optionItemId,
        String itemName,
        boolean isActive,
        boolean isCustom,
        Long categoryId,
        String categoryName,
        Long parentItemId
) {
    public static OptionItemResponseDTO from(OptionItem optionItem) {
        Long parentId = optionItem.getParent() != null
                ? optionItem.getParent().getId()
                : null;

        OptionCategory category = optionItem.getOptionCategory();
        return new OptionItemResponseDTO(
                optionItem.getId(),
                optionItem.getItemName(),
                optionItem.isActive(),
                optionItem.isCustom(),
                category.getId(),
                category.getName(),
                parentId
        );
    }
}
