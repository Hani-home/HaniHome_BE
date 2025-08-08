package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryRentInternalDetails;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class TemporaryRentPropertyResponseDTO extends TemporaryPropertyResponseDTO {
    private RentPropertySubType rentPropertySubType;
    private CapacityRent capacityRent;
    private TemporaryRentInternalDetails internalDetails;

    public static TemporaryRentPropertyResponseDTO fromEntity(TemporaryRentProperty entity) {
        List<TimeSlot> timeSlots = entity.getTimeSlots();
        List<TimeSlot> processedTimeSlots = new ArrayList<>();
        formattingTimeSlotToSize3(timeSlots, processedTimeSlots);
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
                .timeSlots(processedTimeSlots)
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

    private static void formattingTimeSlotToSize3(List<TimeSlot> timeSlots, List<TimeSlot> processedTimeSlots) {
        if (!(timeSlots.size() == 3)) {
            timeSlots.forEach(timeSlot -> {
                processedTimeSlots.add(timeSlot);
            });
            while (processedTimeSlots.size() < 3) {
                processedTimeSlots.add(TimeSlot.getEmptyTimeSlot());
            }
        } else {
            processedTimeSlots.addAll(timeSlots);
        }
    }

}
