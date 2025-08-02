package org.hanihome.hanihomebe.temporaryProperty.web.dto.create;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.web.dto.request.create.CostDetailsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.LivingConditionsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.MoveInInfoDTO;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;

import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,               // 타입을 문자열 이름으로 구분
        include = JsonTypeInfo.As.PROPERTY,       // JSON 안에 필드로 포함
        property = "jsonDiscriminator"            // JSON 안에 들어갈 필드명
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TemporaryRentPropertyCreateRequestDTO.class, name = "RENT"),
        @JsonSubTypes.Type(value = TemporarySharePropertyCreateRequestDTO.class, name = "SHARE")
})
public interface TemporaryPropertyCreateRequestDTO {//seal하면 좋을 듯
    PropertySuperType kind();

    GenderPreference genderPreference();

    boolean lgbtAvailable();

    Region region();

    List<String> photoUrls();

    CostDetailsDTO costDetails();

    List<Long> optionItemIds();

    LivingConditionsDTO livingConditions();

    //이것도 temporary용으로 따로....?
    MoveInInfoDTO moveInInfo();

    LocalDate meetingDateFrom();
    LocalDate meetingDateTo();
    List<TimeSlot> timeSlots();
    List<ViewingAvailableDateTime> viewingAvailableDateTimes();
    boolean viewingAlwaysAvailable();

    String description();
}
