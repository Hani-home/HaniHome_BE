package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;

import java.util.List;


public record ViewingChecklistResponseDTO(
        Long viewingId,
        List<Long> optionItemIds

) {
    public static ViewingChecklistResponseDTO from(Long viewingId, List<Long> checklistItemIds) {
        return new ViewingChecklistResponseDTO(viewingId, checklistItemIds);
    }

}
/*
public record ViewingChecklistResponseDTO(
        Long viewingId,
        List<OptionItemResponseDTO> optionItems

) {
    public static ViewingChecklistResponseDTO from(Long viewingId, List<OptionItem> optionItems) {
        List<OptionItemResponseDTO> optionItemDTOs = optionItems.stream()
                .map(optionItem -> OptionItemResponseDTO.from(optionItem))
                .toList();
        return new ViewingChecklistResponseDTO(viewingId, optionItemDTOs);
    }

}*/
