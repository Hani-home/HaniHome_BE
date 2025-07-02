package org.hanihome.hanihomebe.property.web.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import org.hanihome.hanihomebe.property.application.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

@Getter
public class SharePropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private SharePropertySubType sharePropertySubType;   // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
    private Double internalArea;                        // 2-1. 실제 사용 면적
    private Double totalArea;                           // 2-2. 전체 면적
    private Integer totalResidents;                     // 2-3. 총 거주 인원
    private Integer totalBathUser;                      // 2-4. 욕실 공유 인원
    private Integer totalFloors;                        // 2-5. 건물 총 층수
    private Integer propertyFloor;                      // 2-6. 해당 매물의 층수
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