package org.hanihome.hanihomebe.property.application;

import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;

import java.util.List;

public interface PropertyConverter<T> {
    PropertyViewType supports();

    T convert(Property property, List<OptionItemResponseDTO> optionItems);
//
//    T convert(Property property);
}