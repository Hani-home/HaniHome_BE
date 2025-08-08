package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryShareProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryShareInternalDetails;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class TemporarySharePropertyResponseDTO extends TemporaryPropertyResponseDTO {
    private SharePropertySubType sharePropertySubType;
    private CapacityShare capacityShare;
    private TemporaryShareInternalDetails internalDetails;

    public static TemporarySharePropertyResponseDTO fromEntity(TemporaryShareProperty entity) {
        List<TimeSlot> timeSlots = entity.getTimeSlots();
        List<TimeSlot> processedTimeSlots = new ArrayList<>();
        formattingTimeSlotToSize3(timeSlots, processedTimeSlots);

        return TemporarySharePropertyResponseDTO.builder()
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
                .sharePropertySubType(entity.getSharePropertySubType())
                .capacityShare(entity.getCapacityShare())
                .internalDetails(entity.getShareInternalDetails())
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
