package org.hanihome.hanihomebe.property.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    Region region();

    List<String> photoUrls();

    BigDecimal weeklyCost();

    List<Long> optionItemIds();

    String costDescription();

    BigDecimal deposit();

    BigDecimal keyDeposit();

    Integer noticePeriodWeeks();

    Integer minimumStayWeeks();

    String contractTerms();

    Set<LocalDateTime> availableFrom();

    ParkingOption parkingOption();

    Set<LocalDateTime> viewingDates();

    String description();

}
