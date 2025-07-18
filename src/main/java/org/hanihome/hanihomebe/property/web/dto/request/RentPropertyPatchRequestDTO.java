package org.hanihome.hanihomebe.property.web.dto.request;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import org.hanihome.hanihomebe.property.application.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.command.RentPropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;

import java.util.List;

@Getter
public class RentPropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private RentPropertySubType rentPropertySubType;    // (RentProperty 고유) 매물 유형
    private RealEstateType realEstateIntervention;    // (RentProperty 고유) 부동산 중개 여부
    private RentInternalDetails internalDetails;
    private CapacityRent capacityRent;                  // (RentProperty 고유) 수용인원-렌트
    private Exposure exposure;                           // (RentProperty 고유) 남향북향

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
        return RentPropertyPatchCommand.builder()
                // 공통 필드
                .genderPreference(super.getGenderPreference())
                .lgbtAvailable(super.getLgbtAvailable())
                .region(super.getRegion())
                .photoUrls(super.getPhotoUrls())
                .optionItems(propertyOptionItems)
                .costDetails(super.getCostDetails())
                .livingConditions(super.getLivingConditions())
                .moveInInfo(super.getMoveInInfo())
                .parkingOption(super.getParkingOption())
                .meetingDateFrom(super.getMeetingDateFrom())
                .meetingDateTo(super.getMeetingDateTo())
                .timeSlots(super.getTimeSlots())
                .viewingAlwaysAvailable(super.getViewingAlwaysAvailable())
                .description(super.getDescription())
                .displayStatus(super.getDisplayStatus())
                .tradeStatus(super.getTradeStatus())

                // RentProperty 전용 필드
                .rentPropertySubType(this.rentPropertySubType)
                .internalDetails(this.internalDetails)
                .capacityRent(this.capacityRent)
                .build();
    }

}
