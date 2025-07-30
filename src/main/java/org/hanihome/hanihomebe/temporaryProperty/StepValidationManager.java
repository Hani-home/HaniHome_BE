package org.hanihome.hanihomebe.temporaryProperty;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.AddressAndPhotosDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.DescriptionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ShareDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StepValidationManager {
    private final Map<StepValidationKey, StepValidator<?>> validatorMap;//?는 와일드 카드

    //현재 Step까지 검사
    public void validateUpToStep(TemporaryPropertyStepSaveRequestDTO dto) {
        TemporaryPropertyStepStatus currentStep = dto.getStepStatus(); //currentStep까지 검증을 해야함
        List<TemporaryPropertyStepStatus> stepsToValidate = TemporaryPropertyStepStatus.getStepsUpTo(currentStep);//처음부터 currentStep까지의 LIST

        //이게 정말 효율 적인 방법일까..
        for (TemporaryPropertyStepStatus step : stepsToValidate) {
            StepValidationKey key = new StepValidationKey(step, dto.getKind());
            StepValidator<?> validator = validatorMap.get(key);
            if (validator == null) continue;

            switch (step) {
                case ADDRESS_AND_PHOTOS -> ((StepValidator<AddressAndPhotosDTO>) validator).validate(dto.getAddressAndPhotos());
                case DETAIL -> {
                    if (dto.getDetail() instanceof RentDetailDTO rentDetailDTO) {
                        ((StepValidator<RentDetailDTO>) validator).validate(rentDetailDTO);
                    } else if (dto.getDetail() instanceof ShareDetailDTO shareDetailDTO) {
                        ((StepValidator<ShareDetailDTO>) validator).validate(shareDetailDTO);
                    }
                }
                case CONDITION -> ((StepValidator<ConditionDTO>) validator).validate(dto.getCondition());
                case CONTRACT -> ((StepValidator<ContractDTO>) validator).validate(dto.getContract());
                case COMPLETED -> ((StepValidator<DescriptionDTO>) validator).validate(dto.getDescription());
                default -> throw new IllegalArgumentException("지원하지 않는 StepStatus입니다: " + step);
            }
        }



    }
}
