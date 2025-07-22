package org.hanihome.hanihomebe.item.application.converter;

import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.viewing.domain.ViewingOptionItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OptionItemConverterForViewing implements OptionItemConverter<ViewingOptionItem> {
    @Override
    public List<OptionItemResponseDTO> toOptionItemResponseDTO(List<ViewingOptionItem> viewingOptionItems) {
        return viewingOptionItems.stream()
                .map(viewingOptionItem -> {
                    OptionItem optionItem = viewingOptionItem.getOptionItem();
                    return OptionItemResponseDTO.from(optionItem);
                })
                .toList();

    }
}
