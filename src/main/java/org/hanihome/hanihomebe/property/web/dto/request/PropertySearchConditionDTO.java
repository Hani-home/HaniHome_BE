package org.hanihome.hanihomebe.property.web.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// @ModelAttribute를 사용하면 Jackson의 리플렉션을 쓰는 것이 아니라 MVC의 바인딩을 이용하므로
@AllArgsConstructor @Builder // for test (jackson은 아래 디폴트 생성자를 우선하여 리플렉션)
@Getter
public class PropertySearchConditionDTO {
    private List<PropertySuperType> kinds;           // 쉐어, 렌트 등
    private List<SharePropertySubType> sharePropertySubTypes;        // 아파트, 유닛 등
    private List<RentPropertySubType> rentPropertySubTypes;

    private BigDecimal minWeeklyCost;
    private BigDecimal maxWeeklyCost;
//    @JsonSetter(nulls = Nulls.SKIP) => for Jackson
    private Boolean billIncluded;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Boolean immediate;
    private Boolean negotiable;

    @DecimalMin("-90.0")  @DecimalMax("90.0")
    private BigDecimal metroStopLatitude;
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private BigDecimal metroStopLongitude;
    private BigDecimal radiusKm;         // 반경 (킬로미터)
}
