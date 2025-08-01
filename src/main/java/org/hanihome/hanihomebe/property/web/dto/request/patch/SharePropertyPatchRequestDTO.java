package org.hanihome.hanihomebe.property.web.dto.request.patch;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import org.hanihome.hanihomebe.property.application.time.validator.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.command.SharePropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.property.web.dto.request.create.ShareInternalDetailsDTO;

import java.util.List;

@Getter
public class SharePropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private SharePropertySubType sharePropertySubType;   // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
    private ShareInternalDetailsDTO internalDetails;
    private CapacityShare capacityShare;                // 3. 수용 인원

    @AssertTrue(message = "timeSlot의 timeFrom, timeTo는 30분 단위여야 합니다.")
    private boolean isValidTimeSlot() {
        // timeSlot is NULL
        if (super.getMeetingDateFrom() == null && super.getMeetingDateTo() == null && super.getTimeSlots() == null) {
            return true;    // patch라서 통과
        } else {
            return TimeSlotValidator.validateAllConditions(super.getTimeSlots());
        }
    }

    @Override
    public PropertyPatchCommand toCommand(List<PropertyOptionItem> propertyOptionItems) {
        return SharePropertyPatchCommand.builder()
                // 공통 필드
                .genderPreference(super.getGenderPreference())
                .lgbtAvailable(super.getLgbtAvailable())
                .region(super.getRegion())
                .photoUrls(super.getPhotoUrls())
                .optionItems(propertyOptionItems)
                .costDetails(super.getCostDetails().toVO())
                .livingConditions(super.getLivingConditions().toVO())
                .moveInInfo(super.getMoveInInfo().toVO())
                .meetingDateFrom(super.getMeetingDateFrom())
                .meetingDateTo(super.getMeetingDateTo())
                .timeSlots(super.getTimeSlots())
                .viewingAlwaysAvailable(super.getViewingAlwaysAvailable())
                .description(super.getDescription())
                .displayStatus(super.getDisplayStatus())

                // ShareProperty 전용 필드
                .sharePropertySubType(this.sharePropertySubType)
                .internalDetails(this.internalDetails.toVO())
                .capacityShare(this.capacityShare)
                .build();
    }

}