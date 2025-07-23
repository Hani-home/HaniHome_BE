package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ShareDetailDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShareDetailStepValidator implements StepValidator<ShareDetailDTO> {
    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.DETAIL;
    }

    @Override
    public void validate(ShareDetailDTO dto) {
        Map<String, Boolean> validationChecks = Map.of(
                "매물 유형", dto.sharePropertySubType() != null,
                "수용 인원", dto.capacityShare() != null,
                "매물 정보", dto.shareInternalDetails() != null, //추가 검증 필요
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
