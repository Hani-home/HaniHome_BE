package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;

import java.util.List;

public record RentDetailDTO(
        RentPropertySubType rentPropertySubType,
        CapacityRent capacityRent,
        RentInternalDetails rentInternalDetails,
        RealEstateType isRealEstateType,
        Exposure exposure,
        List<Long> optionItemIds,
        List<Long> highlightOptionItemIds

) implements DetailDTO {
}
