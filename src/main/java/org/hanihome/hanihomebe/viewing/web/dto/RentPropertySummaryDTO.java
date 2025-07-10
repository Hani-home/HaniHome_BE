package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

public record RentPropertySummaryDTO(
        Long id,
        String suburb,
        Double internalArea,
        int totalFloor,
        boolean billIncluded,
        RentPropertySubType propertySubType,
        String thumbnailUrl
) implements PropertySummaryDTO {
    public static PropertySummaryDTO from(RentProperty entity) {
        return new RentPropertySummaryDTO(
                entity.getId(),
                entity.getRegion().getSuburb(),
                entity.getInternalArea(),
                entity.getTotalFloors(),
                entity.isBillIncluded(),
                entity.getRentPropertySubType(),
                entity.getThumbnailUrl()
        );
    }

}