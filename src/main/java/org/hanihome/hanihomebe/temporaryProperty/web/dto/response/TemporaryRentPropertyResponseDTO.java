package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryRentInternalDetails;

@Getter
@SuperBuilder
public class TemporaryRentPropertyResponseDTO extends TemporaryPropertyResponseDTO {
    private RentPropertySubType rentPropertySubType;
    private CapacityRent capacityRent;
    private TemporaryRentInternalDetails internalDetails;
    //private RealEstateType isRealEstateIntervention;

    public static TemporaryRentPropertyResponseDTO fromEntity(TemporaryRentProperty entity) {
        return TemporaryRentPropertyResponseDTO.builder()
                .id(entity.getId())
                .kind(entity.getKind())
                .genderPreference(entity.getGenderPreference())
                .lgbtAvailable(entity.getLgbtAvailable())
                .region(entity.getRegion())
                .photoUrls(entity.getPhotoUrls())
                .costDetails(entity.getCostDetails())
                .optionItems(entity.getOptionItems().stream()
                        .map(TemporaryPropertyOptionItem::getOptionItem)
                        .map(OptionItemResponseDTO::from)
                        .toList())
                .livingConditions(entity.getLivingConditions())
                .moveInInfo(entity.getMoveInInfo())
                .meetingDateFrom(entity.getMeetingDateFrom())
                .meetingDateTo(entity.getMeetingDateTo())
                .timeSlots(entity.getTimeSlots())
                .viewingAvailableDateTimes(entity.getViewingAvailableDateTimes())
                .viewingAlwaysAvailable(entity.getViewingAlwaysAvailable())
                .description(entity.getDescription())
                .createdAt(entity.getLastModifiedAt())
                // 자식 고유 필드
                .rentPropertySubType(entity.getRentPropertySubType())
                .capacityRent(entity.getCapacityRent())
                .internalDetails(entity.getRentInternalDetails())
                //.isRealEstateIntervention(entity.getIsRealEstateIntervention())
                .build();
    }

}
