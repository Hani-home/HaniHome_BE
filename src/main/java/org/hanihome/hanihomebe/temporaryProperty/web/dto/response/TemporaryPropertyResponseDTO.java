package org.hanihome.hanihomebe.temporaryProperty.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryMoveInInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@SuperBuilder
public abstract class TemporaryPropertyResponseDTO {
    private Long id;
    private PropertySuperType kind;
    private GenderPreference genderPreference;
    private Boolean lgbtAvailable;
    private Region region;
    private List<String> photoUrls;
    private TemporaryCostDetails costDetails;
    private List<OptionItemResponseDTO> optionItems;
    private TemporaryLivingConditions livingConditions;
    private TemporaryMoveInInfo moveInInfo;
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;
    private List<TimeSlot> timeSlots;
    private List<ViewingAvailableDateTime> viewingAvailableDateTimes;
    private Boolean viewingAlwaysAvailable;
    private String description;
    private LocalDateTime createdAt;
}
