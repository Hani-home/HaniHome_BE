package org.hanihome.hanihomebe.property.web.dto.request.create;

import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;

public record LivingConditionsDTO(
        Integer noticePeriodWeeks,
        Integer minimumStayWeeks,
        String contractTerms,
        boolean contractExtendable
) {
    public LivingConditions toVO() {
        return new LivingConditions(
                noticePeriodWeeks,
                minimumStayWeeks,
                contractTerms,
                contractExtendable
        );
    }
}
