package org.hanihome.hanihomebe.property.web.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertySummaryDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertySummaryDTO.class, name = "RENT")
})
public sealed interface PropertySummaryDTO permits SharePropertySummaryDTO, RentPropertySummaryDTO {
    Long id();

    BigDecimal weeklyCost();

    String suburb();

    Double internalArea();

    int totalFloors();

    boolean billIncluded();

    String thumbnailUrl();

    LocalDateTime createdAt();

    int wishCount();

    TradeStatus tradeStatus();
}
