package org.hanihome.hanihomebe.temporaryProperty.application.validator.impl;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.application.validator.interfac.StepValidator;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConditionStepValidator implements StepValidator<ConditionDTO> {

    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.CONDITION;
    }

    @Override
    public void validate(ConditionDTO dto) {
        Map<String, Boolean> validationChecks = Map.of(
                "성별 선호", dto.genderPreference() != null,
                "입주 조건 설명", dto.livingConditions() != null, //이거 추가 검증 필요
                "입주 가능 정보", dto.moveInInfo() != null, //얘도 추가 검증 필요
                "조건 옵션 아이템", dto.conditionOptionItemIds() != null && !dto.conditionOptionItemIds().isEmpty()
        );

        validationChecks.forEach((field, isValid) -> {
            if (!isValid) {
                throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
            }
        });
    }


}
