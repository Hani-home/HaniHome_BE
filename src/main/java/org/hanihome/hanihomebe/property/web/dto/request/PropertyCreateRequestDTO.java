package org.hanihome.hanihomebe.property.web.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.vo.*;

import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "jsonDiscriminator"      // "jsonCriminator"라는 필드가 요청 바디에 필드로 포함되어있어야함
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyCreateRequestDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyCreateRequestDTO.class, name = "RENT")
})
public sealed interface PropertyCreateRequestDTO permits
        SharePropertyCreateRequestDTO, RentPropertyCreateRequestDTO {
    Long memberId();

    PropertySuperType kind();

    GenderPreference genderPreference();

    boolean lgbtAvailable();

    Region region();

    List<String> photoUrls();

    CostDetails costDetails();

    List<Long> optionItemIds();

    LivingConditions livingConditions();

//    Set<LocalDateTime> availableFrom();
    MoveInInfo moveInInfo();

    ParkingOption parkingOption();

    LocalDate meetingDateFrom();
    LocalDate meetingDateTo();
    List<TimeSlot> timeSlots();
    List<ViewingAvailableDateTime> viewingAvailableDateTimes();
    boolean viewingAlwaysAvailable();

    String description();

}
