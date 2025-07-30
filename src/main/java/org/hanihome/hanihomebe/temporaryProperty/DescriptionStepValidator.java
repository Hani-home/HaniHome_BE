package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.DescriptionDTO;

public class DescriptionStepValidator implements StepValidator<DescriptionDTO>{

    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.COMPLETED;
    }

    @Override
    public void validate(DescriptionDTO dto) {
        if (dto.description() != null && !dto.description().isBlank()) {
            // comment가 null 아니고 공백이 아니면 유효성 검사 로직 실행
            if (dto.description().length() > 200) { // 예시로 길이 제한
                throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
            }
        }
        // comment가 null이면 그냥 통과 (검사 안 함)
    }

}
