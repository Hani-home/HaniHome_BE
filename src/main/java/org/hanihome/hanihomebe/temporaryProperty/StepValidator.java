package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;

public interface StepValidator<T> {
    TemporaryPropertyStepStatus getStepStatus();
    void validate(T stepDTO);
}

