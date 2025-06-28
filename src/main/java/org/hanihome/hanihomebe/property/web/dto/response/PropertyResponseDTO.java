package org.hanihome.hanihomebe.property.web.dto.response;

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

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyResponseDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyResponseDTO.class, name = "RENT")
})
public sealed interface PropertyResponseDTO
        permits SharePropertyResponseDTO, RentPropertyResponseDTO {

    Long id();
    Long memberId();
    PropertySuperType kind();
    GenderPreference genderPreference();
    Region region();
    List<String> photoUrls();
    BigDecimal weeklyCost();
    Boolean billIncluded();
    List<String> optionItemNames();
    String costDescription();
    BigDecimal deposit();
    BigDecimal keyDeposit();
    Integer noticePeriodWeeks();
    Integer minimumStayWeeks();
    String contractTerms();
    LocalDateTime availableFrom();
    LocalDateTime availableTo();
    Boolean negotiable();
    Boolean immediate();
    ParkingOption parkingOption();
    Set<LocalDateTime> possibleMeetingDates();
    String description();

}
