package org.hanihome.hanihomebe.temporaryProperty.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class TemporaryCostDetails {
    // 8-1. 비용 (주 단위)
    private BigDecimal weeklyCost;

    private boolean isBillIncluded;

    // 8-3. bill 설명
    @Column(columnDefinition = "TEXT")
    private String costDescription;

    // 8-4. 보증금
    private BigDecimal deposit;

    // 8-5. 키 보증금
    private BigDecimal keyDeposit;

    private boolean isDepositAdjustable;
}
