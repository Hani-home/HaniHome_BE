package org.hanihome.hanihomebe.property.web.dto.response.summary;

import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyDTOByView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentPropertySummaryDTO(
        Long id,
        PropertySuperType kind,
        RentPropertySubType propertySubType,
        BigDecimal weeklyCost,
        String suburb,
        Double internalArea,
        int totalFloors,
        boolean billIncluded,
        NearestMetroStopResponseDTO nearestStation,
        String thumbnailUrl,
        LocalDateTime createdAt,
        int wishCount,
        TradeStatus tradeStatus
) implements PropertySummaryDTO {
    public static RentPropertySummaryDTO from(RentProperty entity, NearestMetroStopResponseDTO nearestMetroStopResponseDTO) {
        return new RentPropertySummaryDTO(
                entity.getId(),
                entity.getKind(),
                entity.getRentPropertySubType(),
                entity.getCostDetails().getWeeklyCost(),
                entity.getRegion().getSuburb(),
                entity.getRentInternalDetails().getInternalArea(),
                entity.getRentInternalDetails().getTotalFloors(),
                entity.getCostDetails().isBillIncluded(),
                nearestMetroStopResponseDTO,
                entity.getThumbnailUrl(),
                entity.getCreatedAt(),
                entity.getWishCount(),
                entity.getTradeStatus()
        );
    }

}