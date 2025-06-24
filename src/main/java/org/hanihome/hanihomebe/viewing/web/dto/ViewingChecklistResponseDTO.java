package org.hanihome.hanihomebe.viewing.web.dto;

import java.util.List;


public record ViewingChecklistResponseDTO(
        Long viewingId,
        List<Long> checklistOptionItemIds

) {
    public static ViewingChecklistResponseDTO from(Long viewingId, List<Long> optionItemIds) {
        return new ViewingChecklistResponseDTO(viewingId, optionItemIds);
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
