package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContractStepValidator implements StepValidator<ContractDTO> {

    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.CONTRACT;
    }

    @Override
    public void validate(ContractDTO dto) {
        Map<String, Boolean> validationChecks = Map.of(
                "계약 비용 정보", dto.costDetails() != null, //추가 검증필요
                "빌에 포함된 옵션 항목", dto.IncludedOptionItemIds() != null && !dto.IncludedOptionItemIds().isEmpty(),
                "뷰잉 가능 시작일", dto.meetingDateFrom() != null,
                "뷰잉 가능 종료일", dto.meetingDateTo() != null,
                "뷰잉 가능 시간대", dto.timeSlots() != null && !dto.timeSlots().isEmpty() //이것도 검증필요
        );

        validationChecks.forEach((field, isValid) -> {
            if (!isValid) {
                throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
            }
        });

        // 날짜 순서 검증 추가
        if (dto.meetingDateFrom() != null && dto.meetingDateTo() != null) {
            if (dto.meetingDateFrom().isAfter(dto.meetingDateTo())) {
                throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
            }
        }
    }
}
