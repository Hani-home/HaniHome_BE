package org.hanihome.hanihomebe.property.domain.command;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.property.application.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

@Getter
@SuperBuilder
public class RentPropertyPatchCommand extends PropertyPatchCommand {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private RentInternalDetails internalDetails;
    private CapacityRent capacityRent;                  // (RentProperty 고유) 수용인원-렌트


    @AssertTrue(message = "timeSlot의 timeFrom, timeTo는 30분 단위여야 합니다.")
    private boolean isValidTimeSlot() {
        // timeSlot is NULL
        if (super.getMeetingDateFrom() == null && super.getMeetingDateTo() == null && super.getTimeSlots() == null) {
            return true;    // patch라서 통과
        } else {
            return TimeSlotValidator.validateAllConditions(super.getTimeSlots());
        }
    }
}
