package org.hanihome.hanihomebe.property.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

@Getter
public class RentPropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private RealEstateType isRealEstateIntervention;    // (RentProperty 고유) 부동산 중개 여부
    private CapacityRent capacityRent;                  // (RentProperty 고유) 수용인원-렌트
    private Exposure exposure;                           // (RentProperty 고유) 남향북향
}
