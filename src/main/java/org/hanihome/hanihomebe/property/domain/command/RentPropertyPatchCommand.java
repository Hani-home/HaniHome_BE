package org.hanihome.hanihomebe.property.domain.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

@Getter
@SuperBuilder
public class RentPropertyPatchCommand extends PropertyPatchCommand {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private RealEstateType realEstateIntervention;    // (RentProperty 고유) 부동산 중개 여부
    private RentInternalDetails internalDetails;
    private CapacityRent capacityRent;                  // (RentProperty 고유) 수용인원-렌트
    private Exposure exposure;                           // (RentProperty 고유) 남향북향

}
