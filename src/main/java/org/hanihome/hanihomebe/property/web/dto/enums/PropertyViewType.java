package org.hanihome.hanihomebe.property.web.dto.enums;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyDTOByView;
import org.hanihome.hanihomebe.property.web.dto.response.basic.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;

@RequiredArgsConstructor
public enum PropertyViewType {
    DEFAULT("상세정보", PropertyResponseDTO.class),
    SUMMARY("매물 카드 형식에서 제공되는 정보", PropertySummaryDTO.class),
    ;
    private final String description;
    private final Class<? extends PropertyDTOByView> dtoClass;
}
