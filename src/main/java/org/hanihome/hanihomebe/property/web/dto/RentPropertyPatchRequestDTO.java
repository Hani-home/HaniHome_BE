package org.hanihome.hanihomebe.property.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

@Getter
public class RentPropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private Boolean isRealEstateIntervention;            // (RentProperty 고유) 중개 여부
}
