package org.hanihome.hanihomebe.property.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.TimeSlot;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "jsonDiscriminator"      // "jsonCriminator"라는 필드가 요청 바디에 필드로 포함되어있어야함
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyPatchRequestDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyPatchRequestDTO.class, name = "RENT")
})
@Getter
public abstract class PropertyPatchRequestDTO {
    private GenderPreference genderPreference;
    private Region region;
    private List<String> photoUrls;
    private BigDecimal weeklyCost;
    private Boolean billIncluded;
    private List<PropertyOptionItem> optionItems;     // 포함된 비용 항목 리스트, TODO: OptionItemId를 받아야할듯
    private String costDescription;
    private BigDecimal deposit;
    private BigDecimal keyDeposit;
    private Integer noticePeriodWeeks;
    private Integer minimumStayWeeks;
    private String contractTerms;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Boolean immediate;
    private Boolean negotiable;
    private ParkingOption parkingOption;
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;
    private List<TimeSlot> timeSlots;

    private String description;
    private DisplayStatus displayStatus;
    private TradeStatus tradeStatus;

}
