package org.hanihome.hanihomebe.property.web.dto.response.summary;

import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyDTOByView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SharePropertySummaryDTO(
        Long id,
        BigDecimal weeklyCost,
        String suburb,
        Double internalArea,
        int totalFloors,
        boolean billIncluded,
        SharePropertySubType propertySubType,
        String thumbnailUrl,
        LocalDateTime createdAt,
        int wishCount,
        TradeStatus tradeStatus
) implements PropertySummaryDTO, PropertyDTOByView {
    public static SharePropertySummaryDTO from(ShareProperty entity) {
        return new SharePropertySummaryDTO(
                entity.getId(),
                entity.getCostDetails().getWeeklyCost(),
                entity.getRegion().getSuburb(),
                entity.getShareInternalDetails().getInternalArea(),
                entity.getShareInternalDetails().getTotalFloors(),
                entity.getCostDetails().isBillIncluded(),
                entity.getSharePropertySubType(),
                entity.getThumbnailUrl(),
                entity.getCreatedAt(),
                entity.getWishCount(),
                entity.getTradeStatus()
        );
    }

}
