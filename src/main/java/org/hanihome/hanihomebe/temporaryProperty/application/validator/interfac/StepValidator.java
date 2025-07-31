package org.hanihome.hanihomebe.temporaryProperty.application.validator.interfac;

import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;

public interface StepValidator<T> {
    TemporaryPropertyStepStatus getStepStatus();
    void validate(T stepDTO);
}

