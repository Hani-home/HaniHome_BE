package org.hanihome.hanihomebe.property.domain.command;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.property.application.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

@Getter
@SuperBuilder
public class SharePropertyPatchCommand extends PropertyPatchCommand {
    private SharePropertySubType sharePropertySubType;   // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
    private ShareInternalDetails internalDetails;
    private CapacityShare capacityShare;                // 3. 수용 인원

    @AssertTrue(message = "timeSlot의 timeFrom, timeTo는 30분 단위여야 합니다.")
    private boolean isValidTimeSlot() {
        // timeSlot is NULL
        if (super.getMeetingDateFrom() == null && super.getMeetingDateTo() == null && super.getTimeSlots() == null) {
            return true;    // patch라서 통과
        } else {
            return TimeSlotValidator.validAllConditions(super.getTimeSlots());
        }
    }
}