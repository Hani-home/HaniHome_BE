package org.hanihome.hanihomebe.property.application.converter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class PropertyConvertContext {
    private Property property;
    private List<OptionItemResponseDTO> optionItems;
    private NearestMetroStopResponseDTO nearestMetroStopResponseDTO;

    public static PropertyConvertContext create(Property property, List<OptionItemResponseDTO> optionItems, NearestMetroStopResponseDTO nearestMetroStopResponseDTO) {
        return PropertyConvertContext.builder()
                .property(property)
                .optionItems(optionItems)
                .nearestMetroStopResponseDTO(nearestMetroStopResponseDTO)
                .build();
    }
}
