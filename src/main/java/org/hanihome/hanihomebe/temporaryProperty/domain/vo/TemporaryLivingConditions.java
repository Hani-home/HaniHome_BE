package org.hanihome.hanihomebe.temporaryProperty.domain.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class TemporaryLivingConditions {
    // 10-1. 노티스 (주 단위)
    private Integer noticePeriodWeeks;

    // 10-2. 최소 거주 기간 (주 단위)
    private Integer minimumStayWeeks;

    // 10-3. 계약 형태 설명
    private String contractTerms;

    @JsonProperty("contractExtendable")
    private Boolean isContractExtendable;   // 계약 연장 가능

    public static TemporaryLivingConditions empty() {
        return TemporaryLivingConditions.builder()
                .noticePeriodWeeks(null)
                .minimumStayWeeks(null)
                .contractTerms(null)
                .isContractExtendable(null)
                .build();
    }
}
