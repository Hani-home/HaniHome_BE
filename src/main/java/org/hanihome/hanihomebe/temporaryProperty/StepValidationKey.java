package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;

public record StepValidationKey(
        TemporaryPropertyStepStatus statusStep,
        PropertySuperType kind
){

}

