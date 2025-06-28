package org.hanihome.hanihomebe.property.web.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor @Builder // for test (jackson은 아래 디폴트 생성자를 우선하여 리플렉션)
@Getter
@NoArgsConstructor
public class PropertySearchConditionDTO {
    private List<PropertySuperType> kinds;           // 쉐어, 렌트 등
    private List<SharePropertySubType> sharePropertySubTypes;        // 아파트, 유닛 등
    private List<RentPropertySubType> rentPropertySubTypes;

    private BigDecimal minWeeklyCost;
    private BigDecimal maxWeeklyCost;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean billIncluded = false;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean immediate = false;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean negotiable = false;

    private String stationCode;      // 역 코드
    private Double radiusKm;         // 반경 (킬로미터)
}
