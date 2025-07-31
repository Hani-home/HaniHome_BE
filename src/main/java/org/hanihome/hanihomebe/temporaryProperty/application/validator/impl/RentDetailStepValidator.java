package org.hanihome.hanihomebe.temporaryProperty.application.validator.impl;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.application.validator.interfac.StepValidator;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RentDetailStepValidator implements StepValidator<RentDetailDTO> {
    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.DETAIL;
    }

    @Override
    public void validate(RentDetailDTO dto) {
        Map<String, Boolean> validationChecks = Map.of(
                "매물 유형", dto.rentPropertySubType() != null,
                "수용 인원", dto.capacityRent() != null,
                "매물 정보", dto.rentInternalDetails() != null, //추가 검증필요
                "부동산 중개 여부", dto.isRealEstateType() != null,
                "기본 제공 옵션", dto.optionItemIds() != null && !dto.optionItemIds().isEmpty(),
                "매물 장점", dto.highlightOptionItemIds() != null && !dto.highlightOptionItemIds().isEmpty()
        );

        validationChecks.forEach((field, isValid) -> {
            if (!isValid) {
                throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
            }
        });


    }

}
