package org.hanihome.hanihomebe.viewing.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.web.dto.response.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.SharePropertyResponseDTO;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertySummaryDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertySummaryDTO.class, name = "RENT")
})
public sealed interface PropertySummaryDTO permits SharePropertySummaryDTO, RentPropertySummaryDTO {
    Long id();

    String suburb();

    Double internalArea();

    int totalFloor();

    boolean billIncluded();

    String thumbnailUrl();
}
