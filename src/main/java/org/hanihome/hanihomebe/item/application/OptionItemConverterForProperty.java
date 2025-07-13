package org.hanihome.hanihomebe.item.application;

import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OptionItemConverterForProperty implements OptionItemConverter<PropertyOptionItem>{

    @Override
    public List<OptionItemResponseDTO> toResponseDTO(List<PropertyOptionItem> propertyOptionItems) {

        return propertyOptionItems.stream()
                .map(propertyOptionItem ->
                        {
                            OptionItem optionItem = propertyOptionItem.getOptionItem();
                            return OptionItemResponseDTO.from(optionItem);
                        }
                )
                .toList();
    }
}
