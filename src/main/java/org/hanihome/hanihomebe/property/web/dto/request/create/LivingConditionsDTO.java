package org.hanihome.hanihomebe.property.web.dto.request.create;

import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;

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

    public TemporaryLivingConditions toTemporaryVO() {

        return new TemporaryLivingConditions(
                noticePeriodWeeks,
                minimumStayWeeks,
                contractTerms,
                contractExtendable
        );
    }
}
