package org.hanihome.hanihomebe.property.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.option.PropertyOptionItem;

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
        @JsonSubTypes.Type(value = SharePropertyPatchRequestDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyPatchRequestDTO.class, name = "RENT")
})
@Getter
public abstract class PropertyPatchRequestDTO {
    private CapacityShare capacity;
    private GenderPreference genderPreference;
    private Region region;
    private List<String> photoUrls;
    private BigDecimal weeklyCost;
    private List<PropertyOptionItem> optionItems;     // 포함된 비용 항목 리스트
    private String costDescription;
    private BigDecimal deposit;
    private BigDecimal keyDeposit;
    private Integer noticePeriodWeeks;
    private Integer minimumStayWeeks;
    private String contractTerms;
    private Set<LocalDateTime> availableFrom;
    private ParkingOption parkingOption;
    private Set<LocalDateTime> viewingDates;
    private String description;

}
