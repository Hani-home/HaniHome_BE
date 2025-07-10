package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

public record SharePropertySummaryDTO(
        Long id,
        String suburb,
        Double internalArea,
        int totalFloor,
        boolean billIncluded,
        SharePropertySubType propertySubType,
        String thumbnailUrl
) implements PropertySummaryDTO {
    public static SharePropertySummaryDTO from(ShareProperty entity) {
        return new SharePropertySummaryDTO(
                entity.getId(),
                entity.getRegion().getSuburb(),
                entity.getInternalArea(),
                entity.getTotalFloors(),
                entity.isBillIncluded(),
                entity.getSharePropertySubType(),
                entity.getThumbnailUrl()
        );
    }
}
