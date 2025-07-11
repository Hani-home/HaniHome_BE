package org.hanihome.hanihomebe.property.web.dto.response.summary;

import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyDTOByView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentPropertySummaryDTO(
        Long id,
        BigDecimal weeklyCost,
        String suburb,
        Double internalArea,
        int totalFloors,
        boolean billIncluded,
        RentPropertySubType propertySubType,
        String thumbnailUrl,
        LocalDateTime createdAt,
        int wishCount,
        TradeStatus tradeStatus
) implements PropertySummaryDTO, PropertyDTOByView {
    public static RentPropertySummaryDTO from(RentProperty entity) {
        return new RentPropertySummaryDTO(
                entity.getId(),
                entity.getCostDetails().getWeeklyCost(),
                entity.getRegion().getSuburb(),
                entity.getRentInternalDetails().getInternalArea(),
                entity.getRentInternalDetails().getTotalFloors(),
                entity.getCostDetails().isBillIncluded(),
                entity.getRentPropertySubType(),
                entity.getThumbnailUrl(),
                entity.getCreatedAt(),
                entity.getWishCount(),
                entity.getTradeStatus()
        );
    }

}