package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;

import java.util.List;

public record ShareDetailDTO(
        SharePropertySubType sharePropertySubType,
        CapacityShare capacityShare,
        ShareInternalDetails shareInternalDetails,
        List<Long> optionItemIds,
        List<Long> highlightOptionItemIds

) implements DetailDTO {

}
