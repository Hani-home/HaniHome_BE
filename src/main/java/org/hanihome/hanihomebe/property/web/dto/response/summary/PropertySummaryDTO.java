package org.hanihome.hanihomebe.property.web.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyDTOByView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertySummaryDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertySummaryDTO.class, name = "RENT")
})
public sealed interface PropertySummaryDTO
        extends PropertyDTOByView
        permits SharePropertySummaryDTO, RentPropertySummaryDTO {
    Long id();

    PropertySuperType kind();

    BigDecimal weeklyCost();

    String suburb();

    Double internalArea();

    int totalFloors();

    boolean billIncluded();

    NearestMetroStopResponseDTO nearestStation();

    String thumbnailUrl();

    LocalDateTime createdAt();

    int wishCount();

    TradeStatus tradeStatus();
}
