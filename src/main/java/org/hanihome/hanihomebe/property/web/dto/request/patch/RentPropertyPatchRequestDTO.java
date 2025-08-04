package org.hanihome.hanihomebe.property.web.dto.request.patch;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import org.hanihome.hanihomebe.property.application.time.validator.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.command.RentPropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.property.web.dto.request.create.CostDetailsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.LivingConditionsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.MoveInInfoDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.RentInternalDetailsDTO;

import java.util.List;

@Getter
public class RentPropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private RentInternalDetailsDTO internalDetails;
    private CapacityRent capacityRent;                  // (RentProperty 고유) 수용인원-렌트

    @AssertTrue(message = "timeSlot의 timeFrom, timeTo는 30분 단위여야 합니다.")
    private boolean isValidTimeSlot() {
        if (timeSlotIsNull()) {
            return true;    // patch라서 통과
        } else {
            return TimeSlotValidator.validateAllConditions(super.getTimeSlots());
        }
    }

    private boolean timeSlotIsNull() {
        return super.getMeetingDateFrom() == null && super.getMeetingDateTo() == null && super.getTimeSlots() == null;
    }

    @Override
    public PropertyPatchCommand toCommand(List<PropertyOptionItem> propertyOptionItems) {
        LivingConditionsDTO livingConditionsDTO = super.getLivingConditions();
        MoveInInfoDTO moveInInfoDTO = super.getMoveInInfo();
        CostDetailsDTO costDetailsDTO = super.getCostDetails();
        RentInternalDetailsDTO rentInternalDetailsDTO = this.internalDetails;
        return RentPropertyPatchCommand.builder()
                // 공통 필드
                .genderPreference(super.getGenderPreference())
                .lgbtAvailable(super.getLgbtAvailable())
                .region(super.getRegion())
                .photoUrls(super.getPhotoUrls())
                .optionItems(propertyOptionItems)
                .costDetails(costDetailsDTO != null ? costDetailsDTO.toVO() : null)
                .livingConditions(livingConditionsDTO != null ? livingConditionsDTO.toVO() : null)
                .moveInInfo(moveInInfoDTO != null ? moveInInfoDTO.toVO() : null)
                .meetingDateFrom(super.getMeetingDateFrom())
                .meetingDateTo(super.getMeetingDateTo())
                .timeSlots(super.getTimeSlots())
                .viewingAlwaysAvailable(super.getViewingAlwaysAvailable())
                .description(super.getDescription())
                .displayStatus(super.getDisplayStatus())

                // RentProperty 전용 필드
                .rentPropertySubType(this.rentPropertySubType)
                .internalDetails(rentInternalDetailsDTO != null ? rentInternalDetailsDTO.toVO() : null)
                .capacityRent(this.capacityRent)
                .build();
    }

}

