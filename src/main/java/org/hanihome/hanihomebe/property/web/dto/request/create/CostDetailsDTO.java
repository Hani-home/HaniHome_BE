package org.hanihome.hanihomebe.property.web.dto.request.create;

import jakarta.persistence.Column;
import lombok.Builder;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;

import java.math.BigDecimal;

@Builder
public record CostDetailsDTO(
         BigDecimal weeklyCost,
         boolean billIncluded,
         String costDescription,
         BigDecimal deposit,
         BigDecimal keyDeposit,
         boolean depositAdjustable
) {
    public CostDetails toVO() {
        return new CostDetails(
                weeklyCost,
                billIncluded,
                costDescription,
                deposit,
                keyDeposit,
                depositAdjustable
        );
    }
}
