package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.RENT;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.SHARE;
import static org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus.ADDRESS_AND_PHOTOS;
import static org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus.CONDITION;
import static org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus.CONTRACT;
import static org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus.DETAIL;

@Configuration
public class StepValidationConfig {

    @Bean //Manager를 빈으로 등록
    public StepValidationManager stepValidationManager(
            AddressAndPhotosStepValidator addressAndPhotosStepValidator,
            RentDetailStepValidator rentDetailStepValidator,
            ShareDetailStepValidator shareDetailStepValidator,
            ConditionStepValidator conditionStepValidator,
            ContractStepValidator contractStepValidator
    ) {
        Map<StepValidationKey, StepValidator<?>> validatorMap = new HashMap<>();
        validatorMap.put(new StepValidationKey(DETAIL, RENT), rentDetailStepValidator);
        validatorMap.put(new StepValidationKey(DETAIL, SHARE), shareDetailStepValidator);

        validatorMap.put(new StepValidationKey(ADDRESS_AND_PHOTOS, null), addressAndPhotosStepValidator);
        validatorMap.put(new StepValidationKey(CONDITION, null), conditionStepValidator);
        validatorMap.put(new StepValidationKey(CONTRACT, null), contractStepValidator);
        return new StepValidationManager(validatorMap);
    }
}
